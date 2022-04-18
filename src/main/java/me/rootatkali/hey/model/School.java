package me.rootatkali.hey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ComparisonChain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

@Entity
public class School implements Comparable<School> {
  @Id
  private int id;
  
  private String name;
  private String town;
  
  @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<User> users;
  
  // protected 0ac for jpa
  protected School() {
  
  }
  
  public School(int id, String name, String town) {
    this.id = id;
    this.name = name;
    this.town = town;
  }
  
  public int getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public String getTown() {
    return town;
  }
  
  public List<User> getUsers() {
    return List.copyOf(users);
  }
  
  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .add("town", town)
        .toString();
  }
  
  @Override
  public int compareTo(School o) {
    return ComparisonChain.start().compare(id, o.id).result();
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    School school = (School) o;
    return id == school.id;
  }
  
  @Override
  public int hashCode() {
    return id;
  }
}
