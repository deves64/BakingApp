package com.udacity.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.MainActivity;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.viewModel.MainViewModel;
import com.udacity.bakingapp.vo.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private List<Recipe> mRecipes;
    private MainViewModel mMainViewModel;

    private final MainActivity mActivity;

    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        private TextView mRecipeCardName;
        private RecipeViewHolder(ConstraintLayout constraintLayout) {
            super(constraintLayout);
            mRecipeCardName = constraintLayout.findViewById(R.id.recipe_card_name);
        }
    }

    public RecipeAdapter(MainActivity activity, MainViewModel mainViewModel) {
        mActivity = activity;
        mMainViewModel = mainViewModel;
        startObserver();
    }

    private void startObserver() {
        mMainViewModel.getRecipes().observe(mActivity, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if (recipes == null) {
                    mActivity.sendToastMessage();
                    return;
                }

                mRecipes = recipes;
                RecipeAdapter.this.notifyDataSetChanged();
            }
        });
    }

    public Recipe getRecipeAtPosition(int position) {
        return mRecipes.get(position);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);

        constraintLayout.setOnClickListener(mActivity);

        return new RecipeViewHolder(constraintLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.mRecipeCardName.setText(mRecipes.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) {
            return 0;
        }

        return mRecipes.size();
    }
}
