package me.rootatkali.hey.db;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;

@Converter
public class IntArrStringConverter implements AttributeConverter<int[], String> {
  @Override
  public String convertToDatabaseColumn(int[] attribute) {
    return Arrays.toString(attribute);
  }
  
  @Override
  public int[] convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.equals("null") || dbData.isBlank()) return null;
    if (dbData.charAt(0) != '[' || dbData.charAt(dbData.length() - 1) != ']') throw new IllegalArgumentException();
    
    dbData = dbData.substring(1, dbData.length() - 1); // remove whitespaces
    String[] nums = dbData.split(",");
    int[] arr = new int[nums.length];
    
    for (int i = 0; i < nums.length; i++) {
      String n = nums[i].trim();
      arr[i] = Integer.parseInt(n);
    }
    
    return arr;
  }
}
