package com.bakingstreet;

import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bakingstreet.data.Constants;
import com.bakingstreet.data.Recipe;
import com.bakingstreet.ui.IngredientsRecycleViewAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailsActivity extends AppCompatActivity {

    private IngredientsRecycleViewAdapter mIngredientsRecycleViewAdapter;
    @BindView(R.id.recipe_ingredients_recycler_view) RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.recipe_image) ImageView mRecipeImageView;
    private Recipe.Ingredients[] mIngredients;
    private Recipe.Steps[] mSteps;
    private String mImageString;
    private String mRecipeName;
    private int mServings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ButterKnife.bind(this);

        getRecipeDetails();
        setupCollapsingActionBarProperties();
        showListOfIngredients();
    }


    //Structural methods
    private void getRecipeDetails() {
        Recipe selectedRecipe = getIntent().getParcelableExtra(Constants.RECIPE_DETAILS_PARCEL);
        Parcelable[] selectedRecipeIngredients = getIntent().getParcelableExtra(Constants.RECIPE_INGREDIENTS_PARCEL);
        Parcelable[] selectedRecipeSteps = getIntent().getParcelableExtra(Constants.RECIPE_STEPS_PARCEL);
        mImageString = selectedRecipe.getImage();
        mRecipeName = selectedRecipe.getRecipeName();
        mServings = selectedRecipe.getServings();
        mIngredients = (Recipe.Ingredients[]) selectedRecipeIngredients;
        mSteps = (Recipe.Steps[]) selectedRecipeSteps;
    }
    private void setupCollapsingActionBarProperties() {
        //Note: new settings in styles.xml/windowActionBar & windowNoTitle
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if (mRecipeName!=null) actionBar.setTitle(mRecipeName);

        }
        if (mImageString!=null) {
            Uri imageUri = Uri.fromFile(new File(mImageString));
            Picasso.with(getApplicationContext())
                    .load(imageUri)
                    .error(R.drawable.ic_missing_image)
                    .into(mRecipeImageView);
        }
    }
    private void showListOfIngredients() {
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mIngredientsRecycleViewAdapter = new IngredientsRecycleViewAdapter(this, mIngredients);
        mIngredientsRecycleViewAdapter.setContents(mIngredients);
        mIngredientsRecyclerView.setAdapter(mIngredientsRecycleViewAdapter);
    }
}
