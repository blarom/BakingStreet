package com.bakingstreet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bakingstreet.data.*;

import com.bakingstreet.ui.RecipesRecycleViewAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipesRecycleViewAdapter.RecipesListItemClickHandler {

    @BindView(R.id.recipes_recycler_view) RecyclerView mRecipesRecyclerView;
    Recipe[] mRecipesList;
    RecipesRecycleViewAdapter mRecipesRecycleViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        getRecipeListFromJson();
        updateRecipesWithImages();
        showListOfRecipesForSelection();

    }

    //Structural methods
    private void getRecipeListFromJson() {
        String recipesJson = readJsonFromAsset("recipes.json");
        Gson recipesGson = new Gson();
        mRecipesList = recipesGson.fromJson(recipesJson, Recipe[].class);
    }
    private void updateRecipesWithImages() {
        String pathString= "//android_asset/";
        for (int i=0; i<mRecipesList.length; i++) {
            String image = Constants.recipeImagesMap.get(mRecipesList[i].getRecipeName());
            mRecipesList[i].setImage(pathString + image);
        }
    }
    private void showListOfRecipesForSelection() {
        int numberOfColumns = 1;
        mRecipesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecipesRecycleViewAdapter = new RecipesRecycleViewAdapter(this, this, mRecipesList);
        mRecipesRecycleViewAdapter.setContents(mRecipesList);
        mRecipesRecyclerView.setAdapter(mRecipesRecycleViewAdapter);
    }

    //Functional methods
    public String readJsonFromAsset(String asset) {
        String json = null;
        try {
            InputStream is = getAssets().open(asset);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onRecipesListItemClick(int clickedItemIndex) {
        Recipe selectedRecipe = mRecipesList[clickedItemIndex];
        Intent startRecipeDetailsActivityIntent = new Intent(this, RecipeDetailsActivity.class);
        startRecipeDetailsActivityIntent.putExtra(Constants.RECIPE_DETAILS_PARCEL, selectedRecipe);
        startRecipeDetailsActivityIntent.putParcelableArrayListExtra(Constants.RECIPE_INGREDIENTS_PARCEL, selectedRecipe.getIngredients());
        startRecipeDetailsActivityIntent.putParcelableArrayListExtra(Constants.RECIPE_STEPS_PARCEL, selectedRecipe.getSteps());
        startActivity(startRecipeDetailsActivityIntent);
    }
}
