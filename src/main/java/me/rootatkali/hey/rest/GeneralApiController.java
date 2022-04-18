package me.rootatkali.hey.rest;

import lombok.extern.slf4j.Slf4j;
import me.rootatkali.hey.HeyApplication;
import me.rootatkali.hey.external.VerificationService;
import me.rootatkali.hey.model.*;
import me.rootatkali.hey.service.AuthService;
import me.rootatkali.hey.service.FriendService;
import me.rootatkali.hey.service.SchoolService;
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
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class GeneralApiController {
  private final AuthService authService;
  private final SchoolService schoolService;
  private final UserService userService;
  private final VerificationService verificationService;
  private final FriendService friendService;
  
  @Autowired
  public GeneralApiController(UserService userService,
                              AuthService authService,
                              SchoolService schoolService,
                              VerificationService verificationService,
                              FriendService friendService) {
    this.userService = userService;
    this.authService = authService;
    this.schoolService = schoolService;
    this.verificationService = verificationService;
    this.friendService = friendService;
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
  
  @PatchMapping(path = "/me", consumes = "application/json")
  public User editMe(@CookieValue(name = "token", required = false) String token, @RequestBody User edit) {
    User u = getMe(token);
    
    return userService.editUser(u, edit);
  }
  
  @GetMapping("/me/location")
  public Location getLocation(@CookieValue(name = "token", required = false) String token) {
    User u = getMe(token);
    
    return userService.getLocation(u);
  }
  
  @PostMapping(path = "/me/location", consumes = "application/json")
  public LatLon setLocation(@CookieValue(name = "token", required = false) String token,
                              @RequestBody LatLon location) {
    User u = getMe(token);
    
    return userService.setLocation(u, new Location(location.lat(), location.lon()));
  }
  
  @GetMapping("/schools")
  public Iterable<School> getSchools(@CookieValue(name = "token", required = false) String token) {
    authService.validateAccessToken(token); // not dependent on user - no need for getMe
    
    return schoolService.getSchools();
  }
  
  @GetMapping("/verify")
  public boolean isVerified(@CookieValue(name = "token", required = false) String token) {
    User u = getMe(token);
    
    return verificationService.isVerified(u);
  }
  
  @PostMapping(path = "/verify/mashov", consumes = "application/json")
  public User verifyMashov(@CookieValue(name = "token", required = false) String token, @RequestBody MashovLogin login) {
    User u = getMe(token);
    
    return verificationService.verifyMashov(
        u,
        login.semel(),
        login.year(),
        login.username(),
        login.password()
    );
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
  
  @GetMapping("/interests")
  public List<Interest> getInterests(@CookieValue(name = "token", required = false) String token) {
    authService.validateAccessToken(token);
    
    return friendService.getAllInterests();
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
