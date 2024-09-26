package com.demo.spotify_api_getting_album_cover.model;

public class SpotifyTrackInfo {
    private String albumCoverUrl;
    private String trackLink;

    public SpotifyTrackInfo(String albumCoverUrl, String trackLink) {
        this.albumCoverUrl = albumCoverUrl;
        this.trackLink = trackLink;
    }

    public String getAlbumCoverUrl() {
        return albumCoverUrl;
    }

    public String getTrackLink() {
        return trackLink;
    }
}
