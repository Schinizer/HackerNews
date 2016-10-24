package com.schinizer.hackernews.data;

import android.support.annotation.NonNull;

import com.schinizer.hackernews.Item;
import com.schinizer.hackernews.dagger2.scopes.Local;
import com.schinizer.hackernews.dagger2.scopes.Remote;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

    private Map<Integer, Item> cachedItems = new LinkedHashMap<>();
    private List<Integer> cachedTop100Stories = new ArrayList<>();

    private Boolean cacheIsDirty = false;

    @Inject
    ItemRepository(@Local ItemDataSource itemLocalDataSource,
                   @Remote ItemDataSource itemRemoteDataSource) {
        this.itemLocalDataSource = itemLocalDataSource;
        this.itemRemoteDataSource = itemRemoteDataSource;
    }

    @Override
    public Observable<List<Integer>> getTop100Stories() {

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
        return itemLocalDataSource.getTop100Stories()
                .doOnNext(new Action1<List<Integer>>() {
                    @Override
                    public void call(List<Integer> ids) {
                        cachedTop100Stories.clear();
                        cachedTop100Stories.addAll(ids);
                    }
                });
    }

    private Observable<List<Integer>> getAndSaveRemoteTop100Stories() {
        return itemRemoteDataSource.getTop100Stories()
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

        Item cachedItem = getCachedItem(id);

        if (cachedItem != null) {
            return Observable.just(cachedItem);
        }

        Observable<Item> localItem = itemLocalDataSource
                .getItem(id)
                .doOnNext(new Action1<Item>() {
                    @Override
                    public void call(Item item) {
                        cachedItems.put(item.id(), item);
                    }
                })
                .first();

        Observable<Item> remoteItem = itemRemoteDataSource
                .getItem(id)
                .doOnNext(new Action1<Item>() {
                    @Override
                    public void call(Item item) {
                        itemLocalDataSource.saveItem(item);
                        cachedItems.put(item.id(), item);
                    }
                })
                .first();

        return Observable.concat(localItem, remoteItem)
                .first()
                .map(new Func1<Item, Item>() {
                    @Override
                    public Item call(Item item) {
                        if (item == null) {
                            throw new NoSuchElementException("No item found with id " + id.toString());
                        }
                        return item;
                    }
                });
    }

    private Item getCachedItem(@NonNull Integer id) {
        if (cachedItems == null || cachedItems.isEmpty()) {
            return null;
        } else {
            return cachedItems.get(id);
        }
    }

    @Override
    public void saveItem(@NonNull Item item) {
        itemLocalDataSource.saveItem(item);
        cachedItems.put(item.id(), item);
    }

    @Override
    public void saveTop100Stories(@NonNull List<Integer> ids) {
        itemLocalDataSource.saveTop100Stories(ids);
        cachedTop100Stories.clear();
        cachedTop100Stories.addAll(ids);
    }
}
