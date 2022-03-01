package me.rootatkali.hey.external;

import com.google.gson.Gson;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import me.rootatkali.hey.util.UnimplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MashovService {
  private final RestTemplate restTemplate;
  private final Gson gson;
  private static final String MASHOV_URL = "https://web.mashov.info/api";
  
  @Autowired
  public MashovService(RestTemplateBuilder builder) {
    this.restTemplate = builder.build();
    this.gson = new Gson();
  }
  
  private Map<String, Object> loginMap(int semel, int year, String username, String password) {
    Map<String, Object> map = new HashMap<>();
    map.put("apiVersion", "3.20210425");
    map.put("apiBuild", 3.20210425);
    map.put("appName", "info.mashov.students");
    map.put("appVersion", 3.20210425);
    map.put("deviceManufacturer", "win");
    map.put("deviceModel", "desktop");
    map.put("devicePlatform", "chrome");
    map.put("deviceUuid", "chrome");
    map.put("deviceVersion", "99.0");
    map.put("IsBiometric", false);
    map.put("username", username);
    map.put("password", password);
    map.put("semel", semel);
    map.put("year", year);
    return map;
  }
  
  /**
   * Connects to Mashov and returns
   * @param semel The school ID
   * @param year The login year
   * @param username The user's login username
   * @param password The user's password
   * @return An {@link ExternalDetails} with details about the user
   */
  public ExternalDetails getMashovDetails(int semel, int year, String username, String password) {
    var map = loginMap(semel, year, username, password);
  
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
  
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
    
    ResponseEntity<String> res = restTemplate.postForEntity(MASHOV_URL + "/login", entity, String.class);
    
    if (res.getStatusCode() != HttpStatus.OK) throw new ResponseStatusException(res.getStatusCode(), res.getBody());
    
    ReadContext ctx = JsonPath.parse(res.getBody());
    
    var ret = new ExternalDetails(
        ctx.read("$.credential.userId"),
        ctx.read("$.credential.idNumber", String.class),
        -1,
        null,
        null
    ); // todo finish
    
    throw new UnimplementedException();
  }
}
