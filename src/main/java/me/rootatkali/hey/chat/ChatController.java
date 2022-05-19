package me.rootatkali.hey.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
  private final SimpMessagingTemplate messagingTemplate;
  private final MessageService messageService; // TODO Implement
  private final ChatService chatService; // TODO Implement
  
  @Autowired
  public ChatController(SimpMessagingTemplate messagingTemplate,
                        MessageService messageService,
                        ChatService chatService) {
    this.messagingTemplate = messagingTemplate;
    this.messageService = messageService;
    this.chatService = chatService;
  }
  
  public void processMessage(@Payload Message<?> message) {
    
    Message<?> saved = messageService.saveMessage(message);
  }
}
