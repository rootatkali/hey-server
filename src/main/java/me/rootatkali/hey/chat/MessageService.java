package me.rootatkali.hey.chat;

import me.rootatkali.hey.repo.MessageRepository;
import me.rootatkali.hey.util.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {
  private final MessageRepository messageRepo;
  
  @Autowired
  public MessageService(MessageRepository messageRepo) {
    this.messageRepo = messageRepo;
  }
  
  public Message saveMessage(Message message) {
    message.setStatus(MessageStatus.SENT);
    message.setId(UUID.randomUUID().toString());
    return messageRepo.save(message);
  }
  
  public List<Message> findChatHistory(Chat chat) {
    Iterable<Message> messages = messageRepo.findAllByChat(chat.getId());
    
    List<Message> list = new ArrayList<>();
    messages.forEach(list::add);
    Collections.sort(list);
    
    return list;
  }
  
  public void updateStatus(Chat chat, String sender, String recipient, MessageStatus status) {
    messageRepo.updateStatusByChatAndSenderAndRecipient(chat.getId(), sender, recipient, status);
  }
  
  public Message findMessage(String id) {
    return messageRepo.findById(id)
        .map(m -> {
          m.setStatus(MessageStatus.DELIVERED);
          return messageRepo.save(m);
        })
        .orElseThrow(Error.NOT_FOUND);
  }
}
