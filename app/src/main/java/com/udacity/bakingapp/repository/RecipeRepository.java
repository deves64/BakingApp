package com.udacity.bakingapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.udacity.bakingapp.api.BakingService;
import com.udacity.bakingapp.util.AppExecutors;
import com.udacity.bakingapp.vo.Recipe;
import com.udacity.bakingapp.vo.Step;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

public class RecipeRepository {
    private BakingService mBakingService;
    private AppExecutors mAppExecutors;

    public RecipeRepository(BakingService bakingService, AppExecutors appExecutors) {
        mBakingService = bakingService;
        mAppExecutors = appExecutors;
    }

    public LiveData<List<Recipe>> getRecipes() {
        final MutableLiveData<List<Recipe>> recipesLiveData = new MutableLiveData<>();

        mAppExecutors.getmNetworkIo().execute(new Runnable() {
            @Override
            public void run() {
                Boolean internet = isInternetConnectionAvailable();

                if (!internet) {
                    recipesLiveData.postValue(null);
                    return;
                }

                try {

                    List<Recipe> recipes = mBakingService.listRecipe().execute().body();

                    if (recipes== null) {
                        return;
                    }

                    for (int i = 0; i < recipes.size(); i++) {
                        Recipe recipe = recipes.get(i);
                        recipe.setId(i);
                    }

                    recipesLiveData.postValue(recipes);
                }
                catch (IOException e) {
                    recipesLiveData.postValue(null);
                }
            }
        });

        return recipesLiveData;
    }

    public LiveData<Recipe> getRecipe(final int id) {
        final MutableLiveData<Recipe> recipeLiveData = new MutableLiveData<>();

        mAppExecutors.getmNetworkIo().execute(new Runnable() {
            @Override
            public void run() {
                Boolean internet = isInternetConnectionAvailable();

                if (!internet) {
                    recipeLiveData.postValue(null);
                    return;
                }

                try {
                    List<Recipe> recipes = mBakingService.listRecipe().execute().body();

                    if (recipes== null) {
                        return;
                    }

                    for (int i = 0; i < recipes.size(); i++) {
                        Recipe recipe = recipes.get(i);
                        recipe.setId(i);
                    }

                    Recipe recipe = recipes.get(id);

                    recipeLiveData.postValue(recipe);
                }
                catch (IOException e) {
                    recipeLiveData.postValue(null);
                }
            }
        });

        return recipeLiveData;
    }

    public LiveData<List<Step>> getSteps(final int id) {
        final MutableLiveData<List<Step>> recipesLiveData = new MutableLiveData<>();

        mAppExecutors.getmNetworkIo().execute(new Runnable() {
            @Override
            public void run() {
                Boolean internet = isInternetConnectionAvailable();

                if (!internet) {
                    recipesLiveData.postValue(null);
                    return;
                }

                try {
                    List<Recipe> recipes = mBakingService.listRecipe().execute().body();

                    if (recipes== null) {
                        return;
                    }

                    for (int i = 0; i < recipes.size(); i++) {
                        Recipe recipe = recipes.get(i);
                        recipe.setId(i);
                    }

                    List<Step> steps = recipes.get(id).getSteps();

                    for (int i = 0; i < steps.size(); i++) {
                        Step step = steps.get(i);
                        step.setId(i);
                    }

                    recipesLiveData.postValue(steps);
                }
                catch (IOException e) {
                    recipesLiveData.postValue(null);
                }
            }
        });

        return recipesLiveData;
    }

    private Boolean isInternetConnectionAvailable() {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
