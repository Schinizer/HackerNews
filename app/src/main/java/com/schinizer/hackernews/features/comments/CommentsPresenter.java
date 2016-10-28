package com.schinizer.hackernews.features.comments;

import android.support.annotation.NonNull;

import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.data.ItemRepository;
import com.schinizer.hackernews.utility.schedulers.BaseSchedulerProvider;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by DPSUser on 10/29/2016.
 */

public class CommentsPresenter implements CommentsContract.Presenter {

    private ItemRepository itemRepository;
    private CommentsContract.View view;
    private Item item;
    private BaseSchedulerProvider schedulerProvider;

    private CompositeSubscription subscriptions;

    @Inject
    public CommentsPresenter(@NonNull ItemRepository itemRepository,
                             @NonNull CommentsContract.View view,
                             @NonNull Item item,
                             @NonNull BaseSchedulerProvider schedulerProvider)
    {
        this.itemRepository = itemRepository;
        this.view = view;
        this.item = item;
        this.schedulerProvider = schedulerProvider;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        loadStoryAndComments(false);
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadStoryAndComments(Boolean forceRefresh) {

    }
}
