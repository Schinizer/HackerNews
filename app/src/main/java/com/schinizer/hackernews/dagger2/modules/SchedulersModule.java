package com.schinizer.hackernews.dagger2.modules;

import com.schinizer.hackernews.utility.schedulers.BaseSchedulerProvider;
import com.schinizer.hackernews.utility.schedulers.SchedulerProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DPSUser on 10/26/2016.
 */

@Module
public class SchedulersModule {
    @Provides
    BaseSchedulerProvider providesSchedulerProvider() { return new SchedulerProvider(); }
}
