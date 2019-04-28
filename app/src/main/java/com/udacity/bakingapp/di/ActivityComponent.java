package com.udacity.bakingapp.di;

import com.udacity.bakingapp.IngredientActivity;
import com.udacity.bakingapp.MainActivity;
import com.udacity.bakingapp.RecipeActivity;
import com.udacity.bakingapp.StepActivity;

import dagger.Subcomponent;

@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(RecipeActivity recipeActivity);
    void inject(StepActivity stepActivity);
    void inject(IngredientActivity ingredientActivity);
}
