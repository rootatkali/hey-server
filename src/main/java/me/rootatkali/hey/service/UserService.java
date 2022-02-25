package me.rootatkali.hey.service;

import me.rootatkali.hey.model.User;
import me.rootatkali.hey.model.UserPreferences;
import me.rootatkali.hey.repo.UserPreferencesRepository;
import me.rootatkali.hey.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
  private final UserRepository userRepo;
  private final UserPreferencesRepository userPrefsRepo;
  
  @Autowired
  public UserService(UserRepository userRepo,
                     UserPreferencesRepository userPrefsRepo) {
    this.userRepo = userRepo;
    this.userPrefsRepo = userPrefsRepo;
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
  
  public User addUser(User u) {
    // todo delete
    return userRepo.save(u);
  }
}
