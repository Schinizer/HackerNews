package com.schinizer.hackernews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new UnixTimeDeserializer())
                .create();

        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://hacker-news.firebaseio.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(rxAdapter)
                .client(okHttpClient)
                .build();

        HackerNewsAPI hackerNewsAPI = retrofit.create(HackerNewsAPI.class);

        Observable<Item> call = hackerNewsAPI.item(12697561);
        Observable<List<Integer>> call2 = hackerNewsAPI.top500Stories();

        Observable.zip(call, call2, new Func2<Item, List<Integer>, Item>() {
            @Override
            public Item call(Item item, List<Integer> integers) {
                return item;
            }
        })
                .subscribe(new Subscriber<Item>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("HackerNewsAPI", e.toString());
                    }

                    @Override
                    public void onNext(Item item) {
                        Log.d("HackerNewsAPI", item.toString());
                    }
                });
    }
}
