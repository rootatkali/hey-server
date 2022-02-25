package me.rootatkali.hey.repo;

import me.rootatkali.hey.model.Interest;
import org.springframework.data.repository.CrudRepository;

public interface InterestRepository extends CrudRepository<Interest, String> {
}