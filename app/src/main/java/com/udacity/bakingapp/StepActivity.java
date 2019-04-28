package com.udacity.bakingapp;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.udacity.bakingapp.util.AppExecutors;
import com.udacity.bakingapp.viewModel.StepViewModel;
import com.udacity.bakingapp.vo.Recipe;
import com.udacity.bakingapp.vo.Step;

import java.util.List;

import javax.inject.Inject;

public class StepActivity extends BaseActivity implements View.OnClickListener  {

    public final static String EXTRA_STEP_ID = "extraStepId";

    public int recipeId;
    public int stepId;

    private List<Step> mStepList;

    @Inject
    StepViewModel stepViewModel;

    @Inject
    AppExecutors appExecutors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
           hideSystemUI();
        }

        setContentView(R.layout.activity_step);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        final int rId = intent.getIntExtra(RecipeActivity.EXTRA_RECIPE_ID, 1000);
        final int sId = intent.getIntExtra(EXTRA_STEP_ID, 1000);

        if (sId == 1000 || rId == 1000) {
            closeOnError();
        }

        recipeId = rId;
        stepId = sId;

        getActivityComponent().inject(this);

       /* if (findViewById(R.id.button_next) != null && findViewById(R.id.button_prev) != null) {
            Button buttonNext = findViewById(R.id.button_next);
            buttonNext.setOnClickListener(this);

            Button buttonPrev = findViewById(R.id.button_prev);
            buttonPrev.setOnClickListener(this);
        }*/

        startObserver();
    }

    private void startObserver() {
        stepViewModel.getRecipe().observe(this, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe == null) {
                    sendToastMessage();
                    return;
                }

                StepActivity.this.setTitle(recipe.getName());
            }
        });

        stepViewModel.getSteps().observe(this, new Observer<List<Step>>() {
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
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        recipeId = savedInstanceState.getInt(RecipeActivity.EXTRA_RECIPE_ID);
        stepId = savedInstanceState.getInt(EXTRA_STEP_ID);

        Step step = mStepList.get(stepId);
        FragmentManager fragmentManager = getSupportFragmentManager();

        RecipeStepFragment recipeStepFragment
                = (RecipeStepFragment) fragmentManager.findFragmentById(R.id.step_fragment);

        if (recipeStepFragment != null) {
            recipeStepFragment.setStep(step);
            recipeStepFragment.setAppExecutors(appExecutors);
        }
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_STEP_ID, stepId);
        outState.putInt(RecipeActivity.EXTRA_RECIPE_ID, recipeId);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {

        if (mStepList == null) {
            return;
        }

       /* if (v.getId() == R.id.button_next) {
            if (stepId < mStepList.size() - 1) {
                Step step = mStepList.get(stepId + 1);
                stepId = step.getId();

                FragmentManager fragmentManager = getSupportFragmentManager();

                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                recipeStepFragment.setStep(step);
                recipeStepFragment.setAppExecutors(appExecutors);

                fragmentManager.beginTransaction()
                        .replace(R.id.step_fragment, recipeStepFragment)
                        .commit();

                return;
            }
        }

        if (v.getId() == R.id.button_prev) {
            if (stepId > 0) {
                Step step = mStepList.get(stepId - 1);
                stepId = step.getId();

                FragmentManager fragmentManager = getSupportFragmentManager();

                RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
                recipeStepFragment.setStep(step);
                recipeStepFragment.setAppExecutors(appExecutors);

                fragmentManager.beginTransaction()
                        .replace(R.id.step_fragment, recipeStepFragment)
                        .commit();
            }
        }*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public void sendToastMessage() {
        Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_SHORT).show();
    }
}