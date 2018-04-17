package com.bakingstreet;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bakingstreet.data.Constants;
import com.bakingstreet.data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment {

    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    private Recipe.Step mStep;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private String mDescription;
    private String mShortDescription;
    @BindView(R.id.step_image) ImageView mStepImageView;
    @BindView(R.id.step_description) TextView mStepDescriptionTextView;
    @BindView(R.id.step_short_description) TextView mStepShortDescriptionTextView;
    NavigationButtonClickHandler mOnNavigationButtonClickHandler;

    public StepDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
    }

    //Functional methods
    public interface NavigationButtonClickHandler {
        void onBackButtonClick();
        void onPrevStepClick();
        void onNextStepClick();
    }

}
