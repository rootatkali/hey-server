package me.rootatkali.hey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.ComparisonChain;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Interest implements Comparable<Interest> {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;
  private String name;
  
  @ManyToMany(mappedBy = "interests", cascade = CascadeType.ALL)
  @JsonIgnore
  private List<User> users;
  
  public Interest() {
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public List<User> getUsers() {
    return List.copyOf(users);
  }
  
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Interest interest = (Interest) o;
    return Objects.equals(id, interest.id);
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
  
  @Override
  public int compareTo(Interest o) {
    return ComparisonChain.start().compare(name, o.name).result();
  }
}
