package me.rootatkali.hey.chat;

import me.rootatkali.hey.model.User;
import me.rootatkali.hey.service.AuthService;
import me.rootatkali.hey.service.UserService;
import me.rootatkali.hey.util.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ChatController {
  private final SimpMessagingTemplate messagingTemplate;
  private final MessageService messageService;
  private final ChatService chatService;
  private final UserService userService;
  private final AuthService authService;
  
  
  @Autowired
  public ChatController(SimpMessagingTemplate messagingTemplate,
                        MessageService messageService,
                        ChatService chatService,
                        UserService userService,
                        AuthService authService) {
    this.messagingTemplate = messagingTemplate;
    this.messageService = messageService;
    this.chatService = chatService;
    this.userService = userService;
    this.authService = authService;
  }
  
  @MessageMapping("/chat")
  public void processMessage(@Payload ChatPayload payload) {
    String auth = payload.auth();
    User u = authService.validateAccessToken(auth);
    
    Message message = payload.message();
    if (!message.getSender().equals(u.getId())) throw Error.BAD_REQUEST.get();
    // TODO Verify recipient is friend
    
    Chat chat = chatService.getChat(message.getSender(), message.getRecipient());
    message.setChat(chat.getId());
    
    Message saved = messageService.saveMessage(message);
    
    messagingTemplate.convertAndSendToUser(message.getRecipient(), "/queue/messages/" + u.getId(), new ChatNotification(
        saved.getId(), saved.getSender(), userService.getUser(saved.getRecipient()).getFullName()
    ));
  }
}
