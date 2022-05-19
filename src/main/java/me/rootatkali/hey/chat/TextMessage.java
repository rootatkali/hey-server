package me.rootatkali.hey.chat;

public class TextMessage extends Message<String> {
  public TextMessage(String chat, String from, String to, String body) {
    super(MessageType.TEXT, chat, from, to, body);
  }
}
