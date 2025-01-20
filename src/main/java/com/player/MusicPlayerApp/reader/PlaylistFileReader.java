package com.player.MusicPlayerApp.reader;

import com.player.MusicPlayerApp.model.Song;

import java.io.IOException;
import java.util.List;

public interface PlaylistFileReader {
    List<Song> readFile(String filePath, FileType fileType) throws IOException;
}
