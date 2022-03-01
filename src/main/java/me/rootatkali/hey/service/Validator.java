package me.rootatkali.hey.service;

import com.google.common.base.Strings;
import com.google.common.primitives.Longs;
import me.rootatkali.hey.repo.UserRepository;
import me.rootatkali.hey.util.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class Validator {
  private final UserRepository userRepo;
  
  private static final int BIO_MAX_LENGTH = 5000;
  
  @Autowired
  public Validator(UserRepository userRepo) {
    this.userRepo = userRepo;
  }
  
  public void validateUsername(String username) {
    var invalidUsername = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid username.");
    
    // check length >= 3
    if (username == null || username.length() < 3) throw invalidUsername;
    // check alphanum
    if (!username.matches("^[a-zA-Z][a-zA-Z0-9]+$")) throw invalidUsername;
    // check not already exists
    if (userRepo.existsByUsername(username)) throw Error.ALREADY_EXISTS.get();
  }
  
  public void validateName(String name) {
    var invalidName = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name: " + name + ".");
    
    final String approvedSpecialChars = " '-";
    // check for empty name
    if (Strings.isNullOrEmpty(name)) throw invalidName;
    // trim and check for printability
    name = name.strip();
    for (char c : name.toCharArray()) {
      if (!(Character.isLetter(c) || approvedSpecialChars.contains("" + c))) throw invalidName;
    }
  }
  
  public void validateEmail(String email) {
    // email regex from internet
    if (!Pattern.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", email))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address.");
  }
  
  /**
   * validate Israeli mobile phones
   */
  @SuppressWarnings("UnstableApiUsage")
  public void validatePhoneNumber(String number) {
    var invalidPhone = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number.");
    // check if 10-digit number
    if (number == null || number.length() != 10) throw invalidPhone;
    if (Longs.tryParse(number) == null) throw invalidPhone;
    
    // check for mobile carrier prefix
    if (!number.startsWith("05")) throw invalidPhone;
    if (!List.of('0', '1', '2', '3', '4', '5', '8').contains(number.charAt(2))) throw invalidPhone;
  }
  
  public void validatePassword(String password) {
    var invalidPassword = new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password.");
    
    // 8-64 chars
    if (password == null || password.length() < 8 || password.length() > 64) throw invalidPassword;
    
    int checks = 0;
    // a valid password must contain at least 3 of the following 4 tests, in addition to length (compulsory):
    
    // contains upper
    if (password.matches(".*[A-Z].*")) checks++;
    
    // contains lower
    if (password.matches(".*[a-z].*")) checks++;
    
    // contains digit
    if (password.matches(".*\\d.*")) checks++;
    
    // contains special, which is any of the following:
    // -,. !@#$%^&*()_+=/[]{}\
    if (password.matches(".*[-,. !@#$%^&*()_+=?/\\[\\]{}\\\\].*")) checks++;
    
    if (checks < 3) throw invalidPassword;
  }
  
  public void validateGrade(int grade) {
    // includes support for grades 13 and 14
    if (grade < 7 || grade > 14) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid grade.");
  }
  
  public void validateGender(char gender) {
    if (!"MFOX".contains(Character.toString(gender).toUpperCase()))
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid gender.");
  }
  
  public void validateBio(String bio) {
    if (bio.length() > BIO_MAX_LENGTH) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bio too long.");
  }
}
