package com.udacity.bakingapp.di;

import android.app.Application;

import com.udacity.bakingapp.api.BakingService;
import com.udacity.bakingapp.repository.RecipeRepository;
import com.udacity.bakingapp.util.AppExecutors;
import com.udacity.bakingapp.util.MainThreadExecutor;
import com.udacity.bakingapp.util.NetworkUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    private Application mApp;

    private final static String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    public AppModule(Application app) {
        mApp = app;
    }

    @Singleton
    @Provides
    NetworkUtil getNetworkUtilInstance() {
        return new NetworkUtil();
    }

    @Singleton
    @Provides
    @Named("diskIoExecutor")
    Executor getDiskIoExecutorInstance() {
        return Executors.newSingleThreadExecutor();
    }

    @Singleton
    @Provides
    @Named("networkIoExecutor")
    Executor getNetworkIoExecutorInstance() {
        return Executors.newFixedThreadPool(3);
    }

    @Singleton
    @Provides
    @Named("mainThread")
    Executor getMainThread() {
        return new MainThreadExecutor();
    }

    @Singleton
    @Provides
    AppExecutors getAppExecutorsInstance(@Named("diskIoExecutor") Executor diskIoExecutor,
                                         @Named("networkIoExecutor") Executor networkIoExecutor,
                                         @Named("mainThread") Executor mainThread) {

        return new AppExecutors(diskIoExecutor, networkIoExecutor, mainThread);
    }

    @Provides
    BakingService getBakingServiceInstance(Retrofit retrofit) {
        return retrofit.create(BakingService.class);
    }

    @Singleton
    @Provides
    Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }

    @Singleton
    @Provides
    RecipeRepository getRecipeRepositoryInstance(BakingService bakingService,
                                                 AppExecutors appExecutors) {

        return new RecipeRepository(bakingService, appExecutors);
    }
}