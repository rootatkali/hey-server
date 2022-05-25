package me.rootatkali.hey.service;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import me.rootatkali.hey.model.*;
import me.rootatkali.hey.repo.FriendshipRepository;
import me.rootatkali.hey.repo.InterestRepository;
import me.rootatkali.hey.repo.UserPreferencesRepository;
import me.rootatkali.hey.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * A service handling functionality related to friends and user interests.
 */
@Service
public class FriendService {
  private final UserRepository userRepo;
  private final InterestRepository interestRepo;
  private final FriendshipRepository friendRepo;
  private final UserPreferencesRepository userPrefsRepo; // TODO take prefs into account
  private final UserService userService;
  
  /** An approximate value for the earth's radius, in km */
  private static final double R_E = 6.3781E3;
  
  @Autowired
  public FriendService(UserRepository userRepo,
                       InterestRepository interestRepo,
                       FriendshipRepository friendRepo,
                       UserPreferencesRepository userPrefsRepo,
                       UserService userService) {
    this.userRepo = userRepo;
    this.interestRepo = interestRepo;
    this.friendRepo = friendRepo;
    this.userPrefsRepo = userPrefsRepo;
    this.userService = userService;
  }
  
  public List<Interest> getAllInterests() {
    List<Interest> all = Lists.newArrayList(interestRepo.findAll());
    Collections.sort(all);
    return all;
  }
  
  public Interest addInterest(String name) {
    Interest i = new Interest();
    i.setName(name);
    return interestRepo.save(i);
  }
  
  public List<Interest> setUserInterests(User user, List<Interest> interests, List<String> newInterests) {
    // register new interests and merge lists
    List<Interest> added = newInterests != null ? newInterests.stream().map(this::addInterest).toList() : List.of();
    interests.addAll(added);
    
    user.setInterests(interests);
    user = userRepo.save(user);
    return user.getInterests();
  }
  
  public List<Interest> setUserInterests(User user, List<Interest> interests) {
    return setUserInterests(user, interests, null);
  }
  
  /** Returns the constant c² for normal curve calculations, based on the requested value at a specific point x. */
  private double c(double x, double f) {
    return - (x * x) / Math.log(f);
  }
  
  // f(2) = 0.5
  private double ageCurve(double x) {
    return Math.exp(-(x * x) / c(2, 0.5));
  }
  
  private double hav(double deg) {
    return (1 - Math.cos(Math.toRadians(deg))) / 2;
  }
  
  /**
   * Parameters in degrees
   */
  private double d(double la1, double lo1, double la2, double lo2) {
    return 2 * R_E * Math.asin(
        Math.sqrt(hav(la2 - la1) + (1 - hav(la1 - la2) - hav(la1 + la2)) * hav(lo2 - lo1))
    );
  }
  
  // f(12) = 0.5
  private double distanceCurve(double la1, double lo1, double la2, double lo2) {
    return Math.exp(-Math.pow(d(la1, lo1, la2, lo2), 2) / c(12, 0.5));
  }
  
  // Logistic curve
  // Why 10 and 0.46? Approx: f(0)=0, f(1)=1
  private double interestCurve(double x) {
    if (x == 1) return x;
    return 1 / (1 + Math.exp(-10 * (x - 0.46)));
  }
  
  /**
   * Calculate the match score between two users.
   *
   * <p>The algorithm takes the following factors into account:
   * <ul>
   *   <li>The age (grade) difference (20%) - calculated using a normal curve.</li>
   *   <li>The proximity between user's homes (15%) - calculated using a normal curve.</li>
   *   <li>Same school bonus (5%) - binary.</li>
   *   <li>The interest match between the users (60%) - calculated using the ratio between the shared interests
   *   and the interest union group, and normalized using a logistic curve.</li>
   * </ul>
   * @param a The first user
   * @param b The second user
   * @return A double value between 0.0 and 1.0 (inclusive), where 1 is a perfect match and 0 is no match.
   */
  public double match(User a, User b) {
    // Calculate grade score element
    final double gradeWeight = 0.2;
    int ageDiff = Math.abs(a.getGrade() - b.getGrade());
    double gradeScoreElement = gradeWeight * ageCurve(ageDiff);
    
    // Calculate location (address) score element
    final double locationWeight = 0.15;
    Location la = a.getLocation();
    Location lb = b.getLocation();
    
    double locationScoreElement = locationWeight * distanceCurve(la.lat(), la.lon(), lb.lat(), lb.lon());
  
    // Calculate school affiliation score element
    final double schoolWeight = 0.05;
    int isSameSchool = (a.getSchool().getId() == b.getSchool().getId()) ? 1 : 0;
    double schoolScoreElement = schoolWeight * isSameSchool;
    
    // Calculate interest score element
    final double interestWeight = 0.6;
    
    // Count unique interests
    Set<Interest> allInterests = new HashSet<>();
    allInterests.addAll(a.getInterests());
    allInterests.addAll(b.getInterests());
    double total = allInterests.size();
    
    // Count common interests
    List<Interest> intersect = new ArrayList<>(a.getInterests());
    intersect.retainAll(b.getInterests());
    double common = intersect.size();
    
    // Calculate score
    double commonInterests = total > 0 ? common / total : 0; // if no interests at all (should never happen in prod)
    double interestScoreElement = interestWeight * interestCurve(commonInterests);
    
    return gradeScoreElement + locationScoreElement + schoolScoreElement + interestScoreElement;
  }
  
  private FriendView userToFriendView(User friend, User requester, double score) {
    var status = friendRepo
        .findByTwoUsers(friend, requester)
        .map(Friendship::getStatus)
        .orElse(Friendship.Status.STRANGER);
    
    boolean initiator = friendRepo.findByTwoUsers(friend, requester)
        .map(f -> f.getInvitor().equals(friend)).orElse(false);
        
    Location fLoc = friend.getLocation(), rLoc = requester.getLocation();
    double distance;
    if (fLoc == null || rLoc == null) distance = -1;
    else distance = d(fLoc.lat(), fLoc.lon(), rLoc.lat(), rLoc.lon());
    
    return new FriendView(
        friend.getId(),
        friend.getUsername(),
        friend.getFirstName(),
        friend.getLastName(),
        status,
        initiator,
        friend.getBio(),
        friend.getSchool(),
        friend.getInterests(),
        friend.getHometown(),
        distance,
        friend.getGrade(),
        friend.getGender(),
        score
    );
  }
  
  public FriendView getFriendView(User user, String friend) {
    User fUser = userService.getUser(friend);
    return userToFriendView(fUser, user, match(user, fUser));
  }
  
  public List<FriendView> matchAllUsers(User u) {
    List<User> allUsers = Lists.newArrayList(userRepo.findAll());
    allUsers.remove(u);
    
    // cache for the match operation
    Map<User, Double> matchValues = new HashMap<>();
    allUsers.forEach(usr -> matchValues.put(usr, match(u, usr)));
    
    allUsers.sort(
        Collections.reverseOrder((a, b) -> Doubles.compare(matchValues.get(a), matchValues.get(b)))
    );
    
    return allUsers.stream().map(
        usr -> userToFriendView(usr, u, matchValues.get(usr))
    ).toList();
  }
  
  public Iterable<Friendship> getFriendData(User u) {
    return friendRepo.getFriendshipByUser(u);
  }
}
