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

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.OnStepSelectedListener {

    private Recipe mSelectedRecipe;
    private ArrayList<Recipe.Ingredient> mSelectedRecipeIngredients;
    private ArrayList<Recipe.Step> mSelectedRecipeSteps;
    private String mRecipeName;
    private FragmentManager mFragmentManager;
    private Recipe.Step mSelectedStep;

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
        activateRecipeDetailsFragment();
    }

    //Structural methods
    private void activateRecipeDetailsFragment() {
        RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE_DETAILS_PARCEL, mSelectedRecipe);
        bundle.putParcelableArrayList(Constants.RECIPE_INGREDIENTS_PARCEL, mSelectedRecipeIngredients);
        bundle.putParcelableArrayList(Constants.RECIPE_STEPS_PARCEL, mSelectedRecipeSteps);
        recipeDetailsFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.recipe_details_fragment_container, recipeDetailsFragment);
        fragmentTransaction.commit();
    }
    private void activateStepDetailsFragment() {
        StepDetailsFragment recipeDetailsFragment = new StepDetailsFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.STEP_DETAILS_PARCEL, mSelectedStep);
        recipeDetailsFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.recipe_details_fragment_container, recipeDetailsFragment);
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

    @Override
    public void onStepSelected(Recipe.Step selectedStep) {
        mSelectedStep = selectedStep;
        activateStepDetailsFragment();
    }
}
