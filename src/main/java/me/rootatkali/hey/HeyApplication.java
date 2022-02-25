package me.rootatkali.hey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HeyApplication {
  public static final boolean DEBUG = true;
  
  public static void main(String[] args) {
    SpringApplication.run(HeyApplication.class, args);
  }
  
}
