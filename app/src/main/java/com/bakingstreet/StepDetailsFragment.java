package com.bakingstreet;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bakingstreet.data.Constants;
import com.bakingstreet.data.Recipe;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment {

    private Recipe.Step mStep;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private String mDescription;
    private String mShortDescription;
    @BindView(R.id.step_image) ImageView mStepImageView;
    @BindView(R.id.step_description) TextView mStepDescriptionTextView;
    @BindView(R.id.step_short_description) TextView mStepShortDescriptionTextView;

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

        return rootView;
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
}
