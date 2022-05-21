package me.rootatkali.hey.chat;

import me.rootatkali.hey.model.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;

@Entity
public class Chat {
  @Id
  private String id;
  private String user1;
  private String user2;
  
  public Chat() {
  }
  
  public Chat(User user1, User user2) {
    this(user1.getId(), user2.getId());
  }
  
  public Chat(String user1, String user2) {
    String[] ids = {user1, user2};
    Arrays.sort(ids);
    this.user1 = ids[0];
    this.user2 = ids[1];
    this.id = new DigestUtils("SHA3-256").digestAsHex(ids[0] + ids[1]);
  }
  
  public String getId() {
    return id;
  }
  
  public String getUser1() {
    return user1;
  }
  
  public String getUser2() {
    return user2;
  }
}
