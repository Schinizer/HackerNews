package com.schinizer.hackernews.features.comments;

import android.support.annotation.NonNull;

import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.data.ItemRepository;
import com.schinizer.hackernews.utility.schedulers.BaseSchedulerProvider;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
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
    public void loadComment(Integer id) {

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

                    }

                    @Override
                    public void onNext(Item item) {
                        view.populateComments(item);
                    }
                });

        subscriptions.add(subscription);
    }

    private Observable<Item> getComments(Integer id)
    {
        return itemRepository.getItem(id)
                .flatMap(new Func1<Item, Observable<Item>>() {
                    @Override
                    public Observable<Item> call(Item item) {
                        if( item.kids() != null) {
                            Observable<Item> traverse = Observable.from(item.kids())
                                    .flatMap(new Func1<Integer, Observable<Item>>() {
                                        @Override
                                        public Observable<Item> call(Integer id) {
                                            return itemRepository.getItem(id);
                                        }
                                    });

                            return Observable.concat(Observable.just(item), traverse);
                        }
                        else {
                            return Observable.just(item);
                        }
                    }
                });
    }
}
