package com.schinizer.hackernews;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.schinizer.hackernews.data.APIModule;
import com.schinizer.hackernews.data.AndroidAPIModule;
import com.schinizer.hackernews.data.DaggerItemRepositoryComponent;
import com.schinizer.hackernews.data.ItemDataSourceModule;
import com.schinizer.hackernews.data.ItemRepositoryComponent;
import com.schinizer.hackernews.data.NetModule;
import com.schinizer.hackernews.utility.schedulers.SchedulersModule;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by DPSUser on 10/14/2016.
 */

public class HackerNewsApplication extends Application {
    private ItemRepositoryComponent itemRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }

        LeakCanary.install(this);
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
