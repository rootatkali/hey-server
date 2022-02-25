package me.rootatkali.hey.db;

import com.google.gson.Gson;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringArrStringConverter implements AttributeConverter<String[], String> {
  private final Gson gson = new Gson();
  
  @Override
  public String convertToDatabaseColumn(String[] attribute) {
    return gson.toJson(attribute);
  }
  
  @Override
  public String[] convertToEntityAttribute(String dbData) {
    return gson.fromJson(dbData, String[].class);
  }
}
