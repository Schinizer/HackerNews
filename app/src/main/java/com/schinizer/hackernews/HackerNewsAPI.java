package com.schinizer.hackernews;

import com.schinizer.hackernews.data.Item;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by DPSUser on 10/13/2016.
 */

public interface HackerNewsAPI {
    @GET("v0/topstories.json")
    Observable<List<Integer>> top500Stories();

    @GET("v0/item/{id}.json")
    Observable<Item> item(@Path("id") Integer id);
}
