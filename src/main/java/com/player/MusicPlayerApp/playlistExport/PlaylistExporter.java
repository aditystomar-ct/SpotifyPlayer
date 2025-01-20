package com.player.MusicPlayerApp.playlistExport;

import com.player.MusicPlayerApp.model.Song;

import java.io.IOException;
import java.util.List;

public interface PlaylistExporter {

    void exportPlaylist(List<Song> songs, String path, ExportFormat format) throws IOException;
}



