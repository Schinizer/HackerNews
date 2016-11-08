package com.schinizer.hackernews.data.local;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
 * Simple local data source using shared preference
 * Not the best practice using shared preference as a store, but showcases the use case well
 * Proper case should use realm or sqlite etc
 */
@Singleton
public class ItemLocalDataSource implements ItemDataSource {

    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Inject
    public ItemLocalDataSource(SharedPreferences sharedPreferences, Gson gson)
    {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    @Override
    public Observable<List<Integer>> getTop500Stories() {
        List<Integer> data = gson.fromJson(sharedPreferences.getString("top500Stories", ""), new TypeToken<List<Integer>>(){}.getType());
        return Observable.just(data);
    }

    @Override
    public Observable<List<Item>> getItems(@NonNull List<Integer> ids) {
        return Observable.from(ids)
                .map(new Func1<Integer, Item>() {
                    @Override
                    public Item call(Integer id) {
                        return gson.fromJson(sharedPreferences.getString(id.toString(), ""), Item.class);
                    }
                })
                .toList();
    }

    @Override
    public Observable<Item> getItem(@NonNull Integer id) {
        return Observable.just(gson.fromJson(sharedPreferences.getString(id.toString(), ""), Item.class));
    }

    @Override
    public Observable<Boolean> getItemRefresh(@NonNull Integer id) {
        return Observable.just(sharedPreferences.getBoolean(id.toString() + "_refresh", true));
    }

    @Override
    public void saveItem(@NonNull Item item) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(item.id().toString(), gson.toJson(item));
        editor.apply();
    }

    @Override
    public void saveTop500Stories(@NonNull List<Integer> ids) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("top500Stories", gson.toJson(ids));
        editor.apply();
    }

    @Override
    public void refreshTop500Stories() {

    }

    @Override
    public void markItemForRefresh(@NonNull Integer id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(id.toString() + "_refresh", true);
        editor.apply();
    }

    @Override
    public void markItemRefreshed(@NonNull Integer id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(id.toString() + "_refresh", false);
        editor.apply();
    }
}
