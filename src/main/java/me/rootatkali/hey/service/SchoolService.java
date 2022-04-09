package me.rootatkali.hey.service;

import me.rootatkali.hey.model.School;
import me.rootatkali.hey.repo.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SchoolService {
  private final SchoolRepository schoolRepo;
  
  @Autowired
  public SchoolService(SchoolRepository schoolRepo) {
    this.schoolRepo = schoolRepo;
  }
  
  public Iterable<School> getSchools() {
    Iterable<School> schools = schoolRepo.findAll();
    
    List<School> ret = new ArrayList<>();
    schools.forEach(ret::add);
    Collections.sort(ret);
    return ret;
  }
}
