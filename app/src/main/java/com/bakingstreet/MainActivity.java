package com.bakingstreet;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bakingstreet.data.*;

import com.bakingstreet.ui.RecipesRecycleViewAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipesRecycleViewAdapter.RecipesListItemClickHandler {

    @BindView(R.id.recipes_recycler_view) RecyclerView mRecipesRecyclerView;
    List<Recipe> mRecipesList;
    RecipesRecycleViewAdapter mRecipesRecycleViewAdapter;
    private Parcelable mLayoutManagerSavedState;
    private Bundle mBundleRecyclerViewState;

    //Lifecycle methods
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        //if (savedInstanceState!=null) onRestoreInstanceState(savedInstanceState);

        mRecipesList = Statics.getRecipeListFromJson(getApplicationContext());
        updateRecipesWithImages();
        setupRecyclerView();
        showListOfRecipesForSelection();
        saveDataToSharedPreferences();
        updateWidgetData();
        getRecipeSelectionFromWidget();

    }
    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveRecyclerViewState();
        mLayoutManagerSavedState = mRecipesRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(Statics.SAVED_LAYOUT_MANAGER, mLayoutManagerSavedState);

    }
    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        if(savedInstanceState != null) {
//
//            mLayoutManagerSavedState = savedInstanceState.getParcelable(Statics.SAVED_LAYOUT_MANAGER);
//
//            RecipesRecycleViewAdapter recipesRecycleViewAdapter = (RecipesRecycleViewAdapter) mRecipesRecyclerView.getAdapter();
//            if (recipesRecycleViewAdapter == null) {
//
//                mRecipesRecycleViewAdapter = new RecipesRecycleViewAdapter(this, this, mRecipesList);
//                mRecipesRecyclerView.setAdapter(mRecipesRecycleViewAdapter);
//            }
//        }
    }
    @Override protected void onPause() {
        super.onPause();
//        saveRecyclerViewState();
    }
    @Override protected void onResume() {
        super.onResume();
        showListOfRecipesForSelection();
//        if(mBundleRecyclerViewState != null) {
//            mLayoutManagerSavedState = mBundleRecyclerViewState.getParcelable(Statics.SAVED_LAYOUT_MANAGER);
//        }
//        if (mLayoutManagerSavedState != null) {
//            mRecipesRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
//        } else {
//            showListOfRecipesForSelection();
//        }
    }


    //Structural methods
    private void getRecipeSelectionFromWidget() {
        String callingActivity = getIntent().getStringExtra(Statics.CALLING_ACTIVITY);
        if (callingActivity!=null && callingActivity.equals(getString(R.string.activity_name_bakinghelperwidget))) {
            int chosenRecipeIndex = getIntent().getIntExtra(Statics.WIDGET_RECIPE_SELECTION, 0);
            displayChosenRecipe(chosenRecipeIndex);
        }
    }
    private void updateRecipesWithImages() {
        String pathString= "//android_asset/";
        for (int i=0; i<mRecipesList.size(); i++) {
            String image = Statics.recipeImagesMap.get(mRecipesList.get(i).getRecipeName());
            mRecipesList.get(i).setImage(pathString + image);
        }
    }
    private void showListOfRecipesForSelection() {
        mRecipesRecycleViewAdapter.setContents(mRecipesList);
        mRecipesRecycleViewAdapter.notifyDataSetChanged();
        mRecipesRecyclerView.scrollToPosition(0);
    }
    private void setupRecyclerView() {

        int numberOfColumns;
        if (Statics.getSmallestWidth(getApplicationContext()) < Statics.TABLET_SMALLEST_WIDTH_THRESHOLD) {
            numberOfColumns = Statics.NUMBER_GRIDLAYOUT_COLUMNS_PHONE;
        } else numberOfColumns = Statics.NUMBER_GRIDLAYOUT_COLUMNS_TABLET;

        mRecipesRecyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        mRecipesRecycleViewAdapter = new RecipesRecycleViewAdapter(this, this);
        mRecipesRecyclerView.setAdapter(mRecipesRecycleViewAdapter);
    }
    private void saveRecyclerViewState() {
//        mBundleRecyclerViewState = new Bundle();
//        mLayoutManagerSavedState = mRecipesRecyclerView.getLayoutManager().onSaveInstanceState();
//        mBundleRecyclerViewState.putParcelable(Statics.SAVED_LAYOUT_MANAGER, mLayoutManagerSavedState);
    }


    //Functional methods
    @Override
    public void onRecipesListItemClick(int clickedItemIndex) {
        displayChosenRecipe(clickedItemIndex);
    }
    private void displayChosenRecipe(int clickedItemIndex) {
        Recipe selectedRecipe = mRecipesList.get(clickedItemIndex);
        Intent startRecipeDetailsActivityIntent = new Intent(this, RecipeDetailsActivity.class);
        startRecipeDetailsActivityIntent.putExtra(Statics.RECIPE_DETAILS_PARCEL, selectedRecipe);
        startRecipeDetailsActivityIntent.putParcelableArrayListExtra(Statics.RECIPE_INGREDIENTS_PARCEL, selectedRecipe.getIngredients());
        startRecipeDetailsActivityIntent.putParcelableArrayListExtra(Statics.RECIPE_STEPS_PARCEL, selectedRecipe.getSteps());
        startActivity(startRecipeDetailsActivityIntent);
    }
    private void saveDataToSharedPreferences() {

    }
    private void updateWidgetData() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingHelperWidgetProvider.class));
        BakingHelperWidgetProvider.updateBakingHelperWidgets(getApplicationContext(), appWidgetManager, appWidgetIds);
    }
}
