package me.rootatkali.hey.repo;

import me.rootatkali.hey.model.Token;
import me.rootatkali.hey.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<Token, String> {
  @Query("select t from Token t where t.user = ?1")
  Iterable<Token> findAllByUser(User u);
}