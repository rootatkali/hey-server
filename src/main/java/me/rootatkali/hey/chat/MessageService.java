package me.rootatkali.hey.chat;

import me.rootatkali.hey.repo.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
  private final MessageRepository messageRepo;
  
  @Autowired
  public MessageService(MessageRepository messageRepo) {
    this.messageRepo = messageRepo;
  }
  
  public <T> Message<T> saveMessage(Message<T> message) {
    message.setStatus(MessageStatus.SENT);
    // TODO Validity checks
    if (message.getType() == MessageType.PUBLIC_KEY) return message; // Do not persist key messages
    
    return messageRepo.save(message);
  }
}
