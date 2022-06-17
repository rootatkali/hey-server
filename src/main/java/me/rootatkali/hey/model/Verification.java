package me.rootatkali.hey.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
public class Verification {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;
  private Type type;
  private String field;
  @Lob
  private String data;
  private Timestamp timestamp;
  
  @ManyToOne
  @JoinColumn(name = "user")
  @JsonIgnore
  private User user;
  
  public Verification(Type type, String field, String data, Timestamp timestamp, User user) {
    this.type = type;
    this.field = field;
    this.data = data;
    this.timestamp = timestamp;
    this.user = user;
  }
  
  protected Verification() {
  
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public Type getType() {
    return type;
  }
  
  public void setType(Type type) {
    this.type = type;
  }
  
  public String getField() {
    return field;
  }
  
  public void setField(String field) {
    this.field = field;
  }
  
  public String getData() {
    return data;
  }
  
  public void setData(String data) {
    this.data = data;
  }
  
  public Timestamp getTimestamp() {
    return timestamp;
  }
  
  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }
  
  public User getUser() {
    return user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  public enum Type {
    MANUAL, MASHOV, EMAIL
  }
}