package com.udacity.bakingapp.di;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.Fragment;

import com.udacity.bakingapp.RecipeStepFragment;
import com.udacity.bakingapp.RecipeStepsFragment;
import com.udacity.bakingapp.repository.RecipeRepository;
import com.udacity.bakingapp.viewModel.DetailViewModel;
import com.udacity.bakingapp.viewModel.DetailViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private final Fragment mFragment;

    public FragmentModule(Fragment fragment) {
        mFragment = fragment;
    }

    @Provides
    DetailViewModel getDetailViewModelInstance(RecipeRepository recipeRepository) {

        RecipeStepsFragment recipeStepsFragment = (RecipeStepsFragment) mFragment;
        int id = recipeStepsFragment.recipeId;
        DetailViewModelFactory detailViewModelFactory
                = new DetailViewModelFactory(recipeRepository, id);

        return ViewModelProviders.of(recipeStepsFragment, detailViewModelFactory)
                .get(DetailViewModel.class);
    }
}
