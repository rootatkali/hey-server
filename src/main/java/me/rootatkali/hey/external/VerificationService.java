package me.rootatkali.hey.external;

import me.rootatkali.hey.model.School;
import me.rootatkali.hey.model.User;
import me.rootatkali.hey.model.Verification;
import me.rootatkali.hey.repo.SchoolRepository;
import me.rootatkali.hey.repo.UserRepository;
import me.rootatkali.hey.repo.VerificationRepository;
import me.rootatkali.hey.util.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VerificationService {
  private final VerificationRepository verificationRepo;
  private final UserRepository userRepo;
  private final SchoolRepository schoolRepo;
  private final Map<Verification.Type, ExternalService> services;
  
  @Autowired
  public VerificationService(VerificationRepository verificationRepo,
                             UserRepository userRepo,
                             SchoolRepository schoolRepo,
                             MashovService mashovService) {
    this.verificationRepo = verificationRepo;
    this.userRepo = userRepo;
    this.schoolRepo = schoolRepo;
    
    services = new HashMap<>();
    services.put(Verification.Type.MASHOV, mashovService);
  }
  
  public boolean isVerified(User user) {
    // TODO set up a more robust algorithm
    return verificationRepo.existsByUser(user);
  }
  
  public User verify(Verification.Type type, User user, int semel, int year, String username, String password) {
    ExternalService service = services.get(type); // Get details by
    
    ExternalDetails details = service.fetchDetails(semel, year, username, password);
    
    Verification gender = new Verification(
        Verification.Type.MASHOV,
        "gender",
        details.gender(),
        Timestamp.from(Instant.now()),
        user
    );
    user.setGender(details.gender().charAt(0));
  
    Verification grade = new Verification(
        Verification.Type.MASHOV,
        "grade",
        String.valueOf(details.grade()),
        Timestamp.from(Instant.now()),
        user
    );
    user.setGrade(details.grade());
  
    // TODO more verifications?
  
    School s = schoolRepo.findById(semel).orElseThrow(Error.SERVER_ERROR);
    user.setSchool(s);
  
    verificationRepo.saveAll(List.of(gender, grade));
    return userRepo.save(user);
  }
}
