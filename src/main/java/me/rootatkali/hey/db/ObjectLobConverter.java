package me.rootatkali.hey.db;

import com.google.gson.Gson;

import javax.persistence.Converter;
import javax.persistence.AttributeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Converter
public class ObjectLobConverter implements AttributeConverter<Object, byte[]> {
  private final Gson gson = new Gson();
  
  // Generate the byte array for the object
  private byte[] genBytes(Object object) {
    if (object instanceof byte[] b) return b;
    if (object instanceof String s) return s.getBytes(StandardCharsets.UTF_8);
  
    return gson.toJson(object).getBytes(StandardCharsets.UTF_8);
  }
  
  @Override
  public byte[] convertToDatabaseColumn(Object object) {
    // Generates a byte array representing the object, and pads it with a code representing the original type
    byte[] arr = genBytes(object);
    
    byte code;
    if (object instanceof byte[]) code = 0x00;
    else if (object instanceof String) code = 0x01;
    else code = (byte) 0xFF; // general object
    
    byte[] ret = new byte[arr.length + 1];
    ret[0] = code;
    System.arraycopy(arr, 0, ret, 1, arr.length);
    
    return ret;
  }
  
  @Override
  public Object convertToEntityAttribute(byte[] arr) {
    byte code = arr[0];
    byte[] raw = Arrays.copyOfRange(arr, 1, arr.length);
    
    return switch (code) {
      case 0x00 -> raw; // byte[]
      case 0x01 -> new String(raw, StandardCharsets.UTF_8); // String
      default -> new String(raw, StandardCharsets.UTF_8); // Json - Resulting method should handle decoding
    };
  }
}
