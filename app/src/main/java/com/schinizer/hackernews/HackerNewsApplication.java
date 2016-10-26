package com.schinizer.hackernews;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.schinizer.hackernews.dagger2.components.DaggerItemRepositoryComponent;
import com.schinizer.hackernews.dagger2.components.ItemRepositoryComponent;
import com.schinizer.hackernews.dagger2.modules.APIModule;
import com.schinizer.hackernews.dagger2.modules.AndroidAPIModule;
import com.schinizer.hackernews.dagger2.modules.AppModule;
import com.schinizer.hackernews.dagger2.modules.ItemDataSourceModule;
import com.schinizer.hackernews.dagger2.modules.NetModule;
import com.schinizer.hackernews.dagger2.modules.SchedulersModule;

/**
 * Created by DPSUser on 10/14/2016.
 */

public class HackerNewsApplication extends Application {
    private ItemRepositoryComponent itemRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);

        itemRepositoryComponent = DaggerItemRepositoryComponent.builder()
                .appModule(new AppModule(this))
                .aPIModule(new APIModule("https://hacker-news.firebaseio.com"))
                .netModule(new NetModule())
                .schedulersModule(new SchedulersModule())
                .androidAPIModule(new AndroidAPIModule())
                .itemDataSourceModule(new ItemDataSourceModule())
                .build();
    }

    public ItemRepositoryComponent getItemRepositoryComponent()
    {
        return itemRepositoryComponent;
    }
}
