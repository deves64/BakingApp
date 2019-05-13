package com.udacity.bakingapp.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.bakingapp.repository.RecipeRepository;
import com.udacity.bakingapp.vo.Recipe;
import com.udacity.bakingapp.vo.Step;

import java.util.List;

public class DetailViewModel extends ViewModel {
    private LiveData<List<Step>> mLiveDataSteps;
    private LiveData<Recipe> mLiveDataRecipe;

    protected RecipeRepository mRecipeRepository;

    public DetailViewModel(RecipeRepository recipeRepository, int recipeId) {
        mRecipeRepository = recipeRepository;
        mLiveDataSteps = recipeRepository.getSteps(recipeId);
        mLiveDataRecipe = recipeRepository.getRecipe(recipeId);
    }

    public LiveData<List<Step>> getSteps() {
        return mLiveDataSteps;
    }

    public LiveData<Recipe> getRecipe() {
        return  mLiveDataRecipe;
    }

    public RecipeRepository getmRecipeRepository() {
        return mRecipeRepository;
    }
}