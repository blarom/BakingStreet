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

    public static void updateBakingHelperWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static void updateWidgetRecipeNames(Context context, RemoteViews views, int appWidgetId) {
        List<Recipe> recipesList = Statics.getRecipeListFromJson(context);
        if (recipesList != null) {
            views.setTextViewText(R.id.recipes, recipesList.get(0).getRecipeName());
            views.setTextViewText(R.id.widget_recipe_name1, recipesList.get(1).getRecipeName());
            views.setTextViewText(R.id.widget_recipe_name2, recipesList.get(2).getRecipeName());
            views.setTextViewText(R.id.widget_recipe_name3, recipesList.get(3).getRecipeName());
        }
        else {
            views.setTextViewText(R.id.widget_recipe_name, "Default Recipe 1");
            views.setTextViewText(R.id.widget_recipe_name1, "Default Recipe 2");
            views.setTextViewText(R.id.widget_recipe_name2, "Default Recipe 3");
            views.setTextViewText(R.id.widget_recipe_name3, "Default Recipe 4");
        }
    }

    private static void setRecipeNameClickListener(Context context, RemoteViews views) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Statics.WIDGET_RECIPE_SELECTION, 0);
        intent.putExtra(Statics.CALLING_ACTIVITY, context.getString(R.string.activity_name_bakinghelperwidget));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);

        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.putExtra(Statics.WIDGET_RECIPE_SELECTION, 1);
        intent1.putExtra(Statics.CALLING_ACTIVITY, context.getString(R.string.activity_name_bakinghelperwidget));
        PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);
        views.setOnClickPendingIntent(R.id.widget_recipe_name1, pendingIntent1);

        Intent intent2 = new Intent(context, MainActivity.class);
        intent2.putExtra(Statics.WIDGET_RECIPE_SELECTION, 2);
        intent2.putExtra(Statics.CALLING_ACTIVITY, context.getString(R.string.activity_name_bakinghelperwidget));
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
        views.setOnClickPendingIntent(R.id.widget_recipe_name2, pendingIntent2);

        Intent intent3 = new Intent(context, MainActivity.class);
        intent3.putExtra(Statics.WIDGET_RECIPE_SELECTION, 3);
        intent3.putExtra(Statics.CALLING_ACTIVITY, context.getString(R.string.activity_name_bakinghelperwidget));
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent3, 0);
        views.setOnClickPendingIntent(R.id.widget_recipe_name3, pendingIntent3);
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

