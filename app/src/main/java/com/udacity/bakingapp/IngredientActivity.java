package com.udacity.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.udacity.bakingapp.ui.IngredientAdapter;
import com.udacity.bakingapp.viewModel.DetailViewModel;
import com.udacity.bakingapp.vo.Ingredient;

import java.util.List;

import javax.inject.Inject;

public class IngredientActivity extends BaseActivity {

    public final static String EXTRA_RECIPE_ID = "extraRecipeId";

    private List<Ingredient> mIngredientList;

    public int recipeId;

    @Inject
    DetailViewModel detailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        final int id = intent.getIntExtra(EXTRA_RECIPE_ID, 1000);

        if (id == 1000) {
            closeOnError();
        }

        recipeId = id;

        getActivityComponent().inject(this);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ingredients_recycler_view);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        IngredientAdapter adapter = new IngredientAdapter(this, detailViewModel);
        recyclerView.setAdapter(adapter);

        setTitle(R.string.title_ingredients);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void closeOnError() {
        finish();
    }

    public void sendToastMessage() {
        Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
    }
}