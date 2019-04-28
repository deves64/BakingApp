package com.udacity.bakingapp;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.bakingapp.util.AppExecutors;
import com.udacity.bakingapp.viewModel.DetailViewModel;
import com.udacity.bakingapp.vo.Recipe;
import com.udacity.bakingapp.vo.Step;

import java.util.List;

import javax.inject.Inject;

public class RecipeActivity extends BaseActivity implements RecipeStepsFragment.OnFragmentInteractionListener, View.OnClickListener {

    public final static String EXTRA_RECIPE_ID = "extraRecipeId";

    private List<Step> mStepList;

    public int recipeId;

    public int stepId;

    @Inject
    DetailViewModel detailViewModel;

    @Inject
    AppExecutors appExecutors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        if (savedInstanceState == null) {
            stepId = 0;
        }

        getActivityComponent().inject(this);

        TextView textView = (TextView) findViewById(R.id.ingredients_card_name);
        textView.setOnClickListener(this);

        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            RecipeStepsFragment recipeStepsFragment = new RecipeStepsFragment();
            recipeStepsFragment.setRecipeId(recipeId);

            fragmentManager.beginTransaction()
                    .add(R.id.steps_fragment, recipeStepsFragment)
                    .commit();
        }

        startObserver();
    }

    private void startObserver() {
        detailViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe == null) {
                    sendToastMessage();
                    return;
                }

                RecipeActivity.this.setTitle(recipe.getName());
            }
        });

        detailViewModel.getSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                if (steps == null) {
                    sendToastMessage();
                    return;
                }

                mStepList = steps;
                startFragment(steps.get(stepId));
            }
        });
    }

    private void startFragment(Step step) {
        if (findViewById(R.id.step_fragment) == null) {
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.findFragmentById(R.id.step_fragment) == null) {
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setStep(step);
            recipeStepFragment.setAppExecutors(appExecutors);

            fragmentManager.beginTransaction()
                    .add(R.id.step_fragment, recipeStepFragment)
                    .commit();
        }
    }

    private void closeOnError() {
        finish();
    }

    @Override
    public void onFragmentInteraction(Step step) {
        if (findViewById(R.id.step_fragment) != null) {
            stepId = step.getId();
            FragmentManager fragmentManager = getSupportFragmentManager();

            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setStep(step);
            recipeStepFragment.setAppExecutors(appExecutors);

            fragmentManager.beginTransaction()
                    .replace(R.id.step_fragment, recipeStepFragment)
                    .commit();
        }
        else {
            launchStepActivity(step);
        }
    }

    private void launchStepActivity(Step step) {
        Intent intent = new Intent(this, StepActivity.class);
        intent.putExtra(RecipeActivity.EXTRA_RECIPE_ID, recipeId);
        intent.putExtra(StepActivity.EXTRA_STEP_ID, step.getId());
        startActivity(intent);
    }

    private void launchIngredientActivity() {
        Intent intent = new Intent(this, IngredientActivity.class);
        intent.putExtra(RecipeActivity.EXTRA_RECIPE_ID, recipeId);
        startActivity(intent);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        recipeId = savedInstanceState.getInt(RecipeActivity.EXTRA_RECIPE_ID);
        stepId = savedInstanceState.getInt(StepActivity.EXTRA_STEP_ID);

        Step step = mStepList.get(stepId);
        FragmentManager fragmentManager = getSupportFragmentManager();

        RecipeStepFragment recipeStepFragment
                = (RecipeStepFragment) fragmentManager.findFragmentById(R.id.step_fragment);

        if (recipeStepFragment != null) {
            recipeStepFragment.setStep(step);
            recipeStepFragment.setAppExecutors(appExecutors);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(StepActivity.EXTRA_STEP_ID, stepId);
        outState.putInt(RecipeActivity.EXTRA_RECIPE_ID, recipeId);

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ingredients_card_name) {
            launchIngredientActivity();
        }
    }

    public void sendToastMessage() {
         Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
    }
}