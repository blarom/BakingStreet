package com.bakingstreet.data;

import android.content.Context;
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

    private static Map<String, String> createRecipeImagesMap() {
        Map<String,String> myMap = new HashMap<>();
        myMap.put("Nutella Pie", "nutella-pie.jpg");
        myMap.put("Brownies", "brownies.jpg");
        myMap.put("Yellow Cake", "yellow-cake.jpg");
        myMap.put("Cheesecake", "cheesecake.jpg");
        return myMap;
    }
    public static final int TABLET_SMALLEST_WIDTH_THRESHOLD = 600;
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
}
