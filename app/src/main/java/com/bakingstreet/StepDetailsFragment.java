package com.bakingstreet;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bakingstreet.data.Constants;
import com.bakingstreet.data.Recipe;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment {

    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    private Recipe.Step mStep;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private String mDescription;
    private String mShortDescription;
    @BindView(R.id.step_video) SimpleExoPlayerView mStepVideoView;
    @BindView(R.id.step_description) TextView mStepDescriptionTextView;
    @BindView(R.id.step_short_description) TextView mStepShortDescriptionTextView;
    NavigationButtonClickHandler mOnNavigationButtonClickHandler;
    private SimpleExoPlayer mExoPlayer;

    public StepDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Initializing layout parameters
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, rootView);

        //Methods
        getStepDetails();
        setupLayoutValues();
        setupBottomNavigation();

        return rootView;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnNavigationButtonClickHandler = (NavigationButtonClickHandler) context;
    }
    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void setupBottomNavigation() {

        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_back:
                                mOnNavigationButtonClickHandler.onBackButtonClick(); break;
                            case R.id.action_previous_step:
                                mOnNavigationButtonClickHandler.onPrevStepClick(); break;
                            case R.id.action_next_step:
                                mOnNavigationButtonClickHandler.onNextStepClick(); break;
                        }
                        return true;
                    }
                });
    }

    //Structural methods
    private void getStepDetails() {
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(Constants.STEP_DETAILS_PARCEL);
        }
        if (mStep!=null) {
            mVideoUrl = mStep.getVideoUrl();
            mThumbnailUrl = mStep.getThumbnailUrl();
            mDescription = mStep.getDescription();
            mShortDescription = mStep.getShortDescription();
        }
    }
    private void setupLayoutValues() {
        if (mDescription!=null) mStepDescriptionTextView.setText(mDescription);
        if (mShortDescription!=null) mStepShortDescriptionTextView.setText(mShortDescription);
        if (mStepVideoView != null) {
            mStepVideoView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_missing_image));

            initializeVideoPlayer(mVideoUrl);
        }
    }
    private void initializeVideoPlayer(String videoUrl) {

        if (getContext()==null) return;

        // Setup ExoPlayer
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mStepVideoView.setPlayer(mExoPlayer);

        // Setup MediaSource
        String userAgent = Util.getUserAgent(getContext(), "ClassicalMusicQuiz");
        MediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse(videoUrl),
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(),
                null,
                null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    //Functional methods
    public interface NavigationButtonClickHandler {
        void onBackButtonClick();
        void onPrevStepClick();
        void onNextStepClick();
    }

}
