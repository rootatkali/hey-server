package me.rootatkali.hey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
public final class Location {
  @Id
  private String id;
  private double lat;
  private double lon;
  
  @OneToOne
  @MapsId
  @JoinColumn(name = "id")
  @JsonIgnore
  private User user;
  
  @SuppressWarnings("ProtectedMemberInFinalClass")
  protected Location() {
  
  }
  
  public Location(double lat, double lon) {
    this.lat = lat;
    this.lon = lon;
  }
  
  public double lat() {
    return lat;
  }
  
  public double lon() {
    return lon;
  }
  
  public User getUser() {
    return user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (Location) obj;
    return Double.doubleToLongBits(this.lat) == Double.doubleToLongBits(that.lat) &&
        Double.doubleToLongBits(this.lon) == Double.doubleToLongBits(that.lon);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(lat, lon);
  }
  
  @Override
  public String toString() {
    return "Location[" +
        "lat=" + lat + ", " +
        "lon=" + lon + ']';
  }
  
}
