package me.rootatkali.hey.repo;

import me.rootatkali.hey.chat.Chat;
import org.springframework.data.repository.CrudRepository;

public interface ChatRepository extends CrudRepository<Chat, String> {
}