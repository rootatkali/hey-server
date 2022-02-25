package me.rootatkali.hey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
public class Interest {
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
}
