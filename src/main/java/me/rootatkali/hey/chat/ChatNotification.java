package me.rootatkali.hey.chat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record ChatNotification(String id, String senderId, String name) {
  public ChatNotification {
    log.info("notification " + id);
  }
}
