package com.udacity.bakingapp;

import android.support.test.espresso.IdlingResource;
import android.util.Log;

import com.udacity.bakingapp.repository.RecipeRepository;

public class RecipeAdapterResource implements IdlingResource {

    private final RecipeRepository mRecipeRepository;

    private ResourceCallback mResourceCallback;

    private Boolean mIdle = false;

    public RecipeAdapterResource(RecipeRepository recipeRepository) {
        mRecipeRepository = recipeRepository;

        if (mRecipeRepository.getmRequestOpen() == 0) {
            mIdle = true;
            Log.v("RecipeAdapterResource", "Idle: True");
        }
        else {
            mIdle = false;
            Log.v("RecipeAdapterResource", "Idle: False");
        }
    }

    @Override
    public String getName() {
        return "RecipeAdapterResource";
    }

    @Override
    public boolean isIdleNow() {
        if (mRecipeRepository.getmRequestOpen() == 0) {
            mIdle = true;
            Log.v("RecipeAdapterResource", "Idle: True");
            mResourceCallback.onTransitionToIdle();
        }
        else {
            mIdle = false;
            Log.v("RecipeAdapterResource", "Idle: False");
        }

        return mIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mResourceCallback = callback;
    }
}
