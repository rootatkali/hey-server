package me.rootatkali.hey.chat;

import me.rootatkali.hey.util.UnimplementedException;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
  public <T> Message<T> saveMessage(Message<T> message) {
    throw new UnimplementedException();
    // TODO Implement
  }
}
