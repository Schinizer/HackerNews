package com.schinizer.hackernews.dagger2.modules;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.schinizer.hackernews.HackerNewsAPI;
import com.schinizer.hackernews.dagger2.scopes.Local;
import com.schinizer.hackernews.dagger2.scopes.Remote;
import com.schinizer.hackernews.data.ItemDataSource;
import com.schinizer.hackernews.data.local.ItemLocalDataSource;
import com.schinizer.hackernews.data.remote.ItemRemoteDataSource;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DPSUser on 10/24/2016.
 */

@Module
public class ItemDataSourceModule {

    @Singleton
    @Provides
    @Local
    ItemDataSource provideItemLocalDataSource(SharedPreferences sharedPreferences, Gson gson)
    {
        return new ItemLocalDataSource(sharedPreferences, gson);
    }

    @Singleton
    @Provides
    @Remote
    ItemDataSource provideItemRemoteDataSource(HackerNewsAPI api)
    {
        return new ItemRemoteDataSource(api);
    }
}
