package com.schinizer.hackernews;

import android.app.Application;

import com.schinizer.hackernews.dagger2.components.DaggerItemRepositoryComponent;
import com.schinizer.hackernews.dagger2.components.ItemRepositoryComponent;
import com.schinizer.hackernews.dagger2.modules.APIModule;
import com.schinizer.hackernews.dagger2.modules.AndroidAPIModule;
import com.schinizer.hackernews.dagger2.modules.AppModule;
import com.schinizer.hackernews.dagger2.modules.ItemRepositoryModule;
import com.schinizer.hackernews.dagger2.modules.NetModule;

/**
 * Created by DPSUser on 10/14/2016.
 */

public class HackerNewsApplication extends Application {
    private ItemRepositoryComponent itemRepositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        itemRepositoryComponent = DaggerItemRepositoryComponent.builder()
                .appModule(new AppModule(this))
                .aPIModule(new APIModule("https://hacker-news.firebaseio.com"))
                .netModule(new NetModule())
                .androidAPIModule(new AndroidAPIModule())
                .itemRepositoryModule(new ItemRepositoryModule())
                .build();
    }

    ItemRepositoryComponent getItemRepositoryComponent()
    {
        return itemRepositoryComponent;
    }
}
