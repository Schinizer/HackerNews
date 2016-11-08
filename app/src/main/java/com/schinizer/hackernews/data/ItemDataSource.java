package com.schinizer.hackernews.data;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;

/**
 * Created by DPSUser on 10/18/2016.
 */

public interface ItemDataSource {

    Observable<List<Integer>> getTop500Stories();

    Observable<List<Item>> getItems(@NonNull List<Integer> ids);

    Observable<Item> getItem(@NonNull Integer id);

    Observable<Boolean> getItemRefresh(@NonNull Integer id);

    void saveItem(@NonNull Item item);

    void saveTop500Stories(@NonNull List<Integer> ids);

    void markItemForRefresh(@NonNull Integer id);
    void markItemRefreshed(@NonNull Integer id);

    void refreshTop500Stories();
}
