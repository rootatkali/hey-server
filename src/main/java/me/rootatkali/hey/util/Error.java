package me.rootatkali.hey.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Supplier;

/**
 * Responsible for providing the program with {@link ResponseStatusException} objects.
 */
// inclusion criteria - appears more than once
public class Error {
  // 400 Bad Request
  public static final Supplier<ResponseStatusException> BAD_REQUEST
      = () -> new ResponseStatusException(HttpStatus.BAD_REQUEST);
  
  // 401 Unauthorized
  public static final Supplier<ResponseStatusException> UNAUTHORIZED
      = () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED);
  
  public static final Supplier<ResponseStatusException> INVALID_LOGIN
      = () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login.");
  
  // 403 Forbidden
  public static final Supplier<ResponseStatusException> FORBIDDEN
      = () -> new ResponseStatusException(HttpStatus.FORBIDDEN);
  
  public static final Supplier<ResponseStatusException> ALREADY_EXISTS
      = () -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Resource already exists.");
}
