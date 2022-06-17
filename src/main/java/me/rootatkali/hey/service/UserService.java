package me.rootatkali.hey.service;

import me.rootatkali.hey.model.*;
import me.rootatkali.hey.repo.LocationRepository;
import me.rootatkali.hey.repo.UserPreferencesRepository;
import me.rootatkali.hey.repo.UserRepository;
import me.rootatkali.hey.util.Error;
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
    return userRepo.findById(id).orElseThrow(Error.NOT_FOUND);
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
   * @param user The user, as fetched from the database
   * @param edit The requested edits to the user
   * @return <code>user</code>, after being edited accordingly
   */
  public User editUser(User user, User edit) {
    if (edit.getUsername() != null) {
      validator.validateUsername(edit.getUsername());
      user.setUsername(edit.getUsername());
    }
    
    if (edit.getFirstName() != null) {
      validator.validateName(edit.getFirstName());
      user.setFirstName(Xss.deXss(edit.getFirstName()));
    }
    
    if (edit.getLastName() != null) {
      validator.validateName(edit.getLastName());
      user.setFirstName(Xss.deXss(edit.getLastName()));
    }
    
    if (edit.getPhoneNum() != null) {
      validator.validatePhoneNumber(edit.getPhoneNum());
      user.setPhoneNum(edit.getPhoneNum());
    }
    
    if (edit.getEmail() != null) {
      validator.validateEmail(edit.getEmail());
      user.setEmail(edit.getEmail());
    }
    
    if (edit.getHometown() != null) {
      validator.validateName(edit.getHometown());
      user.setHometown(edit.getHometown());
    }
    
    if (edit.getGrade() != null && edit.getGrade() != 0) {
      validator.validateGrade(edit.getGrade());
      user.setGrade(edit.getGrade());
    }
    
    if (edit.getGender() != null && edit.getGender() != '\u0000') {
      validator.validateGender(edit.getGender());
      user.setGender(edit.getGender());
    }
    
    if (edit.getBio() != null) {
      validator.validateBio(edit.getBio());
      user.setBio(Xss.deXss(edit.getBio()));
    }
    
    return userRepo.save(user);
  }
  
  /**
   * For use in debug only
   *
   * @deprecated To remove before production
   */
  @Deprecated(forRemoval = true)
  public User setSchool(User user, School school) {
    user.setSchool(school);
    return userRepo.save(user);
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
