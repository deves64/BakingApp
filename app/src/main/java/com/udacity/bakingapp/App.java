package com.udacity.bakingapp;

import android.app.Application;

import com.udacity.bakingapp.di.AppComponent;
import com.udacity.bakingapp.di.AppModule;
import com.udacity.bakingapp.di.DaggerAppComponent;

public class App extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
