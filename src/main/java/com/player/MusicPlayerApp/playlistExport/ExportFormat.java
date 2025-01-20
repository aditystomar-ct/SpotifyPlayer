package com.player.MusicPlayerApp.playlistExport;

public enum ExportFormat {
    CSV(","),
    TSV("\t");

    private final String delimiter;

    private ExportFormat(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }
}