package com.udacity.bakingapp.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.udacity.bakingapp.repository.RecipeRepository;

public class StepViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RecipeRepository mRecipeRepository;
    private final int mId;

    public StepViewModelFactory(RecipeRepository recipeRepository, int id) {
        mRecipeRepository = recipeRepository;
        mId = id;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new StepViewModel(mRecipeRepository, mId);
    }
}

