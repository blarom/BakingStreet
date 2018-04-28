package com.bakingstreet;

import com.bakingstreet.data.Statics;
import com.bakingstreet.data.Recipe;
import com.bakingstreet.ui.IngredientsRecycleViewAdapter;
import com.bakingstreet.ui.StepsRecycleViewAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsFragment extends Fragment implements
        StepsRecycleViewAdapter.StepsListItemClickHandler {

    @BindView(R.id.recipe_details_fragment_container) LinearLayout mRecipeDetailsFragmentContainer;
    @BindView(R.id.recipe_ingredients_recycler_view) RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.recipe_steps_recycler_view) RecyclerView mStepsRecyclerView;
    private Recipe mSelectedRecipe;
    private List<Recipe.Ingredient> mIngredients;
    private List<Recipe.Step> mSteps;
    private String mImageString;
    private String mRecipeName;
    private int mServings;
    private IngredientsRecycleViewAdapter mIngredientsRecycleViewAdapter;
    private StepsRecycleViewAdapter mStepsRecycleViewAdapter;
    private OnStepSelectedListener onStepSelectedListener;
    private int mSelectedStepIndex;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Initializing layout parameters
        View rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, rootView);

        //Methods
        getRecipeDetails();
        setupListOfIngredients();
        setupListOfSteps();

        return rootView;
    }
    @Override public void onAttach(Context context) {
        super.onAttach(context);
        onStepSelectedListener = (OnStepSelectedListener) context;
    }

    //Structural methods
    private void getRecipeDetails() {

        if (getArguments() != null) {
            mSelectedRecipe = getArguments().getParcelable(Statics.RECIPE_DETAILS_PARCEL);
            mIngredients = getArguments().getParcelableArrayList(Statics.RECIPE_INGREDIENTS_PARCEL);
            mSteps = getArguments().getParcelableArrayList(Statics.RECIPE_STEPS_PARCEL);
            mSelectedStepIndex = getArguments().getInt(Statics.CURRENT_RECIPE_STEP_INDEX);
        }
        if (mSelectedRecipe!=null) {
            mImageString = mSelectedRecipe.getImage();
            mRecipeName = mSelectedRecipe.getRecipeName();
            mServings = mSelectedRecipe.getServings();
        }
    }
    private void setupListOfIngredients() {
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mIngredientsRecycleViewAdapter = new IngredientsRecycleViewAdapter(getContext(), mIngredients);
        mIngredientsRecyclerView.setAdapter(mIngredientsRecycleViewAdapter);
    }
    private void setupListOfSteps() {
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (mStepsRecycleViewAdapter==null) mStepsRecycleViewAdapter = new StepsRecycleViewAdapter(getContext(), this, mSteps);
        mStepsRecyclerView.setAdapter(mStepsRecycleViewAdapter);
        mStepsRecycleViewAdapter.setSelectedStep(mSelectedStepIndex);
    }

    //Functional methods
    public void updateStepIndicator(int selectedStepIndex) {
        mSelectedStepIndex = selectedStepIndex;
        if (mStepsRecycleViewAdapter!=null) mStepsRecycleViewAdapter.setSelectedStep(mSelectedStepIndex);
    }
    public interface OnStepSelectedListener {
        void onStepSelected(int clickedItemIndex);
    }

    //Communication with other activites/fragments
    @Override public void onStepsListItemClick(int clickedItemIndex) {
        mSelectedStepIndex = clickedItemIndex;
        mStepsRecycleViewAdapter.setSelectedStep(clickedItemIndex);
        onStepSelectedListener.onStepSelected(clickedItemIndex);
    }
}
