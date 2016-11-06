package com.schinizer.hackernews.features.comments;

import android.support.annotation.NonNull;
import android.util.Log;

import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.data.ItemRepository;
import com.schinizer.hackernews.utility.schedulers.BaseSchedulerProvider;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by DPSUser on 10/29/2016.
 */

public class CommentsPresenter implements CommentsContract.Presenter {

    private ItemRepository itemRepository;
    private CommentsContract.View view;
    private BaseSchedulerProvider schedulerProvider;

    private CompositeSubscription subscriptions;

    @Inject
    public CommentsPresenter(@NonNull ItemRepository itemRepository,
                             @NonNull CommentsContract.View view,
                             @NonNull BaseSchedulerProvider schedulerProvider)
    {
        this.itemRepository = itemRepository;
        this.view = view;
        this.schedulerProvider = schedulerProvider;
        this.subscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void loadComment(Integer id, final Boolean forceUpdate) {

        if(forceUpdate) {
            itemRepository.markItemForRefresh(id);
        }

        subscriptions.clear();
        Subscription subscription = itemRepository.getItem(id)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(new Subscriber<Item>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Item Repository", "Subscription OnError()", e);
                        view.showNetworkError();
                    }

                    @Override
                    public void onNext(Item item) {
                        if(forceUpdate && item.kids() != null)
                        {
                            for(Integer id : item.kids())
                            {
                                itemRepository.markItemForRefresh(id);
                            }
                        }

                        view.populateComments(item, forceUpdate);
                    }
                });

        subscriptions.add(subscription);
    }
}
