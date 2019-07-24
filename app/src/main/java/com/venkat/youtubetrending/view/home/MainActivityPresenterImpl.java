package com.venkat.youtubetrending.view.home;

import com.venkat.youtubetrending.model.data.VideosResponse;

import java.io.File;

public class MainActivityPresenterImpl implements MainContractor.Presenter, MainContractor.GetVideosIntractor.OnFinishedListener {

    private MainContractor.MainView mainView;
    private MainContractor.GetVideosIntractor getVideosIntractor;

    public MainActivityPresenterImpl(MainContractor.MainView mainView, MainContractor.GetVideosIntractor getVideosIntractor) {
        this.mainView = mainView;
        this.getVideosIntractor = getVideosIntractor;
    }

    @Override
    public void onDestroy() {
        mainView = null;
    }

    @Override
    public void onPagination(Boolean boo, File file, String nextPageToken) {
        if (mainView != null) {
            mainView.showProgress();
        }
        getVideosIntractor.getVideos(this, boo, file, nextPageToken);
    }

    @Override
    public void requestDataFromServer(Boolean boo, File file, String nextPageToken) {
        getVideosIntractor.getVideos(this, boo, file, nextPageToken);
    }

    @Override
    public void onFinished(VideosResponse videosResponse) {
        if (mainView != null) {
            mainView.setDataToRecyclerView(videosResponse);
            mainView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {
        if (mainView != null) {
            mainView.onResponseFailure(t);
            mainView.hideProgress();
        }
    }
}
