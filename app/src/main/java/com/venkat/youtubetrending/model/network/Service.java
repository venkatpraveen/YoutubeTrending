package com.venkat.youtubetrending.model.network;

import com.venkat.youtubetrending.model.data.VideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Service {

    @GET("youtube/v3/videos")
    Call<VideosResponse> getPopularVideos(@Query("part") String part,
                                          @Query("chart") String searchBy,
                                          @Query("maxResults") int paginationSize,
                                          @Query("pageToken") String pageToken,
                                          @Query("key") String apiKey);
}
