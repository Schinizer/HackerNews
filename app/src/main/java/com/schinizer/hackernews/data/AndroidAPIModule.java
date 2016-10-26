package com.schinizer.hackernews.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DPSUser on 10/24/2016.
 */

@Module
public class AndroidAPIModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreference(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
