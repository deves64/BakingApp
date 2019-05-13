package com.udacity.bakingapp;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.udacity.bakingapp.ui.RecipeAdapter;
import com.udacity.bakingapp.viewModel.MainViewModel;
import com.udacity.bakingapp.vo.Recipe;
import com.udacity.bakingapp.widget.BakingAppWidget;

import javax.inject.Inject;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;

    @Inject
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);

        mRecyclerView = null;
        StaggeredGridLayoutManager layoutManager = null;

        if (findViewById(R.id.main_one_row_recycler_view) != null) {
            mRecyclerView = (RecyclerView) findViewById(R.id.main_one_row_recycler_view);
            layoutManager = new StaggeredGridLayoutManager(
                    1,
                    StaggeredGridLayoutManager.VERTICAL
            );
        }
        else {
            mRecyclerView = (RecyclerView) findViewById(R.id.main_three_row_recycler_view);
            layoutManager = new StaggeredGridLayoutManager(
                    3,
                    StaggeredGridLayoutManager.VERTICAL
            );
        }

        mRecyclerView.setHasFixedSize(true);
        layoutManager
                .setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        mRecyclerView.setLayoutManager(layoutManager);

        RecipeAdapter adapter = new RecipeAdapter(this, mainViewModel);

        mRecyclerView.setAdapter(adapter);
    }

    private void launchDetailActivity(Recipe recipe) {

        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RecipeActivity.EXTRA_RECIPE_ID, recipe.getId());


        saveRecipeInSharedPreferences(recipe);
        updateBakingAppWidget(this, recipe);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int position = mRecyclerView.getChildLayoutPosition(v);
        RecipeAdapter adapter = (RecipeAdapter) mRecyclerView.getAdapter();

        Recipe recipe = null;

        if (adapter != null) {
            recipe = adapter.getRecipeAtPosition(position);
        }

        if (recipe != null) {
            launchDetailActivity(recipe);
        }
    }

    private void updateBakingAppWidget(Context context, Recipe recipe) {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));
        BakingAppWidget.updateBakingAppWidgets(context, widgetManager, appWidgetIds, recipe);
    }

    private void saveRecipeInSharedPreferences(Recipe recipe) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();

        String json = gson.toJson(recipe);

        editor.putString("CURRENT_RECIPE", json);

        editor.apply();
    }

    public void sendToastMessage() {
        Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
