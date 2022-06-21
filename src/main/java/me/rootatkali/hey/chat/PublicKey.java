package me.rootatkali.hey.chat;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PublicKey {
  @Id
  private String id;
  private String jwk;
  
  public PublicKey() {
  }
  
  public PublicKey(String id, String jwk) {
    this.id = id;
    this.jwk = jwk;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getJwk() {
    return jwk;
  }
  
  public void setJwk(String jwk) {
    this.jwk = jwk;
  }
}
