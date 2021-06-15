package com.retrofit.using.java.utilities.mediastore.video;

import android.net.Uri;

public class VideoMediaFile {

    private Uri uri;

    private String videoNameWithExtension;
    private String videoNameWithoutExtension;
    private String description;
    private String mimeType;
    private int fileSize;
    private int width;
    private int height;
    private long dateAdded;
    private long dateModified;

    private String data;
    private String latitude;
    private String longitude;

    private int bucketId;
    private String bucketName;
    private long dateTaken;
    private int orientation;
    private String duration;

    public VideoMediaFile(Uri uri, String videoNameWithExtension, String videoNameWithoutExtension, String description, String mimeType, int fileSize, int width, int height, long dateAdded, long dateModified, String data, String latitude, String longitude, int bucketId, String bucketName, long dateTaken, int orientation, String duration) {
        this.uri = uri;
        this.videoNameWithExtension = videoNameWithExtension;
        this.videoNameWithoutExtension = videoNameWithoutExtension;
        this.description = description;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.width = width;
        this.height = height;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.data = data;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bucketId = bucketId;
        this.bucketName = bucketName;
        this.dateTaken = dateTaken;
        this.orientation = orientation;
        this.duration = duration;
    }

    public VideoMediaFile(Uri uri, String videoNameWithExtension, String videoNameWithoutExtension, String description, String mimeType, int fileSize, int width, int height, long dateAdded, long dateModified, String data, String latitude, String longitude) {
        this.uri = uri;
        this.videoNameWithExtension = videoNameWithExtension;
        this.videoNameWithoutExtension = videoNameWithoutExtension;
        this.description = description;
        this.mimeType = mimeType;
        this.fileSize = fileSize;
        this.width = width;
        this.height = height;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.data = data;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getVideoNameWithExtension() {
        return videoNameWithExtension;
    }

    public void setVideoNameWithExtension(String videoNameWithExtension) {
        this.videoNameWithExtension = videoNameWithExtension;
    }

    public String getVideoNameWithoutExtension() {
        return videoNameWithoutExtension;
    }

    public void setVideoNameWithoutExtension(String videoNameWithoutExtension) {
        this.videoNameWithoutExtension = videoNameWithoutExtension;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
