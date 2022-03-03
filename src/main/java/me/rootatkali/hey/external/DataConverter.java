package me.rootatkali.hey.external;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.springframework.stereotype.Component;

@Component // for autowiring support
public class DataConverter {
  private final BiMap<String, Integer> grade;
  private final BiMap<Integer, String> edarg; // inverse of grade
  
  private final BiMap<String, String> gender;
  private final BiMap<String, String> redneg; // inverse of gender
  
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
    gender.put("ז", "M");
    gender.put("נ", "F");
  }
  
  public int grade(String grade) {
    return this.grade.get(grade);
  }
  
  public String grade(int grade) {
    return edarg.get(grade);
  }
  
  public String genderEn(String genderHe) {
    return gender.get(genderHe);
  }
  
  public String genderHe(String genderEn) {
    return redneg.get(genderEn);
  }
}
