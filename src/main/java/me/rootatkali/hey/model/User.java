package me.rootatkali.hey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
public final class User {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;
  
  private String username;
  private String firstName;
  private String lastName;
  private String phoneNum;
  private String email;
  private String hometown;
  private Date birthdate;
  private Integer grade;
  private Character gender;
  @Lob
  private String bio;
  
  @ManyToOne
  @JoinColumn(name = "school")
  @JsonIgnore
  private School school;
  
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  @JsonIgnore
  private UserPreferences prefs;
  
  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  @JsonIgnore
  private Auth auth;
  
  @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
  @JsonIgnore
  private List<Token> tokens;
  
  @ManyToMany
  @JoinTable(
      name = "user_interest",
      joinColumns = @JoinColumn(name = "user"),
      inverseJoinColumns = @JoinColumn(name = "interest")
  )
  @JsonIgnore
  private List<Interest> interests;
  
  
  public User() {
  
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
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
  
  public String getEmail() {
    return email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getHometown() {
    return hometown;
  }
  
  public void setHometown(String hometown) {
    this.hometown = hometown;
  }
  
  public Date getBirthdate() {
    return birthdate;
  }
  
  public void setBirthdate(Date birthdate) {
    this.birthdate = birthdate;
  }
  
  public Integer getGrade() {
    return grade;
  }
  
  public void setGrade(int grade) {
    this.grade = grade;
  }
  
  public Character getGender() {
    return gender;
  }
  
  public void setGender(char gender) {
    this.gender = gender;
  }
  
  public String getBio() {
    return bio;
  }
  
  public void setBio(String bio) {
    this.bio = bio;
  }
  
  public School getSchool() {
    return school;
  }
  
  public void setSchool(School school) {
    this.school = school;
  }
  
  public UserPreferences getPrefs() {
    return prefs;
  }
  
  public void setPrefs(UserPreferences prefs) {
    this.prefs = prefs;
  }
  
  public List<Interest> getInterests() {
    return List.copyOf(interests);
  }
  
  public void setInterests(List<Interest> interests) {
    this.interests = interests;
  }
  
  public Auth getAuth() {
    return auth;
  }
  
  public List<Token> getTokens() {
    return tokens;
  }
}
