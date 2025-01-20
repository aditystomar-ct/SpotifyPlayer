package com.player.MusicPlayerApp.playlistExport;


import com.player.MusicPlayerApp.model.Song;

import java.io.*;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlaylistExporterImpl implements PlaylistExporter {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void exportPlaylist(List<Song> songs, String filePath, ExportFormat format)
            throws IOException {
        if (songs == null || songs.isEmpty()) {
            throw new IllegalArgumentException("Song list cannot be null or empty");
        }


        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Create directories if they don't exist

        System.out.println("Exporting to: " + file.getAbsolutePath()); // Debug log

        try (FileWriter fw = new FileWriter(file);
             BufferedWriter writer = new BufferedWriter(fw)) {

            String delimiter = format.getDelimiter();

            // Write header
            writer.write(String.join(delimiter, "Name", "Artist", "Play Count", "Last Played"));
            writer.newLine();

            // Write each song
            for (Song song : songs) {
                StringBuilder line = new StringBuilder();
                line.append(escapeField(song.getName(), delimiter)).append(delimiter)
                        .append(escapeField(song.getArtist(), delimiter)).append(delimiter)
                        .append(song.getPlayCount()).append(delimiter)
                        .append(song.getLastPlayed() != null ?
                                song.getLastPlayed().format(DATE_FORMATTER) : "Never");

                writer.write(line.toString());
                writer.newLine();

                // Debug log
                System.out.println("Wrote song: " + line);
            }

            writer.flush(); // Ensure all data is written
        }
    }

    private String escapeField(String field, String delimiter) {
        if (field == null) return "";
        if (field.contains(delimiter) || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
}