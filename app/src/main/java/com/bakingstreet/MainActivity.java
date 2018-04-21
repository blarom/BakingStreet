package com.bakingstreet;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bakingstreet.data.*;

import com.bakingstreet.ui.RecipesRecycleViewAdapter;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipesRecycleViewAdapter.RecipesListItemClickHandler {

    @BindView(R.id.recipes_recycler_view) RecyclerView mRecipesRecyclerView;
    List<Recipe> mRecipesList;
    RecipesRecycleViewAdapter mRecipesRecycleViewAdapter;
    private Parcelable mLayoutManagerSavedState;
    private Bundle mBundleRecyclerViewState;
    private int mChosenRecipeIndex;
    private int mStoredRecyclerViewPosition;

    //Lifecycle methods
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        //if(savedInstanceState != null) onRestoreInstanceState(savedInstanceState);

        mRecipesList = Statics.getRecipeListFromJson(getApplicationContext());
        restoreChosenRecipeIndex();
        updateRecipesWithImages();
        setupRecyclerView();
        showListOfRecipesForSelection();
        updateWidgetData();
        getRecipeSelectionFromWidget();

    }
    @Override protected void onSaveInstanceState(Bundle outState) {
        mStoredRecyclerViewPosition = getRecipesRecyclerViewPosition();
        saveRecipesRecyclerViewState(outState);
        super.onSaveInstanceState(outState);

    }

    private int getRecipesRecyclerViewPosition() {
        GridLayoutManager layoutManager = ((GridLayoutManager) mRecipesRecyclerView.getLayoutManager());
        return layoutManager.findFirstVisibleItemPosition();
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            restoreRecipesRecyclerViewState(savedInstanceState);
        }
    }
    @Override protected void onPause() {
        super.onPause();
        //mBundleRecyclerViewState = new Bundle();
        //saveRecipesRecyclerViewState(mBundleRecyclerViewState);
    }
    @Override protected void onResume() {
        super.onResume();
        restoreChosenRecipeIndex();
        //restoreRecipesRecyclerViewState(mBundleRecyclerViewState);
        showListOfRecipesForSelection();
        getRecipeSelectionFromWidget();
    }
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Statics.RECIPE_DETAILS_ACTIVITY_CODE) {
            if(resultCode == RESULT_OK) {
                mStoredRecyclerViewPosition = data.getIntExtra(Statics.RECIPES_RECYCLERVIEW_POSITION, 0);
                mRecipesRecyclerView.scrollToPosition(mStoredRecyclerViewPosition);
            }
        }
    }


    //Structural methods
    private void getRecipeSelectionFromWidget() {
        String callingActivity = getIntent().getStringExtra(Statics.CALLING_ACTIVITY);
        if (callingActivity!=null && callingActivity.equals(getString(R.string.activity_name_bakinghelperwidget))) {
            displayChosenRecipe(mChosenRecipeIndex);
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
        mRecipesRecyclerView.scrollToPosition(mStoredRecyclerViewPosition);
    }
    private void restoreRecipesRecyclerViewState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            mLayoutManagerSavedState = savedInstanceState.getParcelable(Statics.SAVED_LAYOUT_MANAGER);
        }
        if (savedInstanceState != null) {
            mRecipesRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
            mStoredRecyclerViewPosition = savedInstanceState.getInt(Statics.RECIPES_RECYCLERVIEW_POSITION);
            mRecipesRecyclerView.scrollToPosition(mStoredRecyclerViewPosition);
        } else {
            showListOfRecipesForSelection();
        }
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
    private void saveRecipesRecyclerViewState(Bundle savedInstanceState) {
        mLayoutManagerSavedState = mRecipesRecyclerView.getLayoutManager().onSaveInstanceState();
        savedInstanceState.putInt(Statics.RECIPES_RECYCLERVIEW_POSITION, mStoredRecyclerViewPosition);
        savedInstanceState.putParcelable(Statics.SAVED_LAYOUT_MANAGER, mLayoutManagerSavedState);
    }


    //Functional methods
    private void displayChosenRecipe(int clickedItemIndex) {
        Recipe selectedRecipe = mRecipesList.get(clickedItemIndex);
        Intent startRecipeDetailsActivityIntent = new Intent(this, RecipeDetailsActivity.class);

        startRecipeDetailsActivityIntent.putExtra(Statics.RECIPES_RECYCLERVIEW_POSITION, getRecipesRecyclerViewPosition());
        startRecipeDetailsActivityIntent.putExtra(Statics.RECIPE_DETAILS_PARCEL, selectedRecipe);
        startRecipeDetailsActivityIntent.putParcelableArrayListExtra(Statics.RECIPE_INGREDIENTS_PARCEL, selectedRecipe.getIngredients());
        startRecipeDetailsActivityIntent.putParcelableArrayListExtra(Statics.RECIPE_STEPS_PARCEL, selectedRecipe.getSteps());
        startActivityForResult(startRecipeDetailsActivityIntent, Statics.RECIPE_DETAILS_ACTIVITY_CODE);
    }
    @Override public void onRecipesListItemClick(int clickedItemIndex) {
        saveChosenRecipeIndex(clickedItemIndex);
        updateWidgetData();
        displayChosenRecipe(clickedItemIndex);
    }
    private void restoreChosenRecipeIndex() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Statics.RECIPE_PREFS, Context.MODE_PRIVATE);
        mChosenRecipeIndex = sharedPref.getInt(Statics.CHOSEN_RECIPE_INDEX, 0);
    }
    private void saveChosenRecipeIndex(int clickedItemIndex) {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Statics.RECIPE_PREFS, Context.MODE_PRIVATE);

        //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(Statics.CHOSEN_RECIPE_INDEX, clickedItemIndex);
        editor.apply();
    }
    private void updateWidgetData() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingHelperWidgetProvider.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.recipe_ingredients);
        BakingHelperWidgetProvider.updateAppWidgets(getApplicationContext(), appWidgetManager, appWidgetIds);
    }
}
