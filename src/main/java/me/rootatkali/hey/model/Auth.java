package me.rootatkali.hey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Auth {
  @Id
  private String id;
  private String salt;
  /** sha256 hash of password - verification at service level */
  private String password;
  private Timestamp passwordExpires;
  
  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  @JsonIgnore
  private User user;
  
  public Auth() {
  
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getSalt() {
    return salt;
  }
  
  public void setSalt(String salt) {
    this.salt = salt;
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String hash) {
    this.password = hash;
  }
  
  public Timestamp getPasswordExpires() {
    return passwordExpires;
  }
  
  public void setPasswordExpires(Timestamp passwordExpires) {
    this.passwordExpires = passwordExpires;
  }
  
  public User getUser() {
    return user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
}
