package me.rootatkali.hey.repo;

import me.rootatkali.hey.model.School;
import org.springframework.data.repository.CrudRepository;

public interface SchoolRepository extends CrudRepository<School, Integer> {
}
