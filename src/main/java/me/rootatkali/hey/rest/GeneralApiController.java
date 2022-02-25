package me.rootatkali.hey.rest;

import me.rootatkali.hey.HeyApplication;
import me.rootatkali.hey.model.Login;
import me.rootatkali.hey.model.Token;
import me.rootatkali.hey.model.User;
import me.rootatkali.hey.model.UserRegistration;
import me.rootatkali.hey.service.AuthService;
import me.rootatkali.hey.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@RestController
@RequestMapping("/api")
public class GeneralApiController {
  private final UserService userService;
  private final AuthService authService;
  
  @Autowired
  public GeneralApiController(UserService userService, AuthService authService) {
    this.userService = userService;
    this.authService = authService;
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
    Cookie tokenCookie = new Cookie("token", t.getId());
    tokenCookie.setHttpOnly(true);
    if (!HeyApplication.DEBUG) tokenCookie.setSecure(true);
    tokenCookie.setMaxAge((int) LocalDateTime.now().until(t.getExpires().toLocalDateTime(), ChronoUnit.SECONDS));
    res.addCookie(tokenCookie);
    
    return t.getUser();
  }
  
  @PostMapping(path = "/login", consumes = "application/json")
  public User login(@RequestBody Login login, HttpServletResponse res) {
    Token t = authService.login(login.username(), login.password());
    
    // set cookie
    Cookie tokenCookie = new Cookie("token", t.getId());
    tokenCookie.setHttpOnly(true);
    if (!HeyApplication.DEBUG) tokenCookie.setSecure(true);
    tokenCookie.setMaxAge((int) LocalDateTime.now().until(t.getExpires().toLocalDateTime(), ChronoUnit.SECONDS));
    res.addCookie(tokenCookie);
  
    return t.getUser();
  }
  
  @GetMapping("/me")
  public User getMe(@CookieValue(name = "token", required = false) String token) {
    return authService.validateAccessToken(token);
  }
  
  @PostMapping("/users/{user}/changePassword")
  public User resetPassword(@PathVariable String user,
                            @RequestBody String oldPassword,
                            @RequestBody String newPassword,
                            HttpServletResponse res) {
    Token t = authService.resetPassword(user, oldPassword, newPassword);
  
    // set cookie
    Cookie tokenCookie = new Cookie("token", t.getId());
    tokenCookie.setHttpOnly(true);
    if (!HeyApplication.DEBUG) tokenCookie.setSecure(true);
    tokenCookie.setMaxAge((int) LocalDateTime.now().until(t.getExpires().toLocalDateTime(), ChronoUnit.SECONDS));
    res.addCookie(tokenCookie);
    
    return t.getUser();
  }
  
  @PostMapping("/logout")
  public int logout(@CookieValue(name = "token", required = false) String token, HttpServletResponse res) {
    authService.validateAccessToken(token);
    authService.logout(token);
    Cookie t = new Cookie("token", "");
    t.setMaxAge(0);
    res.addCookie(t);
    return HttpStatus.OK.value();
  }
}
