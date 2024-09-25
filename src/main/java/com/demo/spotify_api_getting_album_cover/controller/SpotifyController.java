package com.demo.spotify_api_getting_album_cover.controller;

import com.demo.spotify_api_getting_album_cover.service.SpotifyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpotifyController {

    private final SpotifyService spotifyService;

    public SpotifyController(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    @GetMapping("/album-cover")
    public String getAlbumCover(@RequestParam String trackName) {
        return spotifyService.getAlbumCover(trackName);
    }

}
