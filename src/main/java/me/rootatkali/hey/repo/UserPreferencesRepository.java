package me.rootatkali.hey.repo;

import me.rootatkali.hey.model.UserPreferences;
import org.springframework.data.repository.CrudRepository;

public interface UserPreferencesRepository extends CrudRepository<UserPreferences, String> {
}