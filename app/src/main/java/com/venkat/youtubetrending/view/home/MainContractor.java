package com.venkat.youtubetrending.view.home;

import com.venkat.youtubetrending.model.data.VideosResponse;

import java.io.File;

public interface MainContractor {
    //Call when user interact with the view and other when view OnDestroy()
    interface Presenter {

        void onDestroy();

        void onPagination(Boolean boo, File file, String nextPageToken);

        void requestDataFromServer(Boolean boo, File file, String nextPageToken);

    }

    /**
     * showProgress() and hideProgress() would be used for displaying and hiding the progressBar
     * while the setDataToRecyclerView and onResponseFailure is fetched from the GetVideosInteractorImpl class
     **/
    interface MainView {

        void showProgress();

        void hideProgress();

        void setDataToRecyclerView(VideosResponse videosResponse);

        void onResponseFailure(Throwable throwable);

    }

    //Intractors are classes built for fetching data from youtube API.
    interface GetVideosIntractor {

        interface OnFinishedListener {
            void onFinished(VideosResponse videosResponse);

            void onFailure(Throwable t);
        }

        void getVideos(OnFinishedListener onFinishedListener, Boolean boo, File file, String nextPageToken);
    }
}
