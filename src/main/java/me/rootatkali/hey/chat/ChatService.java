package me.rootatkali.hey.chat;

import me.rootatkali.hey.repo.ChatRepository;
import me.rootatkali.hey.util.Error;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class ChatService {
  private final ChatRepository chatRepo;
  private final DigestUtils hash;
  
  @Autowired
  public ChatService(ChatRepository chatRepo) {
    this.chatRepo = chatRepo;
    this.hash = new DigestUtils("SHA3-256");
  }
  
  public Chat registerChat(String user1, String user2) {
    if (user1.equals(user2)) throw Error.BAD_REQUEST.get();
    
    Chat chat = new Chat(user1, user2);
    
    return chatRepo.save(chat);
  }
  
  public Chat getChat(String user1, String user2) {
    if (user1.equals(user2)) throw Error.BAD_REQUEST.get();
    
    String[] ids = {user1, user2};
    Arrays.sort(ids);
    String expectedId = hash.digestAsHex(ids[0] + ids[1]);
    
    return chatRepo
        .findById(expectedId) // If chat exists - return it
        .orElse(registerChat(user1, user2)); // If not - register a new chat
  }
}
