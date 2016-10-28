package com.schinizer.hackernews.features.comments;

import com.schinizer.hackernews.data.Item;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DPSUser on 10/29/2016.
 */

@Module
public class CommentsPresenterModule {
    private CommentsContract.View view;
    private Item item;

    public CommentsPresenterModule(CommentsContract.View view, Item item) {
        this.view = view;
        this.item = item;
    }

    @Provides
    CommentsContract.View providesNewsFeedContractView() { return view; }

    @Provides
    Item providesItem() { return  item; }
}
