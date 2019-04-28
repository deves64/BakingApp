package com.udacity.bakingapp;

import android.support.v7.app.AppCompatActivity;

import com.udacity.bakingapp.di.ActivityComponent;
import com.udacity.bakingapp.di.ActivityModule;
import com.udacity.bakingapp.di.AppComponent;

public abstract class BaseActivity extends AppCompatActivity {
    private boolean mIsInjectorUsed;

    protected ActivityComponent getActivityComponent() {
        if (mIsInjectorUsed) {
            throw new RuntimeException("there is no need to use injector more than once");
        }

        mIsInjectorUsed = true;

        return getApplicationComponent().newActivityComponent(new ActivityModule(this));

    }

    private AppComponent getApplicationComponent() {
        return ((App) getApplication()).getAppComponent();
    }
}