package com.player.MusicPlayerApp.repositoryImpl;

import ch.qos.logback.core.joran.sanity.Pair;
import com.player.MusicPlayerApp.model.Song;
import com.player.MusicPlayerApp.repository.SongRepository;

import java.time.LocalDate;
import java.util.*;


public class SongRepositoryImpl implements SongRepository {

    private static final  Map<String, Song> allSongs = new HashMap<>();
    private  List<Song> totalSongs = new ArrayList<>();
    private final Map<String, List<Song>> songsByArtist = new HashMap<>();
    private final PriorityQueue<Song> topSongs;
    private final Map<String, PriorityQueue<Song>> topArtistSongs = new HashMap<>();
    private final Map<LocalDate, PriorityQueue<Song>> topDailySongs = new HashMap<>();
    private final Map<LocalDate, Set<Song>> dailyPlayedSongs = new HashMap<>();

    public SongRepositoryImpl() {
        Comparator<Song> byPlayCount = (s1, s2) -> {return s2.getPlayCount() - s1.getPlayCount();};
        topSongs = new PriorityQueue<>(byPlayCount);
    }

    public String getKey(String name, String artist) {
        String res = name.trim() + "::" + artist.trim();
        return res.toLowerCase();
    }

    @Override
    public void addSong(String name, String artist) {
        String key = getKey(name, artist);
        if(allSongs.containsKey(key)) {
            System.out.println("Song already exists");
            return ;
        }
        Song song = new Song(name, artist);
        totalSongs.add(song);
        allSongs.put(key, song);
        List<Song> artistSongs;
        if (songsByArtist.containsKey(artist.toLowerCase())) {
            artistSongs = songsByArtist.get(artist.toLowerCase());
        } else {
            artistSongs = new ArrayList<>();
            songsByArtist.put(artist.toLowerCase(), artistSongs);
        }
        artistSongs.add(song);

        topSongs.offer(song);

        PriorityQueue<Song> artistQueue;
        if (topArtistSongs.containsKey(artist.toLowerCase())) {
            artistQueue = topArtistSongs.get(artist.toLowerCase());
        } else {
            artistQueue = new PriorityQueue<>((s1, s2) -> s2.getPlayCount() - s1.getPlayCount());
            topArtistSongs.put(artist.toLowerCase(), artistQueue);
        }
        artistQueue.offer(song);
        System.out.println("Added song: " + name + " " + artist);
    }

    @Override
    public void playSong(String name, String artist) {
        String key = getKey(name, artist);
        Song song = allSongs.get(key);

        if(song == null) {
            System.out.println("Song does not exist");
            return;
        }

        topSongs.remove(song);
        PriorityQueue<Song> artistQueue = topArtistSongs.get(artist.toLowerCase());
        if(artistQueue != null) {
            artistQueue.remove(song);
        }

        song.incrementPlayCount();
        topSongs.add(song);

        if(artistQueue != null) {artistQueue.add(song);}

        LocalDate today = LocalDate.now();
        dailyPlayedSongs.computeIfAbsent(today, k -> new HashSet<>()).add(song);

        PriorityQueue<Song> dailyQueue = topDailySongs.computeIfAbsent(today,
                k -> new PriorityQueue<>((s1, s2) -> s2.getPlayCount() - s1.getPlayCount()));
        dailyQueue.remove(song);
        dailyQueue.offer(song);

        System.out.println("Playing song: " + name + " " + artist);

    }

    @Override
    public Song findSong(String name, String artist) {
        String key = getKey(name, artist);
        Song song = allSongs.get(key);
        if(song == null) {System.out.println("Song does not exist"); return null;}
        return song;
    }

    @Override
    public List<Song> getAllSongs() {
        return totalSongs;
    }

    private List<Song> getTopFromQueue(PriorityQueue<Song> topSongs, int num) {
        List<Song> res = new ArrayList<>();
        PriorityQueue<Song> tempQueue = new PriorityQueue<>(topSongs);

        for(int i = 0; i < num && !tempQueue.isEmpty(); i++) {
            res.add(tempQueue.poll());
        }
        return res;
    }


    @Override
    public List<Song> getTopSongs(int num) {
        return getTopFromQueue(topSongs, num);
    }

    @Override
    public List<Song> getTopSongsByArtist(String artist, int num) {
        PriorityQueue<Song> artistQueue = topArtistSongs.get(artist.toLowerCase());
        if(artistQueue == null) {
            return new ArrayList<>();
        }
        return getTopFromQueue(artistQueue, num);
    }

    @Override
    public List<Song> topDailySongs(int num) {
//        PriorityQueue<Song> dailyQueue = topDailySongs.get(LocalDate.now());
//        if(dailyQueue == null) {
//            return new ArrayList<>();
//        }
//        return getTopFromQueue(dailyQueue, num);

        LocalDate today = LocalDate.now();
        PriorityQueue<Song> dailyQueue = topDailySongs.get(today);
        if (dailyQueue == null) {
            return new ArrayList<>();
        }

        // Use Set to ensure uniqueness
        Set<Song> uniqueSongs = new LinkedHashSet<>();
        PriorityQueue<Song> tempQueue = new PriorityQueue<>(dailyQueue);

        while (!tempQueue.isEmpty() && uniqueSongs.size() < num) {
            uniqueSongs.add(tempQueue.poll());
        }

        return new ArrayList<>(uniqueSongs);
    }

    @Override
    public List<Song> getSongsByArtist(String artist) {
        List<Song> songs;
        if (songsByArtist.containsKey(artist.toLowerCase())) {
            songs = songsByArtist.get(artist.toLowerCase());
        } else {
            songs = new ArrayList<>();
        }
        return songs;
    }
}
