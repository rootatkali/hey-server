package me.rootatkali.hey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.rootatkali.hey.db.IntArrStringConverter;
import me.rootatkali.hey.db.StringArrStringConverter;

import javax.persistence.*;

@Entity
public class UserPreferences {
  @Id
  private String id;
  
  private boolean displayProfilePicture;
  private boolean displayHometown;
  private String interestedGenders; // get genders by using String#toCharArray
  @Convert(converter = IntArrStringConverter.class)
  private int[] interestedGrades; // int[]
  @Convert(converter = StringArrStringConverter.class)
  private String[] interestedTowns; // String[]
  @Convert(converter = IntArrStringConverter.class)
  private int[] interestedSchools; // int[] fk school
  
  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  @JsonIgnore
  private User user;
  
  public UserPreferences() {
  
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public boolean isDisplayProfilePicture() {
    return displayProfilePicture;
  }
  
  public void setDisplayProfilePicture(boolean displayProfilePicture) {
    this.displayProfilePicture = displayProfilePicture;
  }
  
  public boolean isDisplayHometown() {
    return displayHometown;
  }
  
  public void setDisplayHometown(boolean displayHometown) {
    this.displayHometown = displayHometown;
  }
  
  public char[] getInterestedGenders() {
    return interestedGenders.toCharArray();
  }
  
  public void setInterestedGenders(String interestedGenders) {
    this.interestedGenders = interestedGenders;
  }
  
  public int[] getInterestedGrades() {
    return interestedGrades;
  }
  
  public void setInterestedGrades(int[] interestedGrades) {
    this.interestedGrades = interestedGrades;
  }
  
  public String[] getInterestedTowns() {
    return interestedTowns;
  }
  
  public void setInterestedTowns(String[] interestedTowns) {
    this.interestedTowns = interestedTowns;
  }
  
  public int[] getInterestedSchools() {
    return interestedSchools;
  }
  
  public void setInterestedSchools(int[] interestedSchools) {
    this.interestedSchools = interestedSchools;
  }
  
  public User getUser() {
    return user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
}
