package com.retrofit.using.java.utilities.mediastore.audio;

import android.net.Uri;

public class AudioMediaFile {

    private Uri uri;

    private String audioNameWithExtension;
    private String audioNameWithoutExtension;
    private String album;
    private String albumId;
    private String albumKey;
    private String artist;
    private String artistId;
    private String artistKey;
    private String track;
    private String isRingtone;
    private String titleKey;
    private String year;
    private String isPodcast;
    private String isNotification;
    private String isMusic;
    private String bookmark;
    private String composer;
    private String isAlarm;
    private String duration;
    private String mimeType;
    private int fileSize;
    private long dateAdded;
    private long dateModified;

    private String data;

    private int bucketId;
    private String bucketName;
    private long dateTaken;

    public AudioMediaFile(Uri uri, String audioNameWithExtension, String audioNameWithoutExtension, String album, String albumId, String albumKey, String artist, String artistId, String artistKey, String track, String isRingtone, String titleKey, String year, String isPodcast, String isNotification, String isMusic, String bookmark, String composer, String isAlarm, String mimeType, int fileSize, long dateAdded, long dateModified, String data) {
        this.uri = uri;
        this.audioNameWithExtension = audioNameWithExtension;
        this.audioNameWithoutExtension = audioNameWithoutExtension;
        this.album = album;
        this.albumId = albumId;
        this.albumKey = albumKey;
        this.artist = artist;
        this.artistId = artistId;
        this.artistKey = artistKey;
        this.track = track;
        this.isRingtone = isRingtone;
        this.titleKey = titleKey;
        this.year = year;
        this.isPodcast = isPodcast;
        this.isNotification = isNotification;
        this.isMusic = isMusic;
        this.bookmark = bookmark;
        this.composer = composer;
        this.isAlarm = isAlarm;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.data = data;
    }

    public AudioMediaFile(Uri uri, String audioNameWithExtension, String audioNameWithoutExtension, String album, String albumId, String albumKey, String artist, String artistId, String artistKey, String track, String isRingtone, String titleKey, String year, String isPodcast, String isNotification, String isMusic, String bookmark, String composer, String isAlarm, String duration, String mimeType, int fileSize, long dateAdded, long dateModified, String data, int bucketId, String bucketName, long dateTaken) {
        this.uri = uri;
        this.audioNameWithExtension = audioNameWithExtension;
        this.audioNameWithoutExtension = audioNameWithoutExtension;
        this.album = album;
        this.albumId = albumId;
        this.albumKey = albumKey;
        this.artist = artist;
        this.artistId = artistId;
        this.artistKey = artistKey;
        this.track = track;
        this.isRingtone = isRingtone;
        this.titleKey = titleKey;
        this.year = year;
        this.isPodcast = isPodcast;
        this.isNotification = isNotification;
        this.isMusic = isMusic;
        this.bookmark = bookmark;
        this.composer = composer;
        this.isAlarm = isAlarm;
        this.duration = duration;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.data = data;
        this.bucketId = bucketId;
        this.bucketName = bucketName;
        this.dateTaken = dateTaken;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getAudioNameWithExtension() {
        return audioNameWithExtension;
    }

    public void setAudioNameWithExtension(String audioNameWithExtension) {
        this.audioNameWithExtension = audioNameWithExtension;
    }

    public String getAudioNameWithoutExtension() {
        return audioNameWithoutExtension;
    }

    public void setAudioNameWithoutExtension(String audioNameWithoutExtension) {
        this.audioNameWithoutExtension = audioNameWithoutExtension;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumKey() {
        return albumKey;
    }

    public void setAlbumKey(String albumKey) {
        this.albumKey = albumKey;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistKey() {
        return artistKey;
    }

    public void setArtistKey(String artistKey) {
        this.artistKey = artistKey;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getIsRingtone() {
        return isRingtone;
    }

    public void setIsRingtone(String isRingtone) {
        this.isRingtone = isRingtone;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public void setTitleKey(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIsPodcast() {
        return isPodcast;
    }

    public void setIsPodcast(String isPodcast) {
        this.isPodcast = isPodcast;
    }

    public String getIsNotification() {
        return isNotification;
    }

    public void setIsNotification(String isNotification) {
        this.isNotification = isNotification;
    }

    public String getIsMusic() {
        return isMusic;
    }

    public void setIsMusic(String isMusic) {
        this.isMusic = isMusic;
    }

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

    public String getComposer() {
        return composer;
    }

    public void setComposer(String composer) {
        this.composer = composer;
    }

    public String getIsAlarm() {
        return isAlarm;
    }

    public void setIsAlarm(String isAlarm) {
        this.isAlarm = isAlarm;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public long getDateModified() {
        return dateModified;
    }

    public void setDateModified(long dateModified) {
        this.dateModified = dateModified;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getBucketId() {
        return bucketId;
    }

    public void setBucketId(int bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(long dateTaken) {
        this.dateTaken = dateTaken;
    }
}
