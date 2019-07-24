package com.venkat.youtubetrending.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideosResponse {

    @SerializedName("kind")
    private String kind;

    @SerializedName("nextPageToken")
    private String nextPageToken;

    @SerializedName("items")
    private List<VideoItem> videoItems;

    public VideosResponse(){}

    public VideosResponse(String kind, String nextPageToken, List<VideoItem> videoItems) {
        this.kind = kind;
        this.nextPageToken = nextPageToken;
        this.videoItems = videoItems;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<VideoItem> getVideoItems() {
        return videoItems;
    }

    public void setVideoItems(List<VideoItem> items) {
        this.videoItems = items;
    }
}
