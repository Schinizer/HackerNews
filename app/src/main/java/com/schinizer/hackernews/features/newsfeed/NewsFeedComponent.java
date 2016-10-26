package com.schinizer.hackernews.features.newsfeed;

import com.schinizer.hackernews.data.ItemRepositoryComponent;
import com.schinizer.hackernews.utility.schedulers.SchedulersModule;
import com.schinizer.hackernews.utility.ActivityScoped;

import dagger.Component;

/**
 * Created by DPSUser on 10/25/2016.
 */

@ActivityScoped
@Component(dependencies = {ItemRepositoryComponent.class}, modules = {NewsFeedPresenterModule.class, SchedulersModule.class})
public interface NewsFeedComponent {
    void inject(NewsFeedActivity activity);
}
