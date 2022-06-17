package me.rootatkali.hey.repo;

import me.rootatkali.hey.chat.Message;
import me.rootatkali.hey.chat.MessageStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message, String> {
  @Query("select m from Message m where m.chat = ?1")
  Iterable<Message> findAllByChat(String chat);
  
  @Modifying
  @Query("update Message m set m.status = ?4 where m.chat = ?1 and m.sender = ?2 and m.recipient = ?3")
  int updateStatusByChatAndSenderAndRecipient(String chat,
                                                            String sender,
                                                            String recipient,
                                                            MessageStatus status);
}