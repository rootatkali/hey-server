package me.rootatkali.hey.service;

import me.rootatkali.hey.model.LatLon;
import me.rootatkali.hey.model.Location;
import me.rootatkali.hey.model.User;
import me.rootatkali.hey.model.UserPreferences;
import me.rootatkali.hey.repo.LocationRepository;
import me.rootatkali.hey.repo.UserPreferencesRepository;
import me.rootatkali.hey.repo.UserRepository;
import me.rootatkali.hey.util.Xss;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepo;
  private final UserPreferencesRepository userPrefsRepo;
  private final LocationRepository locationRepo;
  private final Validator validator;
  
  @Autowired
  public UserService(UserRepository userRepo,
                     UserPreferencesRepository userPrefsRepo,
                     LocationRepository locationRepo,
                     Validator validator) {
    this.userRepo = userRepo;
    this.userPrefsRepo = userPrefsRepo;
    this.locationRepo = locationRepo;
    this.validator = validator;
  }
  
  public Iterable<User> getUsers() {
    return userRepo.findAll();
  }
  
  public User getUser(String id) {
    return userRepo.findById(id).orElse(null);
  }
  
  public Iterable<UserPreferences> getAllUserPrefs() {
    return userPrefsRepo.findAll();
  }
  
  public UserPreferences getUserPrefs(String id) {
    return userPrefsRepo.findById(id).orElse(null);
  }
  
  public UserPreferences getUserPrefs(User user) {
    return user.getPrefs();
  }
  
  /**
   * Provides a PATCH method for the user entity.
   * @param db The user, as fetched from the database
   * @param edit The requested edits to the user
   * @return <code>db</code>, after being edited accordingly
   */
  public User editUser(User db, User edit) {
    if (edit.getUsername() != null) {
      validator.validateUsername(edit.getUsername());
      db.setUsername(edit.getUsername());
    }
    
    if (edit.getFirstName() != null) {
      validator.validateName(edit.getFirstName());
      db.setFirstName(Xss.deXss(edit.getFirstName()));
    }
    
    if (edit.getLastName() != null) {
      validator.validateName(edit.getLastName());
      db.setFirstName(Xss.deXss(edit.getLastName()));
    }
    
    if (edit.getPhoneNum() != null) {
      validator.validatePhoneNumber(edit.getPhoneNum());
      db.setPhoneNum(edit.getPhoneNum());
    }
    
    if (edit.getEmail() != null) {
      validator.validateEmail(edit.getEmail());
      db.setEmail(edit.getEmail());
    }
    
    if (edit.getHometown() != null) {
      validator.validateName(edit.getHometown());
      db.setHometown(edit.getHometown());
    }
    
    if (edit.getGrade() != null && edit.getGrade() != 0) {
      validator.validateGrade(edit.getGrade());
      db.setGrade(edit.getGrade());
    }
    
    if (edit.getGender() != null && edit.getGender() != '\u0000') {
      validator.validateGender(edit.getGender());
      db.setGender(edit.getGender());
    }
    
    if (edit.getBio() != null) {
      validator.validateBio(edit.getBio());
      db.setBio(Xss.deXss(edit.getBio()));
    }
    
    return userRepo.save(db);
  }
  
  public LatLon setLocation(User user, Location location) {
    if (locationRepo.existsById(user.getId())) locationRepo.deleteById(user.getId());
    location.setUser(user);
    locationRepo.save(location);
    return new LatLon(location.lat(), location.lon());
  }
  
  public Location getLocation(User user) {
    return user.getLocation();
  }
}
