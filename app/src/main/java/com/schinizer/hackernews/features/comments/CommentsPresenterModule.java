package com.schinizer.hackernews.features.comments;

import dagger.Module;
import dagger.Provides;

/**
 * Created by DPSUser on 10/29/2016.
 */

@Module
public class CommentsPresenterModule {
    private CommentsContract.View view;

    public CommentsPresenterModule(CommentsContract.View view) {
        this.view = view;
    }

    @Provides
    CommentsContract.View providesNewsFeedContractView() { return view; }
}
