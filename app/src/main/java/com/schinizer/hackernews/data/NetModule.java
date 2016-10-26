package com.schinizer.hackernews.data;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.schinizer.hackernews.BuildConfig;
import com.schinizer.hackernews.utility.AutoValueAdapterFactory;
import com.schinizer.hackernews.utility.schedulers.BaseSchedulerProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by DPSUser on 10/14/2016.
 */

@Module
public class NetModule {

    @Provides
    @Singleton
    Gson provideGson()
    {
        return new GsonBuilder()
                .registerTypeAdapterFactory(AutoValueAdapterFactory.create())
                .create();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor providesHttpLoggingInterceptor()
    {
        return new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    }

    @Provides
    @Singleton
    OkHttpClient providesOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor, StethoInterceptor stethoInterceptor)
    {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(stethoInterceptor)
                .build();
    }

    @Provides
    @Singleton
    RxJavaCallAdapterFactory providesRxJavaCallAdapterFactory(BaseSchedulerProvider schedulerProvider)
    {
        return RxJavaCallAdapterFactory.createWithScheduler(schedulerProvider.io());
    }

    @Provides
    @Singleton
    StethoInterceptor providesStethoInterceptor()
    {
        return new StethoInterceptor();
    }
}
