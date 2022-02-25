package me.rootatkali.hey.service;

import com.google.common.base.Strings;
import com.google.common.primitives.Longs;
import me.rootatkali.hey.model.Auth;
import me.rootatkali.hey.model.Token;
import me.rootatkali.hey.model.User;
import me.rootatkali.hey.model.UserRegistration;
import me.rootatkali.hey.repo.AuthRepository;
import me.rootatkali.hey.repo.TokenRepository;
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
import java.util.List;
import java.util.regex.Pattern;

@Service
public class AuthService {
  private final AuthRepository authRepo;
  private final UserRepository userRepo;
  private final TokenRepository tokenRepo;
  private final SecureRandom random;
  
  private static final String SALT_CHARS = "01234589" +
      "abcdefghijklmnopqrstuvwxyz" +
      "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
      ".!@#$%^&*()_+-=~`[]{}\\|/?<>'\";:";
  
  private static final int SCL = SALT_CHARS.length();
  
  @Autowired
  public AuthService(AuthRepository authRepo,
                     UserRepository userRepo,
                     TokenRepository tokenRepo) {
    this.authRepo = authRepo;
    this.userRepo = userRepo;
    this.tokenRepo = tokenRepo;
    this.random = new SecureRandom();
  }
  
  private void validateUsername(String username) {
    // check length >= 3
    if (username == null || username.length() < 3) throw Error.BAD_REQUEST.get();
    // check alphanum
    if (!username.matches("^[a-zA-Z][a-zA-Z0-9]+$")) throw Error.BAD_REQUEST.get();
    // check not already exists
    if (userRepo.existsByUsername(username)) throw Error.ALREADY_EXISTS.get();
  }
  
  private void validateName(String name) {
    final String approvedSpecialChars = " '-";
    // check for empty name
    if (Strings.isNullOrEmpty(name)) throw Error.BAD_REQUEST.get();
    // trim and check for printability
    name = name.strip();
    for (char c : name.toCharArray()) {
      if (!(Character.isLetter(c) || approvedSpecialChars.contains("" + c))) throw Error.BAD_REQUEST.get();
    }
  }
  
  private void validateEmail(String email) {
    if (!Pattern.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", email))
      throw Error.BAD_REQUEST.get();
  }
  
  // validate Israeli mobile phones
  @SuppressWarnings("UnstableApiUsage")
  private void validatePhoneNumber(String number) {
    // check if 10-digit number
    if (number == null || number.length() != 10) throw Error.BAD_REQUEST.get();
    if (Longs.tryParse(number) == null) throw Error.BAD_REQUEST.get();
    
    // check for mobile carrier prefix
    if (!number.startsWith("05")) throw Error.BAD_REQUEST.get();
    if (!List.of('0', '1', '2', '3', '4', '5', '8').contains(number.charAt(2))) throw Error.BAD_REQUEST.get();
  }
  
  private void validatePassword(String password) {
    ResponseStatusException invalidPassword = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password.");
    
    // 8-32 chars
    if (password == null || password.length() < 8 || password.length() > 32) throw invalidPassword;
    
    // contains upper
    if (!password.matches(".*[A-Z].*")) throw invalidPassword;
    
    // contains lower
    if (!password.matches(".*[a-z].*")) throw invalidPassword;
    
    // contains digit
    if (!password.matches(".*\\d.*")) throw invalidPassword;
    
    // contains special
    if (!password.matches(".*[,. !@#$%^&*()_=?/\\[\\]{}].*")) throw invalidPassword;
  }
  
  private void validateRegistration(UserRegistration reg) {
    // todo validate fields
    validateUsername(reg.getUsername());
    validateName(reg.getFirstName());
    validateName(reg.getLastName());
    validateEmail(reg.getEmail());
    validatePhoneNumber(reg.getPhoneNum());
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
    u = userRepo.save(u);
    
    // create auth entry
    Auth a = new Auth();
    a.setUser(u);
    String salt = generateSalt();
    a.setSalt(salt);
    a.setPassword(hashPassword(reg.getPassword(), salt));
    a.setPasswordExpires(Timestamp.valueOf(LocalDateTime.now().plusDays(90)));
    a = authRepo.save(a);
    
    // create token
    Token t = new Token();
    t.setUser(u);
    t.setExpires(Timestamp.valueOf(LocalDateTime.now().plusMinutes(90)));
    return tokenRepo.save(t);
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
    Token t = new Token();
    t.setUser(u);
    t.setExpires(Timestamp.valueOf(LocalDateTime.now().plusMinutes(90)));
    return tokenRepo.save(t);
  }
  
  public User validateAccessToken(String token) {
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
    
    validatePassword(newPass);
  
    String salt = generateSalt();
    a.setSalt(salt);
    a.setPassword(hashPassword(newPass, salt));
    a.setPasswordExpires(Timestamp.valueOf(LocalDateTime.now().plusDays(90)));
    a = authRepo.save(a);
    
    // erase all old tokens
    tokenRepo.deleteAll(tokenRepo.findAllByUser(u));
  
    // create new token
    Token t = new Token();
    t.setUser(u);
    t.setExpires(Timestamp.valueOf(LocalDateTime.now().plusMinutes(90)));
    return tokenRepo.save(t);
  }
  
  public void logout(String token) {
    tokenRepo.deleteById(token);
  }
}
