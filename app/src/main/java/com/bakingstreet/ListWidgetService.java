package com.bakingstreet;

/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*  	http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bakingstreet.data.Recipe;
import com.bakingstreet.data.Statics;
import java.util.List;


public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Recipe> mRecipesList;
    private int mRecipeIndex;

    ListRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override public void onCreate() {
        getDataForList();
    }
    @Override public void onDataSetChanged() {
        getDataForList();
    }
    @Override public void onDestroy() {
    }
    @Override public int getCount() {
        return mRecipesList.get(mRecipeIndex).getIngredients().size();
    }
    @Override public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.baking_helper_widget_list_element);

        Recipe.Ingredient ingredient = mRecipesList.get(mRecipeIndex).getIngredients().get(position);
        views.setTextViewText(R.id.ingredient, ingredient.getIngredient());
        views.setTextViewText(R.id.quantity, ingredient.getQuantity());
        views.setTextViewText(R.id.measure, ingredient.getMeasure());

        return views;
    }
    @Override public RemoteViews getLoadingView() {
        return null;
    }
    @Override public int getViewTypeCount() {
        return 1; // Treat all items in the ListView the same
    }
    @Override public long getItemId(int i) {
        return i;
    }
    @Override public boolean hasStableIds() {
        return true;
    }

    private void getDataForList() {
        mRecipesList = Statics.getRecipeListFromJson(mContext);
        mRecipeIndex = Statics.restoreChosenRecipeIndex(mContext);
    }
}

