package com.player.MusicPlayerApp.reader;

public enum FileType {
    CSV(","),
    TSV("\t");

    private final String delimiter;

    FileType(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public static FileType fromFileName(String fileName) {
        if (fileName.toLowerCase().endsWith(".tsv")) {
            return TSV;
        }
        return CSV; // default to CSV
    }
}