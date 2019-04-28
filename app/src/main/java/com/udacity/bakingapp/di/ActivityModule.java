package com.udacity.bakingapp.di;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.udacity.bakingapp.IngredientActivity;
import com.udacity.bakingapp.RecipeActivity;
import com.udacity.bakingapp.MainActivity;
import com.udacity.bakingapp.StepActivity;
import com.udacity.bakingapp.repository.RecipeRepository;
import com.udacity.bakingapp.viewModel.DetailViewModel;
import com.udacity.bakingapp.viewModel.DetailViewModelFactory;
import com.udacity.bakingapp.viewModel.MainViewModel;
import com.udacity.bakingapp.viewModel.MainViewModelFactory;
import com.udacity.bakingapp.viewModel.StepViewModel;
import com.udacity.bakingapp.viewModel.StepViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private final FragmentActivity mActivity;

    public ActivityModule(FragmentActivity activity) {
        mActivity = activity;
    }

    @Provides
    MainViewModel getMainViewModelInstance(RecipeRepository recipeRepository) {
        MainActivity mainActivity = (MainActivity) mActivity;

        MainViewModelFactory mainViewModelFactory = new MainViewModelFactory(recipeRepository);

        return ViewModelProviders.of(mainActivity, mainViewModelFactory)
                .get(MainViewModel.class);
    }

    @Provides
    DetailViewModel getDetailViewModelInstance(RecipeRepository recipeRepository) {

        if(mActivity instanceof RecipeActivity) {
            RecipeActivity recipeActivity = (RecipeActivity) mActivity;
            int id = recipeActivity.recipeId;

            DetailViewModelFactory detailViewModelFactory
                    = new DetailViewModelFactory(recipeRepository, id);

            return ViewModelProviders.of(recipeActivity, detailViewModelFactory)
                    .get(DetailViewModel.class);
        }

        if (mActivity instanceof IngredientActivity) {
            IngredientActivity ingredientActivity = (IngredientActivity) mActivity;
            int id = ingredientActivity.recipeId;

            DetailViewModelFactory detailViewModelFactory
                    = new DetailViewModelFactory(recipeRepository, id);

            return ViewModelProviders.of(ingredientActivity, detailViewModelFactory)
                    .get(DetailViewModel.class);
        }

        return null;
    }

    @Provides
    StepViewModel getStepViewModelInstance(RecipeRepository recipeRepository) {
        StepActivity recipeActivity = (StepActivity) mActivity;
        int id = recipeActivity.recipeId;

        StepViewModelFactory stepViewModelFactory
                = new StepViewModelFactory(recipeRepository, id);

        return ViewModelProviders.of(recipeActivity, stepViewModelFactory)
                .get(StepViewModel.class);
    }
}
