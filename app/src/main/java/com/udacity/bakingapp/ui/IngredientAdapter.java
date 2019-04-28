package com.udacity.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.IngredientActivity;
import com.udacity.bakingapp.R;
import com.udacity.bakingapp.viewModel.DetailViewModel;
import com.udacity.bakingapp.vo.Ingredient;
import com.udacity.bakingapp.vo.Recipe;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private List<Ingredient> mIngredients;

    private final IngredientActivity mActivity;
    private final DetailViewModel mDetailViewModel;

    public static class IngredientViewHolder extends RecyclerView.ViewHolder {
        private TextView mQuantity;
        private TextView mMeasure;
        private TextView mIngredient;
        private IngredientViewHolder(ConstraintLayout constraintLayout) {
            super(constraintLayout);
            mQuantity = constraintLayout.findViewById(R.id.ingredient_quantity);
            mMeasure = constraintLayout.findViewById(R.id.ingredient_measure);
            mIngredient = constraintLayout.findViewById(R.id.ingredient_ingredient);
        }
    }

    public IngredientAdapter(IngredientActivity activity, DetailViewModel detailViewModel) {
        mActivity = activity;
        mDetailViewModel = detailViewModel;
        startDatabaseObserver();
    }

    public void startDatabaseObserver() {
        mDetailViewModel.getRecipe().observe(mActivity, new Observer<Recipe>() {
            @Override
            public void onChanged(@Nullable Recipe recipe) {
                if (recipe == null) {
                    mActivity.sendToastMessage();
                    return;
                }

                mIngredients = recipe.getIngredients();
                IngredientAdapter.this.notifyDataSetChanged();
            }
        });
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ingredient, parent, false);

        return new IngredientViewHolder(constraintLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.mQuantity.setText(Double.toString(mIngredients.get(position).getQuantity()));
        holder.mMeasure.setText(mIngredients.get(position).getMeasure());
        holder.mIngredient.setText(mIngredients.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) {
            return 0;
        }

        return mIngredients.size();
    }
}