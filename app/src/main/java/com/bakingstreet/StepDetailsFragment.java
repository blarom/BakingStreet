package com.bakingstreet;

import com.bakingstreet.IdlingResource.FakeVideoDownloader;
import com.bakingstreet.IdlingResource.TimeDelayIdlingResource;
import com.bakingstreet.data.Statics;
import com.bakingstreet.data.Recipe;

import java.io.File;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.BottomNavigationView;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.santalu.aspectratioimageview.AspectRatioImageView;
import com.squareup.picasso.Picasso;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment implements
        FakeVideoDownloader.DelayerCallback {


    private Recipe.Step mStep;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private String mDescription;
    private String mShortDescription;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;
    @BindView(R.id.step_video) SimpleExoPlayerView mStepVideoView;
    @BindView(R.id.step_description) TextView mStepDescriptionTextView;
    @BindView(R.id.step_short_description) TextView mStepShortDescriptionTextView;
    @BindView(R.id.step_image) AspectRatioImageView mStepImageView;
    @BindView(R.id.exo_rew) ImageView mExoBackToRecipesListImageView;
    @BindView(R.id.exo_prev) ImageView mExoPrevImageView;
    @BindView(R.id.exo_ffwd) ImageView mExoNextImageView; //Overrode the ffwd with Next button since it is enalbed by default
    NavigationButtonClickHandler mOnNavigationButtonClickHandler;
    private SimpleExoPlayer mExoPlayer;
    private Recipe mRecipe;
    private static final String TAG = StepDetailsFragment.class.getSimpleName();
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private int timesClicked;
    private int mNumberOfSteps;
    private int mCurrentStepIndex;

    public StepDetailsFragment() {
        // Required empty public constructor
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Initializing layout parameters
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, rootView);

        //Methods
        getStepDetails();
        setupLayoutCharacteristics();
        setupBottomNavigationBarCharacteristics();

        //Test methods
        getIdlingResource();

        return rootView;
    }
    @Override public void onAttach(Context context) {
        super.onAttach(context);
        mOnNavigationButtonClickHandler = (NavigationButtonClickHandler) context;
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
            mCurrentStepIndex = getArguments().getInt(Statics.CURRENT_RECIPE_STEP_INDEX);
            mNumberOfSteps = getArguments().getInt(Statics.CURRENT_RECIPE_STEP_COUNT);
        }
        if (mStep!=null) {
            mVideoUrl = mStep.getVideoUrl();
            mThumbnailUrl = mStep.getThumbnailUrl();
            mDescription = mStep.getDescription();
            mShortDescription = mStep.getShortDescription();

            correctPossibleJsonFileErrors();
        }
    }
    private void setupLayoutCharacteristics() {
        if (mDescription!=null) mStepDescriptionTextView.setText(mDescription);
        if (mShortDescription!=null) mStepShortDescriptionTextView.setText(mShortDescription);
        if (mStepVideoView != null) {
            mStepVideoView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_missing_image));

            if (!mVideoUrl.equals("") && internetIsAvailable()) {
                initializeMediaSession();
                initializeVideoPlayer(mVideoUrl);
                setExoPlayerButtonFunctionality();
                mStepVideoView.setVisibility(View.VISIBLE);
                mStepImageView.setVisibility(View.GONE);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE
                        && getResources().getConfiguration().smallestScreenWidthDp < getResources().getInteger(R.integer.tablet_smallest_width_threshold)) {
                    mBottomNavigationView.setVisibility(View.GONE);
                } else {
                    mBottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
            else {
                setStepImage();
                mStepVideoView.setVisibility(View.GONE);
                mStepImageView.setVisibility(View.VISIBLE);
                mBottomNavigationView.setVisibility(View.VISIBLE);
            }
        }
    }
    private void setupBottomNavigationBarCharacteristics() {

        //Set the listener
        mBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_back:
                                returnToRecipesDetails();
                                break;
                            case R.id.action_previous_step:
                                if (mCurrentStepIndex==0) item.setEnabled(false);
                                else {
                                    item.setEnabled(true);
                                    skipToPreviousRecipeStep();
                                }
                                break;
                            case R.id.action_next_step:
                                if (mCurrentStepIndex==mNumberOfSteps-1) item.setEnabled(false);
                                else {
                                    item.setEnabled(true);
                                    skipToNextRecipeStep();
                                }
                                break;
                        }
                        return true;
                    }
                });

        //Set the menu item enabled/disabled on fragment load (if relevant)
        if (mCurrentStepIndex==0) mBottomNavigationView.setSelectedItemId(R.id.action_previous_step);
        else if (mCurrentStepIndex==mNumberOfSteps-1) mBottomNavigationView.setSelectedItemId(R.id.action_next_step);
    }

    //Functional methods - Video player
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
        //mMediaSession.setCallback(new StepVideoSessionCallback());
        mMediaSession.setActive(true);
    }
    private void initializeVideoPlayer(String videoUrl) {

        if (getContext()==null) return;

        // Simulate a fake video download for testing (uncomment for testing)
        //simulateFakeVideoDownload();

        // Setup ExoPlayer
        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        mStepVideoView.setPlayer(mExoPlayer);

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
    private void setExoPlayerButtonFunctionality() {

        //Click listeners
        mExoBackToRecipesListImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToRecipesDetails();
            }
        });
        mExoPrevImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Allow the user to click once on the Prev. button once to restart the video, twice within 2sec to skip to Prev. step
                if (timesClicked == 0) {
                    mExoPlayer.seekTo(0);
                    timesClicked = 1;
                    //Wait a bit
                    new CountDownTimer(getResources().getInteger(R.integer.prev_button_min_time_to_double_click_function_change), 1000) {
                        public void onTick(long millisUntilFinished) {}
                        public void onFinish() {
                            timesClicked = 0;
                        }
                    }.start();
                }
                else if (timesClicked == 1) {
                    timesClicked = 0;
                    if (mCurrentStepIndex > 0) skipToPreviousRecipeStep();
                }
            }
        });
        mExoNextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentStepIndex==mNumberOfSteps-1) {
                    mExoNextImageView.setColorFilter(Color.BLACK);
                } else {
                    mExoNextImageView.setColorFilter(Color.WHITE);
                    skipToNextRecipeStep();
                }
            }
        });

        //Black-out irrelevant buttons
        if (mCurrentStepIndex==mNumberOfSteps-1) mExoNextImageView.setColorFilter(Color.BLACK);
        else mExoNextImageView.setColorFilter(Color.WHITE);
    }

    //Functional methods - General
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
    private void returnToRecipesDetails() {
        mOnNavigationButtonClickHandler.onBackButtonClick();
    }
    private void skipToPreviousRecipeStep() {
        mOnNavigationButtonClickHandler.onPrevStepClick();
    }
    private void skipToNextRecipeStep() {
        mOnNavigationButtonClickHandler.onNextStepClick();
    }
    private boolean internetIsAvailable() {
        if (getContext()==null) return false;
        final ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connMgr==null) return false;
        NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        return (activeNetworkInfo != null) ? true : false;
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

    //Communication with other activites/fragments
    public interface NavigationButtonClickHandler {
        void onBackButtonClick();
        void onPrevStepClick();
        void onNextStepClick();
    }

    //Test methods
    @Nullable private TimeDelayIdlingResource mIdlingResource;
    @VisibleForTesting @NonNull public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new TimeDelayIdlingResource(1000);
        }
        return mIdlingResource;
    }
    @Override public void onVideoDownloaded() {
        //The app reaches this method only on testing
        skipToNextRecipeStep();
    }
    private void simulateFakeVideoDownload() {
        FakeVideoDownloader.downloadVideo(getContext(), StepDetailsFragment.this, mIdlingResource);
    }
}
