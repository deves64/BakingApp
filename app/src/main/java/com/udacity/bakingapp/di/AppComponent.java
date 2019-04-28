package com.udacity.bakingapp.di;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    public ActivityComponent newActivityComponent(ActivityModule activityModule);
    public FragmentComponent newFragmentComponent(FragmentModule fragmentModule);
}
