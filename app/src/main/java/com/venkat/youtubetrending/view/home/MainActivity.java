package com.venkat.youtubetrending.view.home;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.venkat.youtubetrending.R;
import com.venkat.youtubetrending.model.data.VideoItem;
import com.venkat.youtubetrending.model.data.VideosResponse;
import com.venkat.youtubetrending.util.PaginationScrollListener;
import com.venkat.youtubetrending.view.VideoPlayerActivity;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainContractor.MainView {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private MainContractor.Presenter presenter;

    private static final int PAGE_START = 1;
    private int currentPage = PAGE_START;
    private int TOTAL_PAGES = 10;
    private File file;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private boolean isFirstTime = true;
    private String nextToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        recyclerView = (RecyclerView) findViewById(R.id.video_recycler_view);
        initRecyclerView();
        file = getCacheDir();

        //To fetch data for the first time
        if (isFirstTime) {
            presenter = new MainActivityPresenterImpl(this, new GetVideosIntractorImpl());
            presenter.requestDataFromServer(isNetworkAvailable(), file, nextToken);
        }
    }

    private RecyclerItemClickListener recyclerItemClickListener = new RecyclerItemClickListener() {
        @Override
        public void onItemClick(VideoItem video) {
            Intent intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
            intent.putExtra("videoId", video.getId());
            intent.putExtra("title", video.getTitle());
            intent.putExtra("channel", video.getChannelTitle());
            startActivity(intent);
        }
    };

    //To initialize Recycler View
    private void initRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void setDataToRecyclerView(VideosResponse videosResponse) {

        PaginationAdapter adapter = new PaginationAdapter(MainActivity.this, recyclerItemClickListener);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                //Mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presenter.requestDataFromServer(isNetworkAvailable(), file, nextToken);
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        adapter.removeLoadingFooter();
        isLoading = false;

        List<VideoItem> results = videosResponse.getVideoItems();
        adapter.addAll(results);

        if (currentPage != TOTAL_PAGES) {
            adapter.addLoadingFooter();
            nextToken = videosResponse.getNextPageToken();
        } else {
            isLastPage = true;
        }

        hideProgress();

        if(isFirstTime){
            isFirstTime = !isFirstTime;
        }

    }

    @Override
    public void showProgress() {
        if (progressBar != null && (progressBar.getVisibility() != View.VISIBLE)) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgress() {
        if (progressBar != null && (progressBar.getVisibility() == View.VISIBLE)) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Toast.makeText(MainActivity.this,
                getString(R.string.error_detail) + throwable.getMessage(),
                Toast.LENGTH_LONG).show();
    }

    //To check network connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("isFirstTime", isFirstTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        isFirstTime = savedInstanceState.getBoolean("isFirstTime");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
