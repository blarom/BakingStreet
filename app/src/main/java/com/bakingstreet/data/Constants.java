package com.bakingstreet.data;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final String RECIPE_DETAILS_PARCEL = "recipe_details_parcel";
    public static final String RECIPE_INGREDIENTS_PARCEL = "recipe_ingredients_parcel";
    public static final String RECIPE_STEPS_PARCEL = "recipe_steps_parcel";
    public static final String STEP_DETAILS_PARCEL = "step_details_parcel";
    public static final Map<String, String> recipeImagesMap = createRecipeImagesMap();
    private static Map<String, String> createRecipeImagesMap() {
        Map<String,String> myMap = new HashMap<>();
        myMap.put("Nutella Pie", "nutella-pie.jpg");
        myMap.put("Brownies", "brownies.jpg");
        myMap.put("Yellow Cake", "yellow-cake.jpg");
        myMap.put("Cheesecake", "cheesecake.jpg");
        return myMap;
    }
}
