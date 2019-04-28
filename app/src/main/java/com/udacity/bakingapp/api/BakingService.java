package com.udacity.bakingapp.api;

import com.udacity.bakingapp.vo.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingService {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> listRecipe();
}
