package com.udacity.bakingapp.util;

import java.util.concurrent.Executor;

import javax.inject.Inject;

public class AppExecutors {
    private final Executor mDiskIo;
    private final Executor mNetworkIo;
    private final Executor mMainThread;

    @Inject
    public AppExecutors(Executor diskIO, Executor networkIO, Executor mainThread) {
        mDiskIo = diskIO;
        mNetworkIo = networkIO;
        mMainThread = mainThread;
    }

    public Executor getmDiskIo() {
        return mDiskIo;
    }

    public Executor getmNetworkIo() {
        return mNetworkIo;
    }

    public Executor getmMainThread() {
        return mMainThread;
    }
}