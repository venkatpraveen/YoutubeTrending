package com.venkat.youtubetrending.model.data;

import com.google.gson.annotations.SerializedName;

public class VideoItem {

    @SerializedName("kind")
    private String kind;

    @SerializedName("id")
    private String id;

    @SerializedName("publishedAt")
    private String publishedAt;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String thumbnails;

    @SerializedName("channelTitle")
    private String channelTitle;

    public VideoItem(){}

    public VideoItem(String kind, String id, String publishedAt, String title, String thumbnails, String channelTitle) {
        this.kind = kind;
        this.id = id;
        this.publishedAt = publishedAt;
        this.title = title;
        this.thumbnails = thumbnails;
        this.channelTitle = channelTitle;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }
}
