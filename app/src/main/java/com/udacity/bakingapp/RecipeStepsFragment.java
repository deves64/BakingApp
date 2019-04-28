package com.udacity.bakingapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.bakingapp.ui.StepAdapter;
import com.udacity.bakingapp.viewModel.DetailViewModel;
import com.udacity.bakingapp.vo.Step;

import javax.inject.Inject;

public class RecipeStepsFragment extends BaseFragment implements View.OnClickListener {

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;

    public int recipeId;

    @Inject
    DetailViewModel mDetailViewModel;

    public RecipeStepsFragment() {
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            recipeId = savedInstanceState.getInt(RecipeActivity.EXTRA_RECIPE_ID, 1000);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        if (currentState != null) {
            currentState.putInt(RecipeActivity.EXTRA_RECIPE_ID, recipeId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.steps_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        Log.v("onCreateView", Integer.toString(recipeId));

        StepAdapter adapter = new StepAdapter(this, mDetailViewModel);
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Step id) {
        if (mListener != null) {
            mListener.onFragmentInteraction(id);
        }
    }

    @Override
    public void onAttach(Context context) {
        if (recipeId != 1000) {
            getFragmentComponent().inject(this);
        }

        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        int position = mRecyclerView.getChildLayoutPosition(v);
        StepAdapter adapter = (StepAdapter) mRecyclerView.getAdapter();

        Step step = null;

        if (adapter != null) {
            step = adapter.getStepAtPosition(position);
        }

        mListener.onFragmentInteraction(step);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Step step);
    }
}