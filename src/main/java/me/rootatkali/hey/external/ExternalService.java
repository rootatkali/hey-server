package me.rootatkali.hey.external;

import me.rootatkali.hey.model.School;

import java.util.List;

public interface ExternalService {
  /**
   * Returns a list of all schools supported by this External Service Provider (ESP).
   */
  List<School> fetchSchools();
  
  /**
   * Fetches details about the user specified.
   */
  ExternalDetails fetchDetails(int semel, int year, String username, String password);
}
