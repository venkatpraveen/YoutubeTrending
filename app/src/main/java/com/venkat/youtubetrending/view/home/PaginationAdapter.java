package com.venkat.youtubetrending.view.home;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.venkat.youtubetrending.R;
import com.venkat.youtubetrending.model.data.VideoItem;

import java.util.ArrayList;
import java.util.List;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    private List<VideoItem> videoResults;
    private Activity activity;
    private RecyclerItemClickListener recyclerItemClickListener;


    public PaginationAdapter(Activity activity, RecyclerItemClickListener recyclerItemClickListener) {
        this.activity = activity;
        this.recyclerItemClickListener = recyclerItemClickListener;
        videoResults = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View view = inflater.inflate(R.layout.item_video, parent, false);
        viewHolder = new VideoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final VideoItem result = videoResults.get(position); // VideoItem

        switch (getItemViewType(position)) {
            case ITEM:
                final VideoViewHolder videoViewHolder = (VideoViewHolder) holder;

                videoViewHolder.mVideoTitle.setText(result.getTitle());
                videoViewHolder.mDate.setText(result.getPublishedAt().substring(0, 10));
                videoViewHolder.mChannelName.setText(result.getChannelTitle());

                //Using Glide to handle image loading.
                Glide
                        .with(activity)
                        .load(result.getThumbnails())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                                videoViewHolder.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                                videoViewHolder.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(videoViewHolder.mPosterImg);

                videoViewHolder.mPosterImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerItemClickListener.onItemClick(videoResults.get(position));
                    }
                });
                break;

            case LOADING:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return videoResults == null ? 0 : videoResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == videoResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    //Helpers
    public void add(VideoItem item) {
        videoResults.add(item);
        notifyItemInserted(videoResults.size() - 1);
    }

    public void addAll(List<VideoItem> moveResults) {
        for (VideoItem result : moveResults) {
            add(result);
        }
    }

    public void remove(VideoItem item) {
        int position = videoResults.indexOf(item);
        if (position > -1) {
            videoResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new VideoItem());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = videoResults.size() - 1;
        if (position > 0) {
            VideoItem result = getItem(position);

            if (result != null) {
                videoResults.remove(position);
                notifyItemRemoved(position);
            }
        }
    }

    public VideoItem getItem(int position) {
        return videoResults.get(position);
    }


    //View Holders
    protected class VideoViewHolder extends RecyclerView.ViewHolder {
        private TextView mVideoTitle;
        private TextView mChannelName;
        private TextView mDate;
        private ImageView mPosterImg;
        private ProgressBar mProgress;

        public VideoViewHolder(View itemView) {
            super(itemView);

            mVideoTitle = (TextView) itemView.findViewById(R.id.title);
            mChannelName = (TextView) itemView.findViewById(R.id.channel);
            mDate = (TextView) itemView.findViewById(R.id.date);
            mPosterImg = (ImageView) itemView.findViewById(R.id.img);
            mProgress = (ProgressBar) itemView.findViewById(R.id.progress_load_thumbnail);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}