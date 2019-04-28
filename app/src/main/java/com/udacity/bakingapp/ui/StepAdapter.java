package com.udacity.bakingapp.ui;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.bakingapp.R;
import com.udacity.bakingapp.RecipeStepsFragment;
import com.udacity.bakingapp.viewModel.DetailViewModel;
import com.udacity.bakingapp.vo.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    private List<Step> mSteps;

    private final RecipeStepsFragment mFragment;
    private final DetailViewModel mDetailViewModel;

    public static class StepViewHolder extends RecyclerView.ViewHolder {
        private TextView mStepCardName;
        private StepViewHolder(ConstraintLayout constraintLayout) {
            super(constraintLayout);
            mStepCardName = constraintLayout.findViewById(R.id.step_card_name);
        }
    }

    public StepAdapter(RecipeStepsFragment fragment, DetailViewModel detailViewModel) {
        mFragment = fragment;
        mDetailViewModel = detailViewModel;
        startObserver();
    }

    public void startObserver() {
       mDetailViewModel.getSteps().observe(mFragment, new Observer<List<Step>>() {
           @Override
           public void onChanged(@Nullable List<Step> steps) {
               if (steps == null) {
                   return;
               }

               mSteps = steps;
               StepAdapter.this.notifyDataSetChanged();
           }
       });
    }

    public Step getStepAtPosition(int position) {
        return mSteps.get(position);
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.step_card, parent, false);

        constraintLayout.setOnClickListener(mFragment);

        return new StepViewHolder(constraintLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.mStepCardName.setText(mSteps.get(position).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) {
            return 0;
        }

        return mSteps.size();
    }
}
