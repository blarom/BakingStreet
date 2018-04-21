package com.bakingstreet;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.bakingstreet.data.Statics;
import com.bakingstreet.data.Recipe;

import java.util.List;

public class BakingHelperWidgetProvider extends AppWidgetProvider {

    //Static methods
    public static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_helper_widget);

        updateWidgetIngredientsList(context, views);
        int recipeIndex = Statics.restoreChosenRecipeIndex(context);
        updateWidgetRecipeName(context, views, recipeIndex);
        setWidgetRecipeNameClickListener(context, views, recipeIndex);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    private static void updateWidgetIngredientsList(Context context, RemoteViews views) {
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.recipe_ingredients, intent);
        views.setEmptyView(R.id.recipe_ingredients, R.id.empty_view);
    }
    private static void updateWidgetRecipeName(Context context, RemoteViews views, int recipeIndex) {
        List<Recipe> recipesList = Statics.getRecipeListFromJson(context);

        if (recipesList != null) {
            views.setTextViewText(R.id.recipes, recipesList.get(recipeIndex).getRecipeName());
        }
        else {
            views.setTextViewText(R.id.recipes, context.getString(R.string.no_recipes_available));
        }
    }
    private static void setWidgetRecipeNameClickListener(Context context, RemoteViews views, int recipeIndex) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Statics.WIDGET_RECIPE_SELECTION, recipeIndex);
        intent.putExtra(Statics.CALLING_ACTIVITY, context.getString(R.string.activity_name_bakinghelperwidget));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.recipes, pendingIntent);

    }

    //Widget methods
    @Override public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }
    @Override public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

