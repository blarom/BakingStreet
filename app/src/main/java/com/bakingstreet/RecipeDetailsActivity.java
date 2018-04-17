package com.bakingstreet;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bakingstreet.data.Constants;
import com.bakingstreet.data.Recipe;
import java.util.ArrayList;

import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.OnStepSelectedListener, StepDetailsFragment.NavigationButtonClickHandler {

    private Recipe mSelectedRecipe;
    private ArrayList<Recipe.Ingredient> mSelectedRecipeIngredients;
    private ArrayList<Recipe.Step> mSelectedRecipeSteps;
    private String mRecipeName;
    private FragmentManager mFragmentManager;
    private int mSelectedStepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        //Initializations
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();

        //Methods
        getRecipeDetails();
        setActionBarTitle();
        setFragmentLayouts();
    }

    //Structural methods
    private void setRecipeDetailsFragment(int viewId) {
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_DETAILS_PARCEL, mSelectedRecipe);
        bundle.putParcelableArrayList(Constants.RECIPE_INGREDIENTS_PARCEL, mSelectedRecipeIngredients);
        bundle.putParcelableArrayList(Constants.RECIPE_STEPS_PARCEL, mSelectedRecipeSteps);
        recipeDetailsFragment.setArguments(bundle);
        fragmentTransaction.replace(viewId, recipeDetailsFragment);
        fragmentTransaction.commit();
    }
    private void setStepDetailsFragment(int viewId, int selectedStepIndex) {
        StepDetailsFragment recipeDetailsFragment = new StepDetailsFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.STEP_DETAILS_PARCEL, mSelectedRecipeSteps.get(selectedStepIndex));
        recipeDetailsFragment.setArguments(bundle);
        fragmentTransaction.replace(viewId, recipeDetailsFragment);
        fragmentTransaction.commit();
    }
    private void getRecipeDetails() {
        mSelectedRecipe = getIntent().getParcelableExtra(Constants.RECIPE_DETAILS_PARCEL);
        mSelectedRecipeIngredients = getIntent().getParcelableArrayListExtra(Constants.RECIPE_INGREDIENTS_PARCEL);
        mSelectedRecipeSteps = getIntent().getParcelableArrayListExtra(Constants.RECIPE_STEPS_PARCEL);
        mRecipeName = mSelectedRecipe.getRecipeName();
    }
    private void setActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (mRecipeName!=null) actionBar.setTitle(mRecipeName);
        }
    }
    private void setFragmentLayouts() {
        if (Constants.getSmallestWidth(getApplicationContext()) < Constants.TABLET_SMALLEST_WIDTH_THRESHOLD) {
            setRecipeDetailsFragment(R.id.recipe_details_fragment_container);
        } else {
            setRecipeDetailsFragment(R.id.recipe_details_fragment_container);
            setStepDetailsFragment(R.id.recipe_step_fragment_container, mSelectedStepIndex);
        }
    }

    //Functional methods
    @Override
    public void onStepSelected(int selectedStepIndex) {
        mSelectedStepIndex = selectedStepIndex;
        setStepDetailsFragmentDependingOnScreenSize();
    }

    @Override
    public void onBackButtonClick() {
        setRecipeDetailsFragment(R.id.recipe_details_fragment_container);
    }
    @Override
    public void onPrevStepClick() {
        if (mSelectedStepIndex > 0) mSelectedStepIndex--;
        else mSelectedStepIndex = 0;
        setStepDetailsFragmentDependingOnScreenSize();
    }
    @Override
    public void onNextStepClick() {
        if (mSelectedStepIndex < mSelectedRecipeSteps.size()-1) mSelectedStepIndex++;
        else mSelectedStepIndex = mSelectedRecipeSteps.size()-1;
        setStepDetailsFragmentDependingOnScreenSize();
    }
    private void setStepDetailsFragmentDependingOnScreenSize() {
        if (Constants.getSmallestWidth(getApplicationContext()) < Constants.TABLET_SMALLEST_WIDTH_THRESHOLD) {
            setStepDetailsFragment(R.id.recipe_details_fragment_container, mSelectedStepIndex);
        } else {
            setStepDetailsFragment(R.id.recipe_step_fragment_container, mSelectedStepIndex);
        }
    }
}
