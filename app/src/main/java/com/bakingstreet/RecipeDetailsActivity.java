package com.bakingstreet;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.bakingstreet.data.Statics;
import com.bakingstreet.data.Recipe;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity implements
        RecipeDetailsFragment.OnStepSelectedListener,
        StepDetailsFragment.NavigationButtonClickHandler,
        StepDetailsFragment.OnPlayerButtonClickListener{

    private Recipe mSelectedRecipe;
    private ArrayList<Recipe.Ingredient> mSelectedRecipeIngredients;
    private ArrayList<Recipe.Step> mSelectedRecipeSteps;
    private String mRecipeName;
    private FragmentManager mFragmentManager;
    private int mSelectedStepIndex;
    private int mRecipesListPosition;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        //Initializations
        ButterKnife.bind(this);
        mFragmentManager = getSupportFragmentManager();

        //Methods
        getExtras();
        setActionBarTitle();
        if(savedInstanceState == null) setFragmentLayouts();
    }
    @Override public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(Statics.RECIPES_RECYCLERVIEW_POSITION, mRecipesListPosition);
        setResult(RESULT_OK, intent);
        //super.onBackPressed();
        finish();
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    //Structural methods
    private void setRecipeDetailsFragment(int viewId) {
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Statics.RECIPE_DETAILS_PARCEL, mSelectedRecipe);
        bundle.putParcelableArrayList(Statics.RECIPE_INGREDIENTS_PARCEL, mSelectedRecipeIngredients);
        bundle.putParcelableArrayList(Statics.RECIPE_STEPS_PARCEL, mSelectedRecipeSteps);
        recipeDetailsFragment.setArguments(bundle);
        fragmentTransaction.replace(viewId, recipeDetailsFragment);
        fragmentTransaction.commit();
    }
    private void setStepDetailsFragment(int viewId, int selectedStepIndex) {
        StepDetailsFragment recipeDetailsFragment = new StepDetailsFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Statics.STEP_DETAILS_PARCEL, mSelectedRecipeSteps.get(selectedStepIndex));
        bundle.putParcelable(Statics.RECIPE_DETAILS_PARCEL, mSelectedRecipe);
        recipeDetailsFragment.setArguments(bundle);
        fragmentTransaction.replace(viewId, recipeDetailsFragment);
        fragmentTransaction.commit();
    }
    private void getExtras() {
        Intent intent = getIntent();
        if (getIntent().hasExtra(Statics.RECIPES_RECYCLERVIEW_POSITION)) {
            mRecipesListPosition = intent.getIntExtra(Statics.RECIPES_RECYCLERVIEW_POSITION,0);
        }
        if (getIntent().hasExtra(Statics.RECIPE_DETAILS_PARCEL)) {
            mSelectedRecipe = intent.getParcelableExtra(Statics.RECIPE_DETAILS_PARCEL);
            mRecipeName = mSelectedRecipe.getRecipeName();
        }
        if (getIntent().hasExtra(Statics.RECIPE_INGREDIENTS_PARCEL)) {
            mSelectedRecipeIngredients = intent.getParcelableArrayListExtra(Statics.RECIPE_INGREDIENTS_PARCEL);
        }
        if (getIntent().hasExtra(Statics.RECIPE_STEPS_PARCEL)) {
            mSelectedRecipeSteps = intent.getParcelableArrayListExtra(Statics.RECIPE_STEPS_PARCEL);
        }
    }
    private void setActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (mRecipeName!=null) actionBar.setTitle(mRecipeName);
        }
    }
    private void setFragmentLayouts() {
        if (Statics.getSmallestWidth(getApplicationContext()) < Statics.TABLET_SMALLEST_WIDTH_THRESHOLD) {
            setRecipeDetailsFragment(R.id.recipe_details_fragment_container);
        } else {
            setRecipeDetailsFragment(R.id.recipe_details_fragment_container);
            setStepDetailsFragment(R.id.recipe_step_fragment_container, mSelectedStepIndex);
        }
    }

    //Functional methods
    @Override public void onStepSelected(int selectedStepIndex) {
        mSelectedStepIndex = selectedStepIndex;
        loadStepDetailsFragmentDependingOnScreenSize();
    }
    @Override public void onBackButtonClick() {
        setRecipeDetailsFragment(R.id.recipe_details_fragment_container);
    }
    @Override public void onPrevStepClick() {
        loadPreviousStepFragment();
    }
    @Override public void onNextStepClick() {
        loadNextStepFragment();
    }
    @Override public void onSkipToPreviousClicked() {
        loadPreviousStepFragment();
    }
    @Override public void onSkipToNextClicked() {
        loadNextStepFragment();
    }
    private void loadPreviousStepFragment() {
        if (mSelectedStepIndex > 0) mSelectedStepIndex--;
        else mSelectedStepIndex = 0;
        loadStepDetailsFragmentDependingOnScreenSize();
    }
    private void loadNextStepFragment() {
        if (mSelectedStepIndex < mSelectedRecipeSteps.size()-1) mSelectedStepIndex++;
        else mSelectedStepIndex = mSelectedRecipeSteps.size()-1;
        loadStepDetailsFragmentDependingOnScreenSize();
    }
    private void loadStepDetailsFragmentDependingOnScreenSize() {
        if (Statics.getSmallestWidth(getApplicationContext()) < Statics.TABLET_SMALLEST_WIDTH_THRESHOLD) {
            setStepDetailsFragment(R.id.recipe_details_fragment_container, mSelectedStepIndex);
        } else {
            setStepDetailsFragment(R.id.recipe_step_fragment_container, mSelectedStepIndex);
        }
    }
}
