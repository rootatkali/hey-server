package me.rootatkali.hey.external;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Service;

@Service
public class DataConverter {
  private final BiMap<String, Integer> grade;
  private final BiMap<Integer, String> edarg;
  private final BiMap<String, String> gender;
  private final BiMap<String, String> redneg;
  
  public DataConverter() {
    this.grade = HashBiMap.create();
    this.edarg = grade.inverse();
    grade.put("ז", 7);
    grade.put("ח", 8);
    grade.put("ט", 9);
    grade.put("י", 10);
    grade.put("יא", 11);
    grade.put("יב", 12);
    grade.put("יג", 13);
    grade.put("יד", 14);
    
    this.gender = HashBiMap.create();
    this.redneg = gender.inverse();
  }
}
