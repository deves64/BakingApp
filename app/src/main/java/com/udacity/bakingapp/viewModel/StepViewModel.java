package com.udacity.bakingapp.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.bakingapp.repository.RecipeRepository;
import com.udacity.bakingapp.vo.Recipe;
import com.udacity.bakingapp.vo.Step;

import java.util.List;

public class StepViewModel extends ViewModel {
    private LiveData<List<Step>> mLiveDataSteps;
    private LiveData<Recipe> mLiveDataRecipe;

    protected RecipeRepository mRecipeRepository;

    public StepViewModel(RecipeRepository recipeRepository, int id) {
        mRecipeRepository = recipeRepository;
        mLiveDataSteps = recipeRepository.getSteps(id);
        mLiveDataRecipe = recipeRepository.getRecipe(id);
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

