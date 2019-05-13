package com.udacity.bakingapp.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.bakingapp.repository.RecipeRepository;
import com.udacity.bakingapp.vo.Recipe;

import java.util.List;

public class MainViewModel extends ViewModel {
    private LiveData<List<Recipe>> mLiveDataRecipes;

    protected RecipeRepository mRecipeRepository;

    public MainViewModel(RecipeRepository recipeRepository) {
        mRecipeRepository = recipeRepository;
        mLiveDataRecipes = mRecipeRepository.getRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mLiveDataRecipes;
    }

    public RecipeRepository getmRecipeRepository() {
        return mRecipeRepository;
    }
}
