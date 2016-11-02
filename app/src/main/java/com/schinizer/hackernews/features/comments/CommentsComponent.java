package com.schinizer.hackernews.features.comments;

import com.schinizer.hackernews.data.ItemRepositoryComponent;
import com.schinizer.hackernews.utility.ActivityScoped;
import com.schinizer.hackernews.utility.schedulers.SchedulersModule;

import dagger.Component;

/**
 * Created by DPSUser on 10/29/2016.
 */

@ActivityScoped
@Component(dependencies = {ItemRepositoryComponent.class}, modules = {CommentsPresenterModule.class, SchedulersModule.class})
public interface CommentsComponent {
    void inject(CommentsActivity activity);
    void inject(CommentsAdapter.ViewHolder viewHolder);
}
