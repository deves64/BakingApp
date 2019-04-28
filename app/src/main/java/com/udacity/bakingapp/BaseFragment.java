package com.udacity.bakingapp;

import android.support.v4.app.Fragment;

import com.udacity.bakingapp.di.AppComponent;
import com.udacity.bakingapp.di.FragmentComponent;
import com.udacity.bakingapp.di.FragmentModule;

public abstract class BaseFragment extends Fragment {
    private boolean mIsInjectorUsed;

    protected FragmentComponent getFragmentComponent() {
        if (mIsInjectorUsed) {
            throw new RuntimeException("there is no need to use injector more than once");
        }

        mIsInjectorUsed = true;

        return getApplicationComponent()
                .newFragmentComponent(new FragmentModule(this));

    }

    private AppComponent getApplicationComponent() {
        return ((App) getActivity().getApplication()).getAppComponent();
    }
}
