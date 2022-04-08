package me.rootatkali.hey.repo;

import me.rootatkali.hey.model.Verification;
import org.springframework.data.repository.CrudRepository;

public interface VerificationRepository extends CrudRepository<Verification, String> {
}