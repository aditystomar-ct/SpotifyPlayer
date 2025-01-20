package com.player.MusicPlayerApp.reader;

import com.player.MusicPlayerApp.model.Song;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistFileReaderImpl implements PlaylistFileReader {

    @Override
    public List<Song> readFile(String filePath, FileType fileType) throws IOException {
        List<Song> songs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String delimiter = fileType.getDelimiter();

            // Skip header
            String header = reader.readLine();
            if (header == null) {
                throw new IOException("File is empty");
            }

            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    Song song = parseLine(line, delimiter);
                    if (song != null) {
                        songs.add(song);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + e.getMessage());
                }
            }
        }
        return songs;
    }

    private Song parseLine(String line, String delimiter) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        // Split by delimiter, handling quoted fields
        String[] fields;
        if (delimiter.equals("\t")) {
            fields = line.split("\t");
        } else {
            fields = line.split(delimiter + "(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        }

        if (fields.length < 2) {
            throw new IllegalArgumentException("Line must contain at least name and artist");
        }

        String name = unescapeField(fields[0]);
        String artist = unescapeField(fields[1]);

        Song song = new Song(name, artist);

        // If play count exists, set it
        if (fields.length > 2 && !fields[2].trim().isEmpty()) {
            try {
                int playCount = Integer.parseInt(fields[2].trim());
                for (int i = 0; i < playCount; i++) {
                    song.incrementPlayCount();
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid play count: " + fields[2]);
            }
        }

        return song;
    }

    private String unescapeField(String field) {
        if (field == null) return "";

        field = field.trim();
        if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
            field = field.replace("\"\"", "\"");
        }
        return field;
    }
}
