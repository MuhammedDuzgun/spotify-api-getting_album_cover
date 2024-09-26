package com.demo.spotify_api_getting_album_cover.service;

import com.demo.spotify_api_getting_album_cover.model.SpotifyTrackInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyService {

    @Value("${spotify.api.url}")
    private String apiUrl;

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    private String accessToken;


    public SpotifyTrackInfo getAlbumCoverAndLink(String trackName) {
        if (accessToken == null || isTokenExpired()) {
            accessToken = getAccessToken();
        }

        String url = apiUrl + "/search?q=" + trackName + "&type=track&limit=1";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return extractTrackInfo(response.getBody());
    }

    private SpotifyTrackInfo extractTrackInfo(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            JsonNode trackNode = jsonNode.path("tracks").path("items").get(0);
            String albumCoverUrl = trackNode.path("album").path("images").get(0).path("url").asText();
            String trackLink = trackNode.path("external_urls").path("spotify").asText();

            return new SpotifyTrackInfo(albumCoverUrl, trackLink);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Hata durumunda null döndür
        }
    }

    private String getAccessToken() {
        String tokenUrl = "https://accounts.spotify.com/api/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

        return extractAccessToken(response.getBody());
    }

    private String extractAccessToken(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Hata durumunda null döndür
        }
    }

    private boolean isTokenExpired() {
        // Access token geçerlilik kontrolü yapılabilir, şu an için her çağırdığında yeniliyor
        return false;
    }
}

