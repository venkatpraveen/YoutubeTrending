package com.venkat.youtubetrending.view;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.venkat.youtubetrending.R;

import static com.venkat.youtubetrending.model.network.Constants.API_KEY;

public class VideoPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener,
        YouTubePlayer.PlaybackEventListener, YouTubePlayer.PlayerStateChangeListener {

    private YouTubePlayerView youTubePlayer;
    private TextView titleTextView;
    private TextView channelTextView;

    private String videoId = "";
    private String title = "";
    private String channel = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        youTubePlayer = (YouTubePlayerView) findViewById(R.id.player);
        youTubePlayer.initialize(API_KEY, this);
        titleTextView = (TextView) findViewById(R.id.title);
        channelTextView = (TextView) findViewById(R.id.channel);

        if (getIntent().getExtras() != null) {
            videoId = getIntent().getStringExtra("videoId");
            title = getIntent().getStringExtra("title");
            channel = getIntent().getStringExtra("channel");
            titleTextView.setText(title);
            channelTextView.setText(channel);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStateChangeListener(this);
        youTubePlayer.setPlaybackEventListener(this);

        if (!b) {
            youTubePlayer.cueVideo(videoId);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onLoaded(String s) {

    }

    @Override
    public void onAdStarted() {

    }

    @Override
    public void onVideoStarted() {

    }

    @Override
    public void onVideoEnded() {

    }

    @Override
    public void onError(YouTubePlayer.ErrorReason errorReason) {

    }
}
