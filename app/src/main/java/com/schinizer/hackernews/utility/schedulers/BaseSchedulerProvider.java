package com.schinizer.hackernews.utility.schedulers;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * Created by DPSUser on 10/26/2016.
 */

public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler newThread();

    @NonNull
    Scheduler ui();
}
