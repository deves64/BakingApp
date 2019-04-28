package com.udacity.bakingapp.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.udacity.bakingapp.repository.RecipeRepository;

public class DetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private final RecipeRepository mRecipeRepository;
    private final int mId;

    public DetailViewModelFactory(RecipeRepository recipeRepository, int id) {
        mRecipeRepository = recipeRepository;
        mId = id;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new DetailViewModel(mRecipeRepository, mId);
    }
}
