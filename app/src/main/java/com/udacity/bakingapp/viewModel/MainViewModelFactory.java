package com.udacity.bakingapp.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.udacity.bakingapp.repository.RecipeRepository;

public class MainViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RecipeRepository mRecipeRepository;

    public MainViewModelFactory(RecipeRepository recipeRepository) {
        mRecipeRepository = recipeRepository;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new MainViewModel(mRecipeRepository);
    }
}