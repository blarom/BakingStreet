package com.bakingstreet.data;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final String RECIPE_DETAILS_PARCEL = "recipe_details_parcel";
    public static final String RECIPE_INGREDIENTS_PARCEL = "recipe_ingredients_parcel";
    public static final String RECIPE_STEPS_PARCEL = "recipe_steps_parcel";
    public static final String STEP_DETAILS_PARCEL = "step_details_parcel";
    public static final Map<String, String> recipeImagesMap = createRecipeImagesMap();
    public static final int NUMBER_GRIDLAYOUT_COLUMNS_PHONE = 1;
    public static final int NUMBER_GRIDLAYOUT_COLUMNS_TABLET = 3;
    public static final String SAVED_LAYOUT_MANAGER = "saved_layout_manager";

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
}
