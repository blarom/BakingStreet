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

    public static void updateBakingHelperWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_helper_widget);

        //Update the RemoteViews ingredients list
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.recipe_ingredients, intent);
        views.setEmptyView(R.id.recipe_ingredients, R.id.empty_view);

        //Update the Recipe names list and set click listener
        updateWidgetRecipeNames(context, views, appWidgetId);
        setRecipeNameClickListener(context, views);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void updateWidgetRecipeNames(Context context, RemoteViews views, int appWidgetId) {
        List<Recipe> recipesList = Statics.getRecipeListFromJson(context);
        if (recipesList != null) {
            views.setTextViewText(R.id.recipes, recipesList.get(0).getRecipeName());
        }
        else {
            views.setTextViewText(R.id.recipes, "Default Recipe 1");
        }
    }

    private static void setRecipeNameClickListener(Context context, RemoteViews views) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Statics.WIDGET_RECIPE_SELECTION, 0);
        intent.putExtra(Statics.CALLING_ACTIVITY, context.getString(R.string.activity_name_bakinghelperwidget));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.recipes, pendingIntent);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

