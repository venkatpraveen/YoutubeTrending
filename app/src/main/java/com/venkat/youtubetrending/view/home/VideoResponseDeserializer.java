package com.venkat.youtubetrending.view.home;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.venkat.youtubetrending.model.data.VideoItem;
import com.venkat.youtubetrending.model.data.VideosResponse;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VideoResponseDeserializer implements JsonDeserializer<VideosResponse> {

    @Override
    public VideosResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        List items = new ArrayList<>();

        final JsonObject jsonObject = json.getAsJsonObject();
        final String kind = jsonObject.get("kind").getAsString();
        final String nextPageToken = jsonObject.get("nextPageToken").getAsString();

        final JsonArray videoItemsJsonArray = jsonObject.get("items").getAsJsonArray();

        for (JsonElement videoJsonElement : videoItemsJsonArray) {
            final JsonObject videoJsonObject = videoJsonElement.getAsJsonObject();
            final String videoKind = videoJsonObject.get("kind").getAsString();
            final String id = videoJsonObject.get("id").getAsString();

            final JsonObject snippet = videoJsonObject.get("snippet").getAsJsonObject();
            final String publishedAt = snippet.get("publishedAt").getAsString();
            final String title = snippet.get("title").getAsString();

            final JsonObject thumbnails = snippet.get("thumbnails").getAsJsonObject();
            final JsonObject standard = thumbnails.get("standard").getAsJsonObject();
            final String url = standard.get("url").getAsString();

            final String channelTitle = snippet.get("channelTitle").getAsString();

            items.add(new VideoItem(kind, id, publishedAt, title, url, channelTitle));
        }

        return new VideosResponse(kind, nextPageToken, items);
    }
}
