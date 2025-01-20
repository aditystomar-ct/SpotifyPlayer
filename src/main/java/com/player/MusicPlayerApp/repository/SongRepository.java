package com.player.MusicPlayerApp.repository;

import com.player.MusicPlayerApp.model.Song;

import java.util.List;

public interface SongRepository {

    void addSong(String name, String artist);
    void playSong(String name, String artist);
    Song findSong(String name, String artist);
    List<Song> getAllSongs();
    List<Song> getTopSongs(int num);
    List<Song> getTopSongsByArtist(String artist, int num);
    List<Song> topDailySongs(int num);
    List<Song> getSongsByArtist(String artist);


}
