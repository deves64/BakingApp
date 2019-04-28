package com.udacity.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.vo.Ingredient;
import com.udacity.bakingapp.vo.Recipe;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class BakingAppWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;

    private Recipe mRecipe;

    private ArrayList<Ingredient> mIngredientList;

    public BakingAppWidgetRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        //mIngredientList = intent.getParcelableArrayListExtra("ingredients");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        Gson gson = new Gson();
        String json = sharedPrefs.getString("CURRENT_RECIPE", "");

        Type type = new TypeToken<Recipe>() {}.getType();
        Recipe recipe = gson.fromJson(json, type);

        mRecipe = recipe;

        mIngredientList = (ArrayList<Ingredient>) recipe.getIngredients();
    }

    @Override
    public void onDestroy() {
        if (mIngredientList != null) {
            mIngredientList.clear();
        }
    }

    @Override
    public int getCount() {
        if (mIngredientList == null) {
            return 0;
        }

        return mIngredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.baking_app_widget_item);
        remoteViews.setTextViewText(R.id.widgetItemQuantityNameLabel,
                Double.toString(mIngredientList.get(position).getQuantity()));

        remoteViews.setTextViewText(R.id.widgetItemMeasureNameLabel,
                mIngredientList.get(position).getMeasure());

        remoteViews.setTextViewText(R.id.widgetItemIngredientNameLabel,
                mIngredientList.get(position).getIngredient());

        Bundle extras = new Bundle();
        extras.putInt(BakingAppWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
