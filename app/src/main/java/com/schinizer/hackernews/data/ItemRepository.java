package com.schinizer.hackernews.data;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by DPSUser on 10/21/2016.
 */

public class ItemRepository implements ItemDataSource {

    private ItemDataSource itemLocalDataSource;
    private ItemDataSource itemRemoteDataSource;

    private Map<Integer, Pair<Item, Boolean>> cachedItems = new LinkedHashMap<>(); // Pair with boolean to notify repository the need to refresh
    private List<Integer> cachedTop100Stories = new ArrayList<>();

    private Boolean cacheIsDirty = false;

    @Inject
    ItemRepository(@Local ItemDataSource itemLocalDataSource,
                   @Remote ItemDataSource itemRemoteDataSource) {
        this.itemLocalDataSource = itemLocalDataSource;
        this.itemRemoteDataSource = itemRemoteDataSource;
    }

    @Override
    public Observable<List<Integer>> getTop500Stories() {

        if (cachedTop100Stories != null && !cacheIsDirty) {
            return Observable.just(cachedTop100Stories);
        }

        Observable<List<Integer>> remoteStories = getAndSaveRemoteTop100Stories();

        if (cacheIsDirty) {
            return remoteStories;
        } else {
            Observable<List<Integer>> localStories = getAndCacheLocalTop100Stories();
            return Observable.concat(localStories, remoteStories)
                    .filter(new Func1<List<Integer>, Boolean>() {
                        @Override
                        public Boolean call(List<Integer> ids) {
                            return !ids.isEmpty();
                        }
                    })
                    .first();
        }
    }

    private Observable<List<Integer>> getAndCacheLocalTop100Stories() {
        return itemLocalDataSource.getTop500Stories()
                .doOnNext(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> ids) {
                        cachedTop100Stories.clear();
                        cachedTop100Stories.addAll(ids);
                    }
                });
    }

    private Observable<List<Integer>> getAndSaveRemoteTop100Stories() {
        return itemRemoteDataSource.getTop500Stories()
                .doOnNext(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> ids) {
                        cachedTop100Stories.clear();
                        cachedTop100Stories.addAll(ids);
                        itemLocalDataSource.saveTop100Stories(ids);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        cacheIsDirty = false;
                    }
                });
    }

    @Override
    public void refreshTop500Stories() {
        cacheIsDirty = true;
        for(Map.Entry<Integer, Pair<Item, Boolean>> entry : cachedItems.entrySet()) {
            cachedItems.put(entry.getKey(), Pair.create(entry.getValue().first, true));
        }
    }

    @Override
    public Observable<List<Item>> getItems(@NonNull List<Integer> ids) {
        return Observable.from(ids)
                .flatMap(new Func1<Integer, Observable<Item>>() {
                    @Override
                    public Observable<Item> call(Integer id) {
                        return getItem(id);
                    }
                })
                .toList();
    }

    @Override
    public Observable<Item> getItem(@NonNull final Integer id) {

        Pair<Item, Boolean> cachedItem = getCachedItem(id);

        if (cachedItem != null && !cachedItem.second) {
            return Observable.just(cachedItem.first);
        }

        Observable<Item> remoteItem = itemRemoteDataSource
                .getItem(id)
                .doOnNext(new Action1<Item>() {
                    @Override
                    public void call(Item item) {
                        itemLocalDataSource.saveItem(item);
                        cachedItems.put(id, Pair.create(item, false));
                    }
                });

        if(cachedItem != null && cachedItem.second) {
            return remoteItem;
        }
        else
        {
            Observable<Item> localItem = itemLocalDataSource
                    .getItem(id)
                    .doOnNext(new Action1<Item>() {
                        @Override
                        public void call(Item item) {
                            cachedItems.put(id, Pair.create(item, false));
                        }
                    });

            return Observable.concat(localItem, remoteItem)
                    .filter(new Func1<Item, Boolean>() {
                        @Override
                        public Boolean call(Item item) {
                            return item != null;
                        }
                    })
                    .first();
        }
    }

    private Pair<Item, Boolean> getCachedItem(@NonNull Integer id) {
        if (cachedItems == null || cachedItems.isEmpty()) {
            return null;
        } else {
            return cachedItems.get(id);
        }
    }

    @Override
    public void saveItem(@NonNull Item item) {
        itemLocalDataSource.saveItem(item);
        cachedItems.put(item.id(), Pair.create(item, false));
    }

    @Override
    public void saveTop100Stories(@NonNull List<Integer> ids) {
        itemLocalDataSource.saveTop100Stories(ids);
        cachedTop100Stories.clear();
        cachedTop100Stories.addAll(ids);
    }
}
