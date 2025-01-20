package com.player.MusicPlayerApp.serviceImpl;

import com.player.MusicPlayerApp.model.Song;
import com.player.MusicPlayerApp.playlistExport.ExportFormat;
import com.player.MusicPlayerApp.playlistExport.PlaylistExporter;
import com.player.MusicPlayerApp.reader.FileType;
import com.player.MusicPlayerApp.reader.PlaylistFileReader;
import com.player.MusicPlayerApp.repository.SongRepository;
import com.player.MusicPlayerApp.service.MusicPlayerSerivce;

import java.io.IOException;
import java.util.List;

public class MusicPlayerServiceImpl implements MusicPlayerSerivce {

    private final SongRepository songRepository;
    private final PlaylistExporter playlistExporter;
    private final PlaylistFileReader fileReader;

    public MusicPlayerServiceImpl(SongRepository songRepository, PlaylistExporter playlistExporter, PlaylistFileReader fileReader) {
        this.songRepository = songRepository;
        this.playlistExporter = playlistExporter;
        this.fileReader = fileReader;
    }

    @Override
    public void addSong(String name, String artist) {
        if (isNullOrEmpty(name) || isNullOrEmpty(artist)) {
            System.out.println("Invalid input: Song name and artist cannot be empty.");
            return;
        }
        songRepository.addSong(name.trim(), artist.trim());
    }

    @Override
    public void playSong(String name, String artist) {
        if (isNullOrEmpty(name) || isNullOrEmpty(artist)) {
            System.out.println("Invalid input: Song name and artist cannot be empty.");
            return;
        }
        songRepository.playSong(name.trim(), artist.trim());
    }

    @Override
    public Song findSong(String name, String artist) {
        if (isNullOrEmpty(name) || isNullOrEmpty(artist)) {
            System.out.println("Invalid input: Song name and artist cannot be empty.");
            return null;
        }
        return songRepository.findSong(name.trim(), artist.trim());
    }

    @Override
    public List<Song> getTopSongs(int num) {
        return songRepository.getTopSongs(num);
    }

    @Override
    public List<Song> getTopArtistSongs(String artist, int num) {
        if (isNullOrEmpty(artist)) {
            System.out.println("Invalid input: Artist name cannot be empty.");
            return List.of();
        }
        return songRepository.getTopSongsByArtist(artist.trim(), num);
    }

    @Override
    public List<Song> getTopDailySongs(int num) {
        return songRepository.topDailySongs(num);
    }

    @Override
    public List<Song> getSongsByArtist(String artist) {
        if (isNullOrEmpty(artist)) {
            System.out.println("Invalid input: Artist name cannot be empty.");
            return List.of();
        }
        return songRepository.getSongsByArtist(artist.trim());
    }

    @Override
    public void exportPlaylist(List<Song> songs, String filePath, ExportFormat format) throws IOException {
        if (songs == null || songs.isEmpty()) {
            throw new IllegalArgumentException("Playlist cannot be empty for export.");
        }
        if (isNullOrEmpty(filePath)) {
            throw new IllegalArgumentException("File path cannot be empty for export.");
        }
        playlistExporter.exportPlaylist(songs, filePath.trim(), format);
    }

    @Override
    public List<Song> readFromFile(String filePath) throws IOException {
        if (isNullOrEmpty(filePath)) {
            throw new IllegalArgumentException("File path cannot be empty for reading.");
        }
        FileType fileType = FileType.fromFileName(filePath.trim());
        return fileReader.readFile(filePath.trim(), fileType);
    }

    private String truncateString(String str, int maxLength) {
        if (str == null) return "";
        return (str.length() <= maxLength) ? str : str.substring(0, maxLength - 3) + "...";
    }

    @Override
    public void displaySongs(List<Song> songs) {
        if (songs == null || songs.isEmpty()) {
            System.out.println("No songs to display.");
            return;
        }

        System.out.println("\nSong List:");
        System.out.println("------------------------------------------------------------");
        System.out.printf("%-30s %-20s %-10s%n", "Name", "Artist", "Play Count");
        System.out.println("------------------------------------------------------------");

        for (Song song : songs) {
            System.out.printf("%-30s %-20s %-10d%n",
                    truncateString(song.getName(), 29),
                    truncateString(song.getArtist(), 19),
                    song.getPlayCount());
        }

        System.out.println("------------------------------------------------------------");
        System.out.println("Total songs: " + songs.size());
    }

    @Override
    public List<Song> getAll() {
        return songRepository.getAllSongs();
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
