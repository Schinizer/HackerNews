package com.schinizer.hackernews.utility.schedulers;

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
