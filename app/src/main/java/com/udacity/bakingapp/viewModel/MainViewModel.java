package com.udacity.bakingapp.viewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.udacity.bakingapp.repository.RecipeRepository;
import com.udacity.bakingapp.vo.Recipe;

import java.util.List;

public class MainViewModel extends ViewModel {
    private LiveData<List<Recipe>> mLiveDataRecipes;

    public MainViewModel(RecipeRepository recipeRepository) {
        mLiveDataRecipes = recipeRepository.getRecipes();
    }

    public LiveData<List<Recipe>> getRecipes() {
        return mLiveDataRecipes;
    }
}
