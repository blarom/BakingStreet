package com.bakingstreet;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.session.PlaybackState;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.bakingstreet.data.Statics;
import com.bakingstreet.data.Recipe;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.santalu.aspectratioimageview.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment implements ExoPlayer.EventListener {

    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    private Recipe.Step mStep;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private String mDescription;
    private String mShortDescription;
    @BindView(R.id.step_video) SimpleExoPlayerView mStepVideoView;
    @BindView(R.id.step_description) TextView mStepDescriptionTextView;
    @BindView(R.id.step_short_description) TextView mStepShortDescriptionTextView;
    @BindView(R.id.step_image) AspectRatioImageView mStepImageView;
    NavigationButtonClickHandler mOnNavigationButtonClickHandler;
    private SimpleExoPlayer mExoPlayer;
    private Recipe mRecipe;
    private OnPlayerButtonClickListener onPlayerButtonClickListener;
    private static final String TAG = StepDetailsFragment.class.getSimpleName();
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    public StepDetailsFragment() {
        // Required empty public constructor
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Initializing layout parameters
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, rootView);

        //Methods
        getStepDetails();
        setupLayoutValues();
        setupBottomNavigation();

        return rootView;
    }
    @Override public void onAttach(Context context) {
        super.onAttach(context);
        mOnNavigationButtonClickHandler = (NavigationButtonClickHandler) context;
        onPlayerButtonClickListener = (OnPlayerButtonClickListener) context;
    }
    @Override public void onStop() {
        super.onStop();
        releasePlayer();
    }
    @Override public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mMediaSession!=null) mMediaSession.setActive(false);
    }

    //Structural methods
    private void getStepDetails() {
        if (getArguments() != null) {
            mStep = getArguments().getParcelable(Statics.STEP_DETAILS_PARCEL);
            mRecipe = getArguments().getParcelable(Statics.RECIPE_DETAILS_PARCEL);
        }
        if (mStep!=null) {
            mVideoUrl = mStep.getVideoUrl();
            mThumbnailUrl = mStep.getThumbnailUrl();
            mDescription = mStep.getDescription();
            mShortDescription = mStep.getShortDescription();

            correctPossibleJsonFileErrors();
        }
    }
    private void correctPossibleJsonFileErrors() {
        if (mThumbnailUrl.contains("mp4") && mVideoUrl.equals("")) {
            mVideoUrl = mThumbnailUrl;
            mThumbnailUrl = "";
        }
        if (mThumbnailUrl.equals("")) mThumbnailUrl = mRecipe.getImage();
        if (mDescription.contains("�")) {
            mDescription = mDescription.replace("�","°");
        }
    }
    private void initializeMediaSession() {

        if (getContext()==null) return;

        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PAUSE |
                PlaybackStateCompat.ACTION_PLAY_PAUSE |
                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                PlaybackStateCompat.ACTION_SKIP_TO_NEXT);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new StepVideoSessionCallback());
        mMediaSession.setActive(true);
    }
    private void initializeVideoPlayer(String videoUrl) {

        if (getContext()==null) return;

        // Setup ExoPlayer
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mStepVideoView.setPlayer(mExoPlayer);

        //Setup player button listeners
        mExoPlayer.addListener(this);

        // Setup MediaSource
        String userAgent = Util.getUserAgent(getContext(), "BakingStreet");
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
    private void setStepImage() {
        if (URLUtil.isValidUrl(mThumbnailUrl) && internetIsAvailable()) {
            Picasso.with(getContext())
                    .load(mThumbnailUrl)
                    .error(R.drawable.ic_missing_image)
                    .into(mStepImageView);
        } else if (URLUtil.isValidUrl(mThumbnailUrl) && !internetIsAvailable()) {
            Picasso.with(getContext())
                    .load(mThumbnailUrl)
                    .error(R.drawable.ic_signal_cellular_connected_no_internet_0_bar_black_24dp)
                    .into(mStepImageView);
        } else {
            Uri imageUri = Uri.fromFile(new File(mThumbnailUrl));
            Picasso.with(getContext())
                    .load(imageUri)
                    .error(R.drawable.ic_missing_image)
                    .into(mStepImageView);
        }
    }
    private void setupLayoutValues() {
        if (mDescription!=null) mStepDescriptionTextView.setText(mDescription);
        if (mShortDescription!=null) mStepShortDescriptionTextView.setText(mShortDescription);
        if (mStepVideoView != null) {
            mStepVideoView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_missing_image));

            if (!mVideoUrl.equals("") && internetIsAvailable()) {
                initializeMediaSession();
                initializeVideoPlayer(mVideoUrl);
                mStepVideoView.setVisibility(View.VISIBLE);
                mStepImageView.setVisibility(View.GONE);
            }
            else {
                setStepImage();
                mStepVideoView.setVisibility(View.GONE);
                mStepImageView.setVisibility(View.VISIBLE);
            }
        }
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


    //Exoplayer listeners
    @Override public void onTimelineChanged(Timeline timeline, Object manifest) {

    }
    @Override public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }
    @Override public void onLoadingChanged(boolean isLoading) {

    }
    @Override public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == PlaybackStateCompat.ACTION_SKIP_TO_NEXT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mStateBuilder.setState(PlaybackState.STATE_SKIPPING_TO_PREVIOUS, mExoPlayer.getCurrentPosition(), 1f);
                mMediaSession.setPlaybackState(mStateBuilder.build());
            }
            skipToPreviousRecipeStep();
        }
        if (playbackState == PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mStateBuilder.setState(PlaybackState.STATE_SKIPPING_TO_NEXT, mExoPlayer.getCurrentPosition(), 1f);
                mMediaSession.setPlaybackState(mStateBuilder.build());
            }
            skipToNextRecipeStep();
        }
        if (playbackState == PlaybackStateCompat.STATE_PAUSED) {

        }
    }
    @Override public void onPlayerError(ExoPlaybackException error) {

    }
    @Override public void onPositionDiscontinuity() {

    }

    //Functional methods
    public interface NavigationButtonClickHandler {
        void onBackButtonClick();
        void onPrevStepClick();
        void onNextStepClick();
    }
    private void skipToPreviousRecipeStep() {
        onPlayerButtonClickListener.onSkipToPreviousClicked();
    }
    private void skipToNextRecipeStep() {
        onPlayerButtonClickListener.onSkipToNextClicked();
    }
    public interface OnPlayerButtonClickListener {
        void onSkipToPreviousClicked();
        void onSkipToNextClicked();
    }
    private boolean internetIsAvailable() {
        if (getContext()==null) return false;
        final ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr==null) return false;
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        return (activeNetworkInfo != null) ? true : false;
    }

    private class StepVideoSessionCallback extends MediaSessionCompat.Callback {
        @Override public void onPlay() {
            super.onPlay();
            mExoPlayer.setPlayWhenReady(true);
        }
        @Override public void onPause() {
            super.onPause();
            mExoPlayer.setPlayWhenReady(false);
        }
        @Override public void onSkipToPrevious() {
            super.onSkipToPrevious();
            mExoPlayer.seekTo(0);
            skipToPreviousRecipeStep();
        }
        @Override public void onSkipToNext() {
            super.onSkipToNext();
            skipToNextRecipeStep();
        }
    }
}
