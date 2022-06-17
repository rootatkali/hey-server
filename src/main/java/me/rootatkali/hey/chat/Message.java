package me.rootatkali.hey.chat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.sql.Timestamp;

@Entity
public class Message implements Comparable<Message> {
  @Id
  private String id;
  private String chat;
  private MessageType type;
  private MessageStatus status;
  @Lob
  private byte[] body;
  private String sender; // User?
  private String recipient; // User?
  private Timestamp sent;
  private Timestamp delivered;
  private Timestamp seen;
  
  protected Message() {
  
  }
  
  public Message(MessageType type, String chat, String sender, String recipient, byte[] body) {
    // gen id
    // set timestamp
    this.type = type;
    this.chat = chat;
    this.sender = sender;
    this.recipient = recipient;
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
  
  public byte[] getBody() {
    return body;
  }
  
  public void setBody(byte[] body) {
    this.body = body;
  }
  
  public String getSender() {
    return sender;
  }
  
  public void setSender(String from) {
    this.sender = from;
  }
  
  public String getRecipient() {
    return recipient;
  }
  
  public void setRecipient(String to) {
    this.recipient = to;
  }
  
  public Timestamp getSent() {
    return sent;
  }
  
  public void setSent(Timestamp sent) {
    this.sent = sent;
  }
  
  public Timestamp getDelivered() {
    return delivered;
  }
  
  public void setDelivered(Timestamp delivered) {
    this.delivered = delivered;
  }
  
  public Timestamp getSeen() {
    return seen;
  }
  
  public void setSeen(Timestamp read) {
    this.seen = read;
  }
  
  @Override
  public int compareTo(Message o) {
    return sent.compareTo(o.sent);
  }
}
