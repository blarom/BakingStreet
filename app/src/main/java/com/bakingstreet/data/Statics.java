package com.bakingstreet.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statics {

    public static final String RECIPE_DETAILS_PARCEL = "recipe_details_parcel";
    public static final String RECIPE_INGREDIENTS_PARCEL = "recipe_ingredients_parcel";
    public static final String RECIPE_STEPS_PARCEL = "recipe_steps_parcel";
    public static final String STEP_DETAILS_PARCEL = "step_details_parcel";
    public static final Map<String, String> recipeImagesMap = createRecipeImagesMap();
    public static final int NUMBER_GRIDLAYOUT_COLUMNS_PHONE = 1;
    public static final int NUMBER_GRIDLAYOUT_COLUMNS_TABLET = 3;
    public static final String SAVED_LAYOUT_MANAGER = "saved_layout_manager";
    public static final String SAVED_RECIPES_LIST = "saved_recipes_list";
    public static final String WIDGET_RECIPE_SELECTION = "widget_recipe_selection";
    public static final String CALLING_ACTIVITY = "calling_activity";
    public static final String CHOSEN_RECIPE_INDEX = "chosen_recipe_index";
    public static final String RECIPE_PREFS = "recipe_prefs";
    public static final String ACTION_UPDATE_WIDGET_LIST = "acton_update_widget_list";
    public static final String RECIPES_RECYCLERVIEW_POSITION = "recipes_recycler_view_position";
    public static final int RECIPE_DETAILS_ACTIVITY_CODE = 123;
    public static final String CURRENT_RECIPE_STEP_INDEX = "current_recipe_step_index";
    public static final String CURRENT_RECIPE_STEP_COUNT = "current_recipe_step_number";
    public static final String SELECTED_STEP_INDEX = "selected_step_index";

    private static Map<String, String> createRecipeImagesMap() {
        Map<String,String> myMap = new HashMap<>();
        myMap.put("Nutella Pie", "nutella-pie.jpg");
        myMap.put("Brownies", "brownies.jpg");
        myMap.put("Yellow Cake", "yellow-cake.jpg");
        myMap.put("Cheesecake", "cheesecake.jpg");
        return myMap;
    }
    public static int getSmallestWidth(Context context) {
        Configuration config = context.getResources().getConfiguration();
        return config.smallestScreenWidthDp;
    }
    public static List<Recipe> getRecipeListFromJson(Context context) {
        String recipesJson = readJsonFromAsset("recipes.json", context);
        Gson recipesGson = new Gson();
        return recipesGson.fromJson(recipesJson, new TypeToken<List<Recipe>>(){}.getType());
    }
    private static String readJsonFromAsset(String asset, Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(asset);
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

    public static int restoreChosenRecipeIndex(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(Statics.RECIPE_PREFS, Context.MODE_PRIVATE);
        return sharedPref.getInt(Statics.CHOSEN_RECIPE_INDEX, 0);
    }
}
