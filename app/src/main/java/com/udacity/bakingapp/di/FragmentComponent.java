package com.udacity.bakingapp.di;

import com.udacity.bakingapp.RecipeStepFragment;
import com.udacity.bakingapp.RecipeStepsFragment;

import dagger.Subcomponent;

@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
    void inject(RecipeStepsFragment recipeStepsFragment);
    void inject(RecipeStepFragment recipeStepFragment);
}
