package me.rootatkali.hey.external;

import me.rootatkali.hey.model.User;
import me.rootatkali.hey.model.Verification;
import me.rootatkali.hey.repo.SchoolRepository;
import me.rootatkali.hey.repo.UserRepository;
import me.rootatkali.hey.repo.VerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class VerificationService {
  private final VerificationRepository verificationRepo;
  private final UserRepository userRepo;
  private final SchoolRepository schoolRepo;
  private final MashovService mashovService;
  
  @Autowired
  public VerificationService(VerificationRepository verificationRepo,
                             UserRepository userRepo,
                             SchoolRepository schoolRepo,
                             MashovService mashovService) {
    this.verificationRepo = verificationRepo;
    this.userRepo = userRepo;
    this.schoolRepo = schoolRepo;
    this.mashovService = mashovService;
  }
  
  public boolean isVerified(User user) {
    // TODO set up a more robust algorithm
    return verificationRepo.existsByUser(user);
  }
  
  public User verifyMashov(User user, int semel, int year, String username, String password) {
    var details = mashovService.fetchDetails(semel, year, username, password);
    
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
    
    // TODO school
    
    verificationRepo.saveAll(List.of(gender, grade));
    return userRepo.save(user);
  }
}
