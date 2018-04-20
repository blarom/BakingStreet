package com.bakingstreet;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.bakingstreet.data.Statics;
import com.bakingstreet.data.Recipe;
import com.bakingstreet.ui.IngredientsRecycleViewAdapter;
import com.bakingstreet.ui.StepsRecycleViewAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsFragment extends Fragment implements StepsRecycleViewAdapter.StepsListItemClickHandler {

    private Recipe mSelectedRecipe;
    @BindView(R.id.recipe_details_fragment_container) ScrollView mRecipeDetailsFragmentContainer;
    @BindView(R.id.recipe_ingredients_recycler_view) RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.recipe_steps_recycler_view) RecyclerView mStepsRecyclerView;
    @BindView(R.id.recipe_image) ImageView mRecipeImageView;
    private List<Recipe.Ingredient> mIngredients;
    private List<Recipe.Step> mSteps;
    private String mImageString;
    private String mRecipeName;
    private int mServings;
    private IngredientsRecycleViewAdapter mIngredientsRecycleViewAdapter;
    private StepsRecycleViewAdapter mStepsRecycleViewAdapter;
    private Context mContext;
    private OnStepSelectedListener onStepSelectedListener;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Initializing layout parameters
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, rootView);

        //Methods
        getRecipeDetails();
        setupLayoutValues();
        setupListOfIngredients();
        setupListOfSteps();

        return rootView;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        onStepSelectedListener = (OnStepSelectedListener) context;
    }

    //Structural methods
    private void getRecipeDetails() {

        if (getArguments() != null) {
            mSelectedRecipe = getArguments().getParcelable(Statics.RECIPE_DETAILS_PARCEL);
            mIngredients = getArguments().getParcelableArrayList(Statics.RECIPE_INGREDIENTS_PARCEL);
            mSteps = getArguments().getParcelableArrayList(Statics.RECIPE_STEPS_PARCEL);
        }
        if (mSelectedRecipe!=null) {
            mImageString = mSelectedRecipe.getImage();
            mRecipeName = mSelectedRecipe.getRecipeName();
            mServings = mSelectedRecipe.getServings();
        }
    }
    private void setupLayoutValues() {
        if (mImageString!=null) {
            Uri imageUri = Uri.fromFile(new File(mImageString));
//            Picasso.with(getContext())
//                    .load(imageUri)
//                    .error(R.drawable.ic_missing_image)
//                    .into(mRecipeImageView);

            Picasso.with(getContext())
                    .load(imageUri)
                    .error(R.drawable.ic_missing_image)
                    .into(mRecipeImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            //Adapted from github\blarom\PopularMovies
                            Drawable drawable = mRecipeImageView.getDrawable();
                            int drawableWidth = drawable.getIntrinsicWidth();
                            int drawableHeight = drawable.getIntrinsicHeight();

                            int containerWidth = mRecipeDetailsFragmentContainer.getWidth();

                            //Set the imageView container height to match the image dimensions
                            int maxImageViewHeight = (int) (((float) drawableHeight) / ((float) drawableWidth) * ((float) containerWidth));
                            mRecipeImageView.setMaxHeight(maxImageViewHeight);
                            mRecipeImageView.requestLayout();
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }
    }
    private void setupListOfIngredients() {
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mIngredientsRecycleViewAdapter = new IngredientsRecycleViewAdapter(getContext(), mIngredients);
        mIngredientsRecyclerView.setAdapter(mIngredientsRecycleViewAdapter);
    }
    private void setupListOfSteps() {
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mStepsRecycleViewAdapter = new StepsRecycleViewAdapter(getContext(), this, mSteps);
        mStepsRecyclerView.setAdapter(mStepsRecycleViewAdapter);
    }

    //Functional methods
    @Override
    public void onStepsListItemClick(int clickedItemIndex) {
        onStepSelectedListener.onStepSelected(clickedItemIndex);
    }
    public interface OnStepSelectedListener {
        void onStepSelected(int clickedItemIndex);
    }
}
