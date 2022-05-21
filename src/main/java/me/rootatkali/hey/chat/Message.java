package me.rootatkali.hey.chat;

import me.rootatkali.hey.db.ObjectLobConverter;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public abstract class Message<T> {
  @Id
  protected String id;
  protected String chat;
  protected MessageType type;
  protected MessageStatus status;
  @Convert(converter = ObjectLobConverter.class)
  protected T body;
  // TODO Timestamp
  private String from; // User?
  private String to; // User?
  
  protected Message() {
  
  }
  
  protected Message(MessageType type, String chat, String from, String to, T body) {
    // gen id
    // set timestamp
    this.type = type;
    this.chat = chat;
    this.from = from;
    this.to = to;
    this.body = body;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getChat() {
    return chat;
  }
  
  public void setChat(String chat) {
    this.chat = chat;
  }
  
  public MessageType getType() {
    return type;
  }
  
  public void setType(MessageType type) {
    this.type = type;
  }
  
  public MessageStatus getStatus() {
    return status;
  }
  
  public void setStatus(MessageStatus status) {
    this.status = status;
  }
  
  public T getBody() {
    return body;
  }
  
  public void setBody(T body) {
    this.body = body;
  }
  
  public String getFrom() {
    return from;
  }
  
  public void setFrom(String from) {
    this.from = from;
  }
  
  public String getTo() {
    return to;
  }
  
  public void setTo(String to) {
    this.to = to;
  }
}
