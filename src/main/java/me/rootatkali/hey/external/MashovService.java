package me.rootatkali.hey.external;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;
import lombok.extern.slf4j.Slf4j;
import me.rootatkali.hey.model.School;
import me.rootatkali.hey.util.UnimplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MashovService {
  private final RestTemplate restTemplate;
  private final Gson gson;
  private final DataConverter dataConverter;
  
  private static final String MASHOV_URL = "https://web.mashov.info/api";
  private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36";
  
  @Autowired
  public MashovService(RestTemplateBuilder builder,
                       DataConverter dataConverter) {
    this.dataConverter = dataConverter;
    this.restTemplate = builder.build();
    this.gson = new Gson();
  }
  
  private Map<String, Object> loginMap(int semel, int year, String username, String password) {
    Map<String, Object> map = new HashMap<>();
    map.put("apiVersion", "3.20210425");
    map.put("apiBuild", 3.20210425);
    map.put("appBuild", 3.20210425);
    map.put("appName", "info.mashov.students");
    map.put("appVersion", 3.20210425);
    map.put("deviceManufacturer", "win");
    map.put("deviceModel", "desktop");
    map.put("devicePlatform", "chrome");
    map.put("deviceUuid", "chrome");
    map.put("deviceVersion", "99.0.4844.51");
    map.put("IsBiometric", false);
    map.put("username", username);
    map.put("password", password);
    map.put("semel", semel);
    map.put("year", year);
    return map;
  }
  
  public List<School> getSchools() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
    
    HttpEntity<Void> entity = new HttpEntity<>(headers);
    
    ResponseEntity<String> response = restTemplate.exchange(MASHOV_URL + "/schools", HttpMethod.GET, entity, String.class);
    
    TypeToken<List<Map<String, Object>>> typeToken = new TypeToken<>() {
    };
    
    List<Map<String, Object>> map = gson.fromJson(response.getBody(), typeToken.getType());
    
    List<School> ret = new ArrayList<>();
    
    if (map != null)
      map.forEach(m -> ret.add(new School(((Double) m.get("semel")).intValue(), (String) m.get("name"), null)));
    
    return ret;
  }
  
  /**
   * Connects to Mashov and returns
   *
   * @param semel    The school ID
   * @param year     The login year
   * @param username The user's login username
   * @param password The user's password
   * @return An {@link ExternalDetails} with details about the user
   */
  public ExternalDetails getMashovDetails(int semel, int year, String username, String password) {
    var map = loginMap(semel, year, username, password);
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.set(HttpHeaders.USER_AGENT, USER_AGENT);
    
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);
    
    ResponseEntity<String> res = restTemplate.postForEntity(MASHOV_URL + "/login", entity, String.class);
    
    if (res.getStatusCode() != HttpStatus.OK) throw new ResponseStatusException(res.getStatusCode(), res.getBody());
    
    ReadContext ctx = JsonPath.parse(res.getBody());
    
    String displayName = ctx.read("$.credential.displayName");
    
    return new ExternalDetails(
        ctx.read("$.credential.userId"),
        ctx.read("$.credential.idNumber", String.class),
        dataConverter.grade(ctx.read("$.accessToken.children[0].classCode")),
        dataConverter.genderEn(ctx.read("$.accessToken.gender")),
        displayName.substring(0, displayName.lastIndexOf(' '))
    );
  }
}
