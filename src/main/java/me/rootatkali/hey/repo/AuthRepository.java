package me.rootatkali.hey.repo;

import me.rootatkali.hey.model.Auth;
import org.springframework.data.repository.CrudRepository;

public interface AuthRepository extends CrudRepository<Auth, String> {
}