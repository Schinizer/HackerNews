package com.schinizer.hackernews.data;

/**
 * Created by DPSUser on 10/14/2016.
 */

import com.google.gson.Gson;
import com.schinizer.hackernews.HackerNewsAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class APIModule {

    private String baseUrl = "";

    public APIModule(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    @Provides
    @Singleton
    HackerNewsAPI providesHackerNewsAPI(OkHttpClient okHttpClient, Gson gson, RxJavaCallAdapterFactory rxJavaCallAdapterFactory)
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return retrofit.create(HackerNewsAPI.class);
    }
}
