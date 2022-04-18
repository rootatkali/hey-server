package me.rootatkali.hey.service;

import me.rootatkali.hey.model.*;
import me.rootatkali.hey.repo.AuthRepository;
import me.rootatkali.hey.repo.TokenRepository;
import me.rootatkali.hey.repo.UserPreferencesRepository;
import me.rootatkali.hey.repo.UserRepository;
import me.rootatkali.hey.util.Error;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
public class AuthService {
  private final AuthRepository authRepo;
  private final UserRepository userRepo;
  private final UserPreferencesRepository prefsRepo;
  private final TokenRepository tokenRepo;
  private final Validator validator;
  private final SecureRandom random; // for salt generation purposes
  
  private static final String SALT_CHARS = "01234589" +
      "abcdefghijklmnopqrstuvwxyz" +
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
      ".!@#$%^&*()_+-=~`[]{}\\|/?<>'\";:";
  
  private static final int SCL = SALT_CHARS.length();
  
  @Autowired
  public AuthService(AuthRepository authRepo,
                     UserRepository userRepo,
                     UserPreferencesRepository prefsRepo,
                     TokenRepository tokenRepo,
                     Validator validator) {
    this.authRepo = authRepo;
    this.userRepo = userRepo;
    this.prefsRepo = prefsRepo;
    this.tokenRepo = tokenRepo;
    this.validator = validator;
    this.random = new SecureRandom();
  }
  
  
  
  private void validateRegistration(UserRegistration reg) {
    validator.validateUsername(reg.getUsername());
    validator.validateName(reg.getFirstName());
    validator.validateName(reg.getLastName());
    validator.validateEmail(reg.getEmail());
    validator.validatePhoneNumber(reg.getPhoneNum());
  }
  
  private String generateSalt() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 32; i++) {
      sb.append(SALT_CHARS.charAt(random.nextInt(SCL)));
    }
    return sb.toString();
  }
  
  private String hashPassword(String password, String salt) {
    String raw = password + salt;
    return new DigestUtils("SHA3-256").digestAsHex(raw);
  }
  
  // generate token with an expiry date of 2099-12-31T23:59:59
  private Token generateToken(User u) {
    Token t = new Token();
    t.setUser(u);
    t.setExpires(Timestamp.valueOf(LocalDateTime.of(2099, 12, 31, 23, 59, 59)));
    return tokenRepo.save(t);
  }
  
  public Token registerUser(UserRegistration reg) {
    // validate registration fields
    validateRegistration(reg);
    
    // create user with data fields and store in db
    User u = new User();
    u.setUsername(reg.getUsername());
    u.setBirthdate(reg.getBirthdate());
    u.setFirstName(reg.getFirstName());
    u.setLastName(reg.getLastName());
    u.setEmail(reg.getEmail());
    u.setPhoneNum(reg.getPhoneNum());
    
    // TODO validation
    u.setGender('X');
    u.setGrade(0);
    
    u = userRepo.save(u);
    
    // create auth entry
    Auth a = new Auth();
    a.setUser(u);
    
    String salt = generateSalt();
    a.setSalt(salt);
    a.setPassword(hashPassword(reg.getPassword(), salt));
    a.setPasswordExpires(Timestamp.valueOf(LocalDateTime.now().plusDays(90)));
    a = authRepo.save(a);
  
    UserPreferences up = new UserPreferences();
    up.setUser(u);
    up = prefsRepo.save(up);
    
    // create token
    return generateToken(u);
  }
  
  public Token login(String username, String password) {
    User u = userRepo.findByUsername(username).orElseThrow(Error.INVALID_LOGIN);
    Auth a = u.getAuth();
    
    // verify password
    String hash = hashPassword(password, a.getSalt());
    if (!a.getPassword().equals(hash)) throw Error.INVALID_LOGIN.get();
    if (a.getPasswordExpires().toLocalDateTime().isBefore(LocalDateTime.now()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Reset your password.");
    
    // create token
    return generateToken(u);
  }
  
  public User validateAccessToken(String token) {
    if (token == null) throw Error.UNAUTHORIZED.get();
    Token t = tokenRepo.findById(token).orElseThrow(Error.UNAUTHORIZED);
    if (t.getExpires().toLocalDateTime().isBefore(LocalDateTime.now()))
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Your token expired, please log in again.");
    return t.getUser();
  }
  
  public Token resetPassword(String username, String oldPass, String newPass) {
    User u = userRepo.findByUsername(username).orElseThrow(Error.UNAUTHORIZED);
    Auth a = u.getAuth();
    
    String hash = hashPassword(oldPass, a.getSalt());
    if (!a.getPassword().equals(hash)) throw Error.BAD_REQUEST.get();
    
    validator.validatePassword(newPass);
    
    String salt = generateSalt();
    a.setSalt(salt);
    a.setPassword(hashPassword(newPass, salt));
    a.setPasswordExpires(Timestamp.valueOf(LocalDateTime.now().plusDays(90)));
    a = authRepo.save(a);
    
    // erase all old tokens
    tokenRepo.deleteAll(tokenRepo.findAllByUser(u)); // assumes at least one token in db, else wouldn't reach here
    
    // create new token
    return generateToken(u);
  }
  
  public void logout(String token) {
    tokenRepo.deleteById(token);
  }
}
