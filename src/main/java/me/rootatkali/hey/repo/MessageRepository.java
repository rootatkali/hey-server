package me.rootatkali.hey.repo;

import me.rootatkali.hey.chat.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepository extends CrudRepository<Message<?>, String> {
}