package com.player.MusicPlayerApp.model;

import java.time.LocalDateTime;

public class Song {

    private final String name;
    private final String artist;
    private int playCount = 0;
    private LocalDateTime lastPlayed;

    public Song(String name, String artist) {
        this.name = name;
        this.artist = artist;
    }

    public String getName() {return name;}
    public String getArtist() {return artist;}
    public int getPlayCount() {return playCount;}
    public LocalDateTime getLastPlayed() {return lastPlayed;}

    public void incrementPlayCount() {++playCount; lastPlayed = LocalDateTime.now();}

    @Override
    public String toString() {
        return String.format("%s by %s (played %d times)",
                name, artist, playCount);
    }
}
