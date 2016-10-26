package com.schinizer.hackernews.features.newsfeed;

import com.schinizer.hackernews.dagger2.components.ItemRepositoryComponent;
import com.schinizer.hackernews.dagger2.modules.SchedulersModule;
import com.schinizer.hackernews.dagger2.scopes.ActivityScoped;

import dagger.Component;

/**
 * Created by DPSUser on 10/25/2016.
 */

@ActivityScoped
@Component(dependencies = {ItemRepositoryComponent.class}, modules = {NewsFeedPresenterModule.class, SchedulersModule.class})
public interface NewsFeedComponent {
    void inject(NewsFeedActivity activity);
}
