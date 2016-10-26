package com.schinizer.hackernews.features.newsfeed;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DPSUser on 10/25/2016.
 */

@Module
public class NewsFeedPresenterModule {

    private NewsFeedContract.View view;

    public NewsFeedPresenterModule(NewsFeedContract.View view) { this.view = view; }

    @Provides
    NewsFeedContract.View providesNewsFeedContractView() { return view; }
}
