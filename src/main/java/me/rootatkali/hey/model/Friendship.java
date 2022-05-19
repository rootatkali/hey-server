package me.rootatkali.hey.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Friendship {
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
  private String id;
  @ManyToOne
  @JoinColumn(name = "invitor")
  private User invitor;
  @ManyToOne
  @JoinColumn(name = "invitee")
  private User invitee;
  private Status status;
  
  public Friendship() {
  
  }
  
  public String getId() {
    return id;
  }
  
  public User getInvitor() {
    return invitor;
  }
  
  public void setInvitor(User invitor) {
    this.invitor = invitor;
  }
  
  public User getInvitee() {
    return invitee;
  }
  
  public void setInvitee(User invitee) {
    this.invitee = invitee;
  }
  
  public Status getStatus() {
    return status;
  }
  
  public void setStatus(Status status) {
    this.status = status;
  }
  
  public enum Status {
    PENDING, FRIEND, REJECTED
  }
}
