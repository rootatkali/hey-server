package me.rootatkali.hey.rest;

import lombok.extern.slf4j.Slf4j;
import me.rootatkali.hey.HeyApplication;
import me.rootatkali.hey.model.Login;
import me.rootatkali.hey.model.Token;
import me.rootatkali.hey.model.User;
import me.rootatkali.hey.model.UserRegistration;
import me.rootatkali.hey.service.AuthService;
import me.rootatkali.hey.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api")
@Slf4j
public class GeneralApiController {
  private final UserService userService;
  private final AuthService authService;
  
  @Autowired
  public GeneralApiController(UserService userService, AuthService authService) {
    this.userService = userService;
    this.authService = authService;
  }
  
  private User tokenAndUser(HttpServletResponse res, Token t) {
    Cookie tokenCookie = new Cookie("token", t.getId());
    tokenCookie.setHttpOnly(true);
    if (!HeyApplication.DEBUG) tokenCookie.setSecure(true);
    tokenCookie.setMaxAge((int) LocalDateTime.now().until(t.getExpires().toLocalDateTime(), ChronoUnit.SECONDS));
    res.addCookie(tokenCookie);
    
    return t.getUser();
  }
  
  @GetMapping("/users")
  public Iterable<User> getUsers() {
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR); // todo permissions
    // return userService.getUsers();
  }
  
  @GetMapping("/users/{id}")
  public User getUser(@PathVariable String id) {
    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR); // todo permissions
    // return userService.getUser(id);
  }
  
  @PostMapping(path = "/users", consumes = "application/json")
  public User registerUser(@RequestBody UserRegistration registration, HttpServletResponse res) {
    Token t = authService.registerUser(registration);
  
    // set cookie
    return tokenAndUser(res, t);
  }
  
  @PostMapping(path = "/login", consumes = "application/json")
  public User login(@RequestBody Login login, HttpServletResponse res) {
    Token t = authService.login(login.username(), login.password());
    
    // set cookie
    return tokenAndUser(res, t);
  }
  
  @GetMapping("/me")
  public User getMe(@CookieValue(name = "token", required = false) String token) {
    return authService.validateAccessToken(token);
  }
  
  @PatchMapping("/me")
  public User editMe(@CookieValue(name = "token", required = false) String token, @RequestBody User edit) {
    User u = getMe(token);
    
    return userService.editUser(u, edit);
  }
  
  @PostMapping("/users/{user}/changePassword")
  public User resetPassword(@PathVariable String user,
                            @RequestBody String oldPassword,
                            @RequestBody String newPassword,
                            HttpServletResponse res) {
    Token t = authService.resetPassword(user, oldPassword, newPassword);
  
    // set cookie
    return tokenAndUser(res, t);
  }
  
  @PostMapping("/logout")
  public ResponseEntity<Void> logout(@CookieValue(name = "token", required = false) String token, HttpServletResponse res) {
    authService.validateAccessToken(token);
    authService.logout(token);
    
    // Delete token cookie
    Cookie t = new Cookie("token", "");
    t.setMaxAge(0);
    res.addCookie(t);
    return ResponseEntity.ok().build();
  }
}
