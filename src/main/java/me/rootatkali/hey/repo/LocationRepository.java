package me.rootatkali.hey.repo;

import me.rootatkali.hey.model.Location;
import org.springframework.data.repository.CrudRepository;

public interface LocationRepository extends CrudRepository<Location, String> {
}