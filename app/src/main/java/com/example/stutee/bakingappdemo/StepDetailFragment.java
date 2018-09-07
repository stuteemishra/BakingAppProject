package com.example.stutee.bakingappdemo;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    @BindView(R.id.step_full_description)
    TextView fullDescriptionTv;
    @BindView(R.id.step_thumbnail)
    ImageView stepThumbnailView;
    @BindView(R.id.no_videoImage)
    ImageView noVideoImageView;
    @BindView(R.id.playerView)
    SimpleExoPlayerView stepVideoView;

    SimpleExoPlayer exoPlayer;

    private String mStepDescription;
    private String mStepVideoURL;
    private String mStepThumbnail;

    private long startPosition;

    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "position";
    private static final String KEY_AUTO_PLAY = "auto_play";


    public void setmStepDescription(String mStepDescription) {
        this.mStepDescription = mStepDescription;
    }

    public void setmStepVideoURL(String mStepVideoURL) {
        this.mStepVideoURL = mStepVideoURL;
    }

    public StepDetailFragment() {

    }


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (savedInstanceState != null) {

            startPosition = savedInstanceState.getLong(KEY_POSITION);
        }

        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        ButterKnife.bind(this,rootView);

        fullDescriptionTv.setText(mStepDescription);

        mStepThumbnail = getArguments().getString("thumbnail");

        Uri thumbnailUri = Uri.parse(mStepThumbnail).buildUpon().build();

        Picasso.with(getContext()).load(thumbnailUri)
                .into(stepThumbnailView);


        if (!mStepVideoURL.isEmpty()) {

            initializePlayer();
            noVideoImageView.setVisibility(View.GONE);


        } else{
            Log.v(StepDetailFragment.class.getSimpleName(), "null url"+ mStepVideoURL);
            stepVideoView.setVisibility(View.GONE);
            noVideoImageView.setVisibility(View.VISIBLE);
        }

        return rootView;

    }

    private void initializePlayer() {
        if (exoPlayer == null) {
            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveVideoTrackSelection.Factory(bandwidthMeter));
                LoadControl loadControl = new DefaultLoadControl();
                exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);


                Uri videoURI = Uri.parse(mStepVideoURL);

                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

                stepVideoView.setPlayer(exoPlayer);

                exoPlayer.prepare(mediaSource);
                if(startPosition != 0){

                    exoPlayer.seekTo(startPosition);

                }
                exoPlayer.setPlayWhenReady(true);
            } catch (Exception e) {
                Log.v("StepDetailFragment", "error occurred: " + e);
            }
        }


    }

    private void releasePlayer() {
        if(exoPlayer != null){
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

    }


    @Override

    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }

    }

    @Override

    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || exoPlayer == null) {
            initializePlayer();
        }


    }
    @Override

    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }


    }



    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        startPosition = exoPlayer.getCurrentPosition();
        outState.putLong(KEY_POSITION, startPosition);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }

    }

}

