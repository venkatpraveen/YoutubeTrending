package com.venkat.youtubetrending.view.home;

import android.util.Log;

import com.venkat.youtubetrending.model.data.VideosResponse;
import com.venkat.youtubetrending.model.network.Client;
import com.venkat.youtubetrending.model.network.Service;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.venkat.youtubetrending.model.network.Constants.API_KEY;

public class GetVideosIntractorImpl implements MainContractor.GetVideosIntractor {
    @Override
    public void getVideos(final OnFinishedListener onFinishedListener, Boolean boo, File file, String nextPageToken) {
        final Boolean isNetworkAvailable = boo;
        final int TOTAL_PAGES = 10;
        int cacheSize = 10 * 1024 * 1024; //10 MB

        try {
            Cache cache = new Cache(file, cacheSize);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .cache(cache)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public okhttp3.Response intercept(Interceptor.Chain chain)
                                throws IOException {
                            Request request = chain.request();
                            int maxStale = 60 * 5; // tolerate 5 minutes stale
                            if (!isNetworkAvailable) {
                                request = request
                                        .newBuilder()
                                        .header("Cache-Control",
                                                "public, only-if-cached, max-stale=" + maxStale)
                                        .build();
                            } else {
                                request = request
                                        .newBuilder()
                                        .header("Cache-Control",
                                                "public, max-stale=" + maxStale)
                                        .build();
                            }
                            return chain.proceed(request);
                        }
                    })
                    .build();


            if (isNetworkAvailable) {

                Service service = Client
                        .getRetrofitInstance(VideosResponse.class,
                                new VideoResponseDeserializer(),
                                okHttpClient)
                        .create(Service.class);

                Call<VideosResponse> call = service.getPopularVideos(
                        "snippet",
                        "mostPopular",
                        TOTAL_PAGES,
                        nextPageToken,
                        API_KEY);

                call.enqueue(new Callback<VideosResponse>() {
                    @Override
                    public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                        Log.d("Response is: ", response.body().getVideoItems().get(0).getThumbnails());
                        onFinishedListener.onFinished(response.body());
                    }

                    @Override
                    public void onFailure(Call<VideosResponse> call, Throwable t) {
                        onFinishedListener.onFailure(t);
                    }
                });
            }
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }

    }
}
