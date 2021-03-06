package me.rootatkali.hey.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Permission {
  @Id
  @GeneratedValue
  private long id;
  private String name;
  
  public Permission() {
  
  }
  
  public long getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
}
