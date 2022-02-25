package me.rootatkali.hey.model;

import java.sql.Date;

public class UserRegistration {
  private String username;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
  private String phoneNum;
  private Date birthdate;
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getFirstName() {
    return firstName;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public String getPhoneNum() {
    return phoneNum;
  }
  
  public void setPhoneNum(String phoneNum) {
    this.phoneNum = phoneNum;
  }
  
  public Date getBirthdate() {
    return birthdate;
  }
  
  public void setBirthdate(Date birthdate) {
    this.birthdate = birthdate;
  }
}
