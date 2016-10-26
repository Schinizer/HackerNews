package com.schinizer.hackernews;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DPSUser on 10/14/2016.
 */

@Module
public class AppModule {

    private Context application;

    public AppModule(Context application)
    {
        this.application = application;
    }

    @Provides
    @Singleton
    Context providesApplication()
    {
        return this.application;
    }
}
