package com.xxl.job.executor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;
import java.util.Map;

public class TokenRequestExample {

    public static void main(String[] args) {
        // REST endpoint URL
        String url = "http://192.168.2.87:9008/API/V2/getToken";

        // appKey and appSecret
        String appKey = "B8F89636";
        String appSecret = "CAAA9DAAEFDDE7EEA78734C0E972522C";

        // Create RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // Prepare request body
        Map<String, String> body = new HashMap<>();
        body.put("appKey", appKey);
        body.put("appSecret", appSecret);

        // Wrap headers and body in HttpEntity
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            // Send POST request
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            // Check response status and process result
            if (response.getStatusCode().is2xxSuccessful()) {
                String token = response.getBody();
                System.out.println("Token received: " + token);
            } else {
                System.err.println("Failed to fetch token. HTTP Status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error occurred while requesting token: " + e.getMessage());
        }
    }
}
