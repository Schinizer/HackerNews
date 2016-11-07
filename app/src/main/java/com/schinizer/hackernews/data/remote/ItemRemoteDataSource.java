package com.schinizer.hackernews.data.remote;

import android.support.annotation.NonNull;

import com.schinizer.hackernews.HackerNewsAPI;
import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.data.ItemDataSource;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by DPSUser on 10/19/2016.
 */

/**
 * Simple remote data source bridged with retrofit
 */
@Singleton
public class ItemRemoteDataSource implements ItemDataSource {

    private HackerNewsAPI api;

    @Inject
    public ItemRemoteDataSource(HackerNewsAPI api)
    {
        this.api = api;
    }

    @Override
    public Observable<Item> getItem(@NonNull Integer id) {
        return api.item(id);
    }

    @Override
    public Observable<List<Item>> getItems(@NonNull List<Integer> ids) {
        return Observable.from(ids)
                .concatMapEager(new Func1<Integer, Observable<Item>>() {
                    @Override
                    public Observable<Item> call(Integer id) {
                        return api.item(id);
                    }
                })
                .toList();
    }

    @Override
    public Observable<List<Integer>> getTop500Stories() {
        return api.top500Stories();
    }

    @Override
    public Observable<Boolean> getItemRefresh(@NonNull Integer id) {
        return Observable.just(true);
    }

    // We can't write to remote, so we leave it empty
    @Override
    public void saveItem(@NonNull Item item) {

    }

    @Override
    public void saveTop100Stories(@NonNull List<Integer> ids) {

    }

    @Override
    public void refreshTop500Stories() {

    }

    @Override
    public void markItemForRefresh(@NonNull Integer id) {

    }

    @Override
    public void markItemRefreshed(@NonNull Integer id) {

    }
}
