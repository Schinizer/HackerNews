package com.schinizer.hackernews.data.local;

import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.utility.AutoValueAdapterFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by DPSUser on 11/8/2016.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemLocalDataSourceTest {

    private ItemLocalDataSource localDataSource;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    private List<Integer> mockIds = Observable.range(0, 500).toList().toBlocking().first();
    private Item item = Item.builder()
            .setId(123456)
            .setBy("Some Dude")
            .build();

    @Before
    public void setup()
    {
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(AutoValueAdapterFactory.create())
                .create();

        sharedPreferences = getDefaultSharedPreferences(InstrumentationRegistry.getContext());
        localDataSource = new ItemLocalDataSource(sharedPreferences, gson);
    }

    @After
    public void cleanUp()
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
    }

    @Test
    public void testSaveAndGetTop500Stories() throws Exception {

        localDataSource.saveTop500Stories(mockIds);

        TestSubscriber<List<Integer>> testSubscriber = new TestSubscriber<>();
        localDataSource.getTop500Stories().subscribe(testSubscriber);
        testSubscriber.assertValue(mockIds);
    }

    @Test
    public void testGetEmptyTop500Stories() throws Exception {

        TestSubscriber<List<Integer>> testSubscriber = new TestSubscriber<>();
        localDataSource.getTop500Stories().subscribe(testSubscriber);
        testSubscriber.assertValue(null);
    }

    @Test
    public void testSaveAndGetItem() throws Exception
    {
        localDataSource.saveItem(item);

        TestSubscriber<Item> testSubscriber = new TestSubscriber<>();
        localDataSource.getItem(item.id()).subscribe(testSubscriber);
        testSubscriber.assertValue(item);
    }

    @Test
    public void testEmptyItem() throws Exception {

        TestSubscriber<Item> testSubscriber = new TestSubscriber<>();
        localDataSource.getItem(item.id()).subscribe(testSubscriber);
        testSubscriber.assertValue(null);
    }

    @Test
    public void testMarkItemRefreshAndGetResult() throws Exception {

        localDataSource.markItemForRefresh(item.id());

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        localDataSource.getItemRefresh(item.id()).subscribe(testSubscriber);
        testSubscriber.assertValue(true);
    }

    @Test
    public void testMarkItemRefreshedAndGetResult() throws Exception {

        localDataSource.markItemRefreshed(item.id());

        TestSubscriber<Boolean> testSubscriber = new TestSubscriber<>();
        localDataSource.getItemRefresh(item.id()).subscribe(testSubscriber);
        testSubscriber.assertValue(false);
    }
}
