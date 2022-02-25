package me.rootatkali.hey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Token {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;
  private Timestamp expires;
  
  @ManyToOne
  @JoinColumn(name = "user")
  @JsonIgnore
  private User user;
  
  public Token() {
  
  }
  
  public String getId() {
    return id;
  }
  
  public Timestamp getExpires() {
    return expires;
  }
  
  public void setExpires(Timestamp expires) {
    this.expires = expires;
  }
  
  public User getUser() {
    return user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
}
