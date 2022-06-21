package me.rootatkali.hey.repo;

import me.rootatkali.hey.chat.PublicKey;
import org.springframework.data.repository.CrudRepository;

public interface PublicKeyRepository extends CrudRepository<PublicKey, String> {
}