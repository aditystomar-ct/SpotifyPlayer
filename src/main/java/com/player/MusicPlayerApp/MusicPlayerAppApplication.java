package com.player.MusicPlayerApp;

import com.player.MusicPlayerApp.model.Song;
import com.player.MusicPlayerApp.playlistExport.ExportFormat;
import com.player.MusicPlayerApp.playlistExport.PlaylistExporter;
import com.player.MusicPlayerApp.playlistExport.PlaylistExporterImpl;
import com.player.MusicPlayerApp.reader.PlaylistFileReader;
import com.player.MusicPlayerApp.reader.PlaylistFileReaderImpl;
import com.player.MusicPlayerApp.repository.SongRepository;
import com.player.MusicPlayerApp.repositoryImpl.SongRepositoryImpl;
import com.player.MusicPlayerApp.service.MusicPlayerSerivce;
import com.player.MusicPlayerApp.serviceImpl.MusicPlayerServiceImpl;

import java.io.IOException;
import java.util.List;

public class MusicPlayerAppApplication {

	public static void main(String[] args) {

		// Initialize components
		SongRepository songRepository = new SongRepositoryImpl();
		PlaylistExporter playlistExporter = new PlaylistExporterImpl();
		PlaylistFileReader playlistFileReader = new PlaylistFileReaderImpl();
		MusicPlayerSerivce musicPlayerService = new MusicPlayerServiceImpl(songRepository, playlistExporter, playlistFileReader);

		// Add songs to the playlist
musicPlayerService.addSong("Pal", "KK");
musicPlayerService.addSong("Yaaron", "KK");
musicPlayerService.addSong("Dil Ibaadat", "KK");
musicPlayerService.addSong("Jeene Laga Hoon", "Atif Aslam");
musicPlayerService.addSong("Phir Mohabbat", "Arijit Singh");
musicPlayerService.addSong("Ajab Si", "KK");
musicPlayerService.addSong("Alvida", "KK");
musicPlayerService.addSong("Tera Hone Laga Hoon", "Atif Aslam");
musicPlayerService.addSong("Tera Ban Jaunga", "Arijit Singh");
musicPlayerService.addSong("Bekhayali", "Sachet Tandon");
musicPlayerService.addSong("Aankhon Mein Teri", "KK");

// Play some songs
musicPlayerService.playSong("Pal", "KK");
musicPlayerService.playSong("Dil Ibaadat", "KK");
musicPlayerService.playSong("Aankhon Mein Teri", "KK");

		// Handle invalid cases
		musicPlayerService.addSong("", "");
		musicPlayerService.addSong("Valid Song", "");
		musicPlayerService.playSong("Non-existent", "Unknown Artist");

		// Get artist-wise and overall song information
		System.out.println("Artist total songs (KK): " + musicPlayerService.getSongsByArtist("KK"));
		System.out.println("Artist total songs (Atif Aslam): " + musicPlayerService.getSongsByArtist("Atif Aslam"));
		System.out.println("Total songs in repository: " + musicPlayerService.getAll());

		// Search for specific songs
		Song foundSong = musicPlayerService.findSong("Beete Lamhe", "KK");
		if (foundSong != null) {
			System.out.println("Found song: " + foundSong.getName());
		} else {
			System.out.println("Song not found!");
		}

		// Fetch top songs
		System.out.println("Daily Top Songs: " + musicPlayerService.getTopDailySongs(2));
		System.out.println("Top Songs: " + musicPlayerService.getTopSongs(2));
		System.out.println("Top Songs of Artist (KK): " + musicPlayerService.getTopArtistSongs("KK", 2));

		// Export playlist to a CSV file
		List<Song> allSongs = musicPlayerService.getAll();
		try {
			musicPlayerService.exportPlaylist(allSongs, "/Users/aditya.tomar/Downloads/SpotifyPlayer/playlist/list1.csv", ExportFormat.CSV);
			System.out.println("CSV file exported successfully.");
		} catch (IOException e) {
			System.err.println("Error while exporting CSV file: " + e.getMessage());
		}

		 //Read songs from a CSV file and display them
//		try {
//			System.out.println("\nReading songs from CSV file:");
//			List<Song> csvSongs = musicPlayerService.readFromFile("/Users/aditya.tomar/Downloads/SpotifyPlayer/playlist/list1.csv");
//			musicPlayerService.displaySongs(csvSongs);
//		} catch (IOException e) {
//			System.err.println("Error reading CSV file: " + e.getMessage());
//		}
	}
}
