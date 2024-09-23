package com.rayandajani.APIconnect.APIconnect;
import com.google.common.util.concurrent.RateLimiter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController  // Make sure to use @RestController for API endpoints
@CrossOrigin
@RequestMapping("/api/twitter")  // Namespace all endpoints under /api/twitter
public class TwitterController {

    private final String BASE_URL = "https://twitter135.p.rapidapi.com";
    private final String RAPIDAPI_HOST = "twitter135.p.rapidapi.com";
    private final String RAPIDAPI_KEY = "9e6033fdd7msh276eb3ea40befbdp19038djsn67f476bd4a39";

    private final RateLimiter rateLimiter = RateLimiter.create(2.0); // 5 permits per second


    private final RestTemplate restTemplate;


    @Autowired
    public TwitterController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String parseUserId(ResponseEntity<String> response){
        //Parse the response body to extract the "id"
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode idNode = root.path("data").path("user").path("result").path("rest_id");
            return idNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
    public String parseUserFollowerCount(ResponseEntity<String> response){
        //Parse the response body to extract the "id"
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode idNode = root.path("data").path("user").path("result").path("legacy").path("followers_count");
            return idNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String parseUserFollowingCount(ResponseEntity<String> response){
        //Parse the response body to extract the "id"
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode idNode = root.path("data").path("user").path("result").path("legacy").path("friends_count");
            return idNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public String parseUserProfileName(ResponseEntity<String> response){
        //Parse the response body to extract the "id"
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode idNode = root.path("data").path("user").path("result").path("legacy").path("name");
            return idNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String parseUserTweetCount(ResponseEntity<String> response){
        //Parse the response body to extract the "id"
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode idNode = root.path("data").path("user").path("result").path("legacy").path("statuses_count");
            return idNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String parseUserBanner(ResponseEntity<String> response){
        //Parse the response body to extract the "id"
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode idNode = root.path("data").path("user").path("result").path("legacy").path("profile_banner_url");
            return idNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String parseUserPicture(ResponseEntity<String> response){
        //Parse the response body to extract the "id"
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode idNode = root.path("data").path("user").path("result").path("legacy").path("profile_image_url_https");
            return idNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }





    public HttpHeaders createHeaders(){

        // Create headers for the API request
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", RAPIDAPI_KEY);
        headers.set("x-rapidapi-host", RAPIDAPI_HOST);
        return headers;
    }


    public String buildURL(String endpoint){

        //build URL using BASE_URL + endpoint
        return BASE_URL + endpoint;
    }



    public ResponseEntity<String> getUserInfo(String userName) {

        rateLimiter.acquire();
        // Build the URL for the API call
        String endpoint = "/v2/UserByScreenName/?username=" + userName;
        String url = buildURL(endpoint);

        // Create an HttpEntity with the headers
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());

        // Make the GET request
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    @GetMapping("/userId")
    public String getUserId(@RequestParam String userName){
        return parseUserId(getUserInfo(userName));
    }

    @GetMapping("/userFollowerCount")
    public String getUserFollowerCount(@RequestParam String userName){
        return parseUserFollowerCount(getUserInfo(userName));
    }

    @GetMapping("/userFollowingCount")
    public String getUserFollowingCount(@RequestParam String userName){
        return parseUserFollowingCount(getUserInfo(userName));
    }

    @GetMapping("/userProfileName")
    public String getUserProfileName(@RequestParam String userName){
        return parseUserProfileName(getUserInfo(userName));
    }

    @GetMapping("/userTweetCount")
    public String getUserTweetCount(@RequestParam String userName){
        return parseUserTweetCount(getUserInfo(userName));
    }

    @GetMapping("/userBanner")
    public String getUserBanner(@RequestParam String userName){
        return parseUserBanner(getUserInfo(userName));
    }

    @GetMapping("/userProfilePicture")
    public String getUserProfilePicture(@RequestParam String userName){
        return parseUserPicture(getUserInfo(userName));
    }

}
