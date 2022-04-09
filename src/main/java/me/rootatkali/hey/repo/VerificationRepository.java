package me.rootatkali.hey.repo;

import me.rootatkali.hey.model.User;
import me.rootatkali.hey.model.Verification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface VerificationRepository extends CrudRepository<Verification, String> {
  @Query("select (count(v) > 0) from Verification v where v.user = ?1")
  boolean existsByUser(User u);
}