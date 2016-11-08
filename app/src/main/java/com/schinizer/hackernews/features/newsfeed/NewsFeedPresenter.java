package com.schinizer.hackernews.features.newsfeed;

import android.support.annotation.NonNull;

import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.data.ItemRepository;
import com.schinizer.hackernews.utility.schedulers.BaseSchedulerProvider;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by DPSUser on 10/25/2016.
 */

public class NewsFeedPresenter implements NewsFeedContract.Presenter {

    public final Integer PAGING_SIZE = 25;

    private ItemRepository itemRepository;
    private NewsFeedContract.View view;
    private BaseSchedulerProvider schedulerProvider;

    private CompositeSubscription subscriptions;
    private Boolean firstUpdate = true;

    @Inject
    NewsFeedPresenter(@NonNull ItemRepository itemRepository,
                      @NonNull NewsFeedContract.View view,
                      @NonNull BaseSchedulerProvider schedulerProvider)
    {
        this.itemRepository = itemRepository;
        this.view = view;
        this.schedulerProvider = schedulerProvider;
        subscriptions = new CompositeSubscription();
    }

    public Boolean getFirstUpdate()
    {
        return firstUpdate;
    }

    public void setFirstUpdate(Boolean firstUpdate)
    {
        this.firstUpdate = firstUpdate;
    }

    @Override
    public void loadTop500Stories(final Boolean forceUpdate)
    {
        if(forceUpdate || firstUpdate) {
            itemRepository.refreshTop500Stories();
        }

        subscriptions.clear();
        Subscription subscription = itemRepository.getTop500Stories()
                .flatMap(new Func1<List<Integer>, Observable<List<Item>>>() {
                    @Override
                    public Observable<List<Item>> call(List<Integer> ids) {
                        return itemRepository.getItems(ids.subList(0, PAGING_SIZE));
                    }
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(new Subscriber<List<Item>>() {
                    @Override
                    public void onCompleted() {
                        firstUpdate = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showNetworkError();
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        if(forceUpdate) {
                            view.clearView();
                        }
                        view.populateView(items);
                    }
                });

        subscriptions.add(subscription);
    }

    @Override
    public void pageStories(final Integer offset) {
        subscriptions.clear();
        Subscription subscription = itemRepository.getTop500Stories()
                .flatMap(new Func1<List<Integer>, Observable<List<Item>>>() {
                    @Override
                    public Observable<List<Item>> call(List<Integer> ids) {
                        return itemRepository.getItems(ids.subList(offset, offset + PAGING_SIZE));
                    }
                })
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe(new Subscriber<List<Item>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showNetworkError();
                    }

                    @Override
                    public void onNext(List<Item> items) {
                        view.populateView(items);
                    }
                });

        subscriptions.add(subscription);
    }

    @Override
    public void subscribe() {
        loadTop500Stories(false);
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }
}
