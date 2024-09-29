package com.rayandajani.APIconnect.APIconnect;

import com.google.common.util.concurrent.RateLimiter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    private String RAPIDAPI_KEY ="1502958a4emsh553be02805994b8p1ebf4bjsn85f67a3a5e46";

    private final RateLimiter rateLimiter = RateLimiter.create(1.0); // 1 request per second
    private final RestTemplate restTemplate;

    @Autowired
    public TwitterController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // Helper method to create HTTP headers
    public HttpHeaders createHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-rapidapi-key", RAPIDAPI_KEY);
        headers.set("x-rapidapi-host", RAPIDAPI_HOST);
        return headers;
    }

    // Helper method to build the full URL
    public String buildURL(String endpoint){
        return BASE_URL + endpoint;
    }

    // Fetch user information
    public ResponseEntity<String> getUserInfo(String userName) {
        rateLimiter.acquire();
        String endpoint = "/v2/UserByScreenName/?username=" + userName;
        String url = buildURL(endpoint);
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // Fetch user tweets based on user ID
    public ResponseEntity<String> getUserTweets(String id) {
        rateLimiter.acquire();
        String endpoint = "/v2/UserTweets/?id=" + id + "&count=1";
        String url = buildURL(endpoint);
        HttpEntity<String> entity = new HttpEntity<>(createHeaders());
        return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }

    // Parse User ID from response
    public String parseUserId(ResponseEntity<String> response){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("data").path("user").path("result").path("rest_id").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Parse follower count
    public String parseUserFollowerCount(ResponseEntity<String> response){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("data").path("user").path("result").path("legacy").path("followers_count").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Parse following count
    public String parseUserFollowingCount(ResponseEntity<String> response){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("data").path("user").path("result").path("legacy").path("friends_count").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Parse profile name
    public String parseUserProfileName(ResponseEntity<String> response){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("data").path("user").path("result").path("legacy").path("name").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Parse tweet count
    public String parseUserTweetCount(ResponseEntity<String> response){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("data").path("user").path("result").path("legacy").path("statuses_count").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Parse profile banner URL
    public String parseUserBanner(ResponseEntity<String> response){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("data").path("user").path("result").path("legacy").path("profile_banner_url").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Parse profile picture URL
    public String parseUserPicture(ResponseEntity<String> response){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            return root.path("data").path("user").path("result").path("legacy").path("profile_image_url_https").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Parse recent tweet content
    public String parseUserRecentTweets(ResponseEntity<String> response){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode instructions = root.path("data").path("user").path("result").path("timeline_v2").path("timeline").path("instructions");
            if (instructions.isArray()) {
                for (JsonNode instruction : instructions) {
                    JsonNode entries = instruction.path("entries");
                    if (entries.isArray()) {
                        for (JsonNode entry : entries) {
                            JsonNode tweetTextNode = entry.path("content").path("itemContent")
                                    .path("tweet_results").path("result").path("legacy").path("full_text");
                            if (!tweetTextNode.isMissingNode()) {
                                return tweetTextNode.asText();
                            }
                        }
                    }
                }
            }
            return "No tweets found.";
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // Controller endpoints

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

    @GetMapping("/userRecentTweets")
    public String getUserRecentTweets(@RequestParam String userName){
        return parseUserRecentTweets(getUserTweets(parseUserId(getUserInfo(userName))));
    }
}
