package com.schinizer.hackernews.data;

import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by DPSUser on 11/7/2016.
 */

public class ItemRepositoryTest {

    @Mock
    private ItemDataSource localDataSource;

    @Mock
    private ItemDataSource remoteDataSource;

    private ItemRepository itemRepository;
    private List<Item> items = Lists.newArrayList(Item.createEmpty(0), Item.createEmpty(1), Item.createEmpty(3));
    private List<Integer> mockIds = Observable.range(0, 500).toList().toBlocking().first();

    @Before
    public void setupItemRepository()
    {
        MockitoAnnotations.initMocks(this);

        itemRepository = new ItemRepository(localDataSource, remoteDataSource);
    }

    @Test
    public void testGetTop500StoriesLocalSource()
    {
        setTop500StoriesAvailable(localDataSource, mockIds);
        setTop500StoriesNotAvailable(remoteDataSource);

        TestSubscriber<List<Integer>> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getTop500Stories().subscribe(testSubscriber1);

        verify(localDataSource).getTop500Stories();
        testSubscriber1.assertValue(mockIds);
    }

    @Test
    public void testGetTop500StoriesRemoteSource()
    {
        setTop500StoriesAvailable(remoteDataSource, mockIds);
        setTop500StoriesNotAvailable(localDataSource);

        TestSubscriber<List<Integer>> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getTop500Stories().subscribe(testSubscriber1);

        verify(remoteDataSource).getTop500Stories();
        testSubscriber1.assertValue(mockIds);
    }

    @Test
    public void testGetTop500StoriesCacheAfterLocalSource()
    {
        setTop500StoriesAvailable(localDataSource, mockIds);
        setTop500StoriesNotAvailable(remoteDataSource);

        TestSubscriber<List<Integer>> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getTop500Stories().subscribe(testSubscriber1);

        TestSubscriber<List<Integer>> testSubscriber2 = new TestSubscriber<>();
        itemRepository.getTop500Stories().subscribe(testSubscriber2);

        verify(localDataSource).getTop500Stories();
        verify(remoteDataSource).getTop500Stories();

        assertFalse(itemRepository.cacheIsDirty);
        testSubscriber1.assertValue(mockIds);
        testSubscriber2.assertValue(mockIds);
    }

    @Test
    public void testGetTop500StoriesCacheAfterRemoteSource()
    {
        setTop500StoriesAvailable(remoteDataSource, mockIds);
        setTop500StoriesNotAvailable(localDataSource);

        TestSubscriber<List<Integer>> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getTop500Stories().subscribe(testSubscriber1);

        TestSubscriber<List<Integer>> testSubscriber2 = new TestSubscriber<>();
        itemRepository.getTop500Stories().subscribe(testSubscriber2);

        verify(localDataSource).getTop500Stories();
        verify(remoteDataSource).getTop500Stories();

        assertFalse(itemRepository.cacheIsDirty);
        testSubscriber1.assertValue(mockIds);
        testSubscriber2.assertValue(mockIds);
    }

    @Test
    public void testGetTop500StoriesDirtyCache()
    {
        setTop500StoriesAvailable(remoteDataSource, mockIds);

        itemRepository.refreshTop500Stories();

        TestSubscriber<List<Integer>> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getTop500Stories().subscribe(testSubscriber1);

        verify(localDataSource, never()).getTop500Stories();
        verify(remoteDataSource).getTop500Stories();
        testSubscriber1.assertValue(mockIds);
    }

    @Test
    public void testGetTop500StoriesNoSource()
    {
        setTop500StoriesNotAvailable(localDataSource);
        setTop500StoriesNotAvailable(remoteDataSource);

        TestSubscriber<List<Integer>> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getTop500Stories().subscribe(testSubscriber1);

        testSubscriber1.assertNoValues();
    }

    @Test
    public void testSaveTop100Stories()
    {
        itemRepository.saveTop500Stories(mockIds);
        verify(localDataSource).saveTop500Stories(ArgumentMatchers.<Integer>anyList());
        assertThat(itemRepository.cachedTop100Stories.size(), is(500));
    }

    @Test
    public void testGetStoryLocalSource()
    {
        Item mockItem = Item.createEmpty(0);

        setItemAvailable(localDataSource, mockItem);
        setItemNotAvailable(remoteDataSource, mockItem.id());
        setItemRefreshed(localDataSource, mockItem.id());

        TestSubscriber<Item> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getItem(mockItem.id()).subscribe(testSubscriber1);

        verify(localDataSource).getItemRefresh(mockItem.id());
        verify(localDataSource).getItem(mockItem.id());
        testSubscriber1.assertValue(mockItem);
    }

    @Test
    public void testGetStoryLocalSourceFails()
    {
        Item mockItem = Item.createEmpty(0);

        setItemAvailable(remoteDataSource, mockItem);
        setItemNotAvailable(localDataSource, mockItem.id());
        setItemRefreshed(localDataSource, mockItem.id());

        TestSubscriber<Item> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getItem(mockItem.id()).subscribe(testSubscriber1);

        verify(localDataSource).getItemRefresh(mockItem.id());
        verify(localDataSource).getItem(mockItem.id());
        verify(remoteDataSource).getItem(mockItem.id());
        testSubscriber1.assertValue(mockItem);
    }

    @Test
    public void testGetStoryLocalSourceCache()
    {
        Item mockItem = Item.createEmpty(0);

        setItemAvailable(localDataSource, mockItem);
        setItemNotAvailable(remoteDataSource, mockItem.id());
        setItemRefreshed(localDataSource, mockItem.id());

        TestSubscriber<Item> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getItem(mockItem.id()).subscribe(testSubscriber1);

        TestSubscriber<Item> testSubscriber2 = new TestSubscriber<>();
        itemRepository.getItem(mockItem.id()).subscribe(testSubscriber2);

        verify(localDataSource, times(2)).getItemRefresh(mockItem.id());
        verify(localDataSource).getItem(mockItem.id());
        verify(remoteDataSource, times(2)).getItem(mockItem.id());
        assertThat(itemRepository.cachedItems.containsKey(mockItem.id()), is(true));
        testSubscriber1.assertValue(mockItem);
    }

    @Test
    public void testGetStoryRemoteSourceCache()
    {
        Item mockItem = Item.createEmpty(0);

        setItemAvailable(remoteDataSource, mockItem);
        setItemNotAvailable(localDataSource, mockItem.id());
        setItemRefreshed(localDataSource, mockItem.id());

        TestSubscriber<Item> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getItem(mockItem.id()).subscribe(testSubscriber1);

        TestSubscriber<Item> testSubscriber2 = new TestSubscriber<>();
        itemRepository.getItem(mockItem.id()).subscribe(testSubscriber2);

        verify(localDataSource, times(2)).getItemRefresh(mockItem.id());
        verify(localDataSource).getItem(mockItem.id());
        verify(remoteDataSource, times(2)).getItem(mockItem.id());
        assertThat(itemRepository.cachedItems.containsKey(mockItem.id()), is(true));
        testSubscriber1.assertValue(mockItem);
    }

    @Test
    public void testGetStoryNoSource()
    {
        Item mockItem = Item.createEmpty(0);

        setItemNotAvailable(remoteDataSource, mockItem.id());
        setItemNotAvailable(localDataSource, mockItem.id());
        setItemRefreshed(localDataSource, mockItem.id());

        TestSubscriber<Item> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getItem(mockItem.id()).subscribe(testSubscriber1);

        testSubscriber1.assertNoValues();
    }

    @Test
    public void testGetStoryForceRefreshFromRemoteOnly()
    {
        Item mockItem = Item.createEmpty(0);

        setItemAvailable(remoteDataSource, mockItem);
        setItemToRefresh(localDataSource, mockItem.id());

        TestSubscriber<Item> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getItem(mockItem.id()).subscribe(testSubscriber1);

        verify(localDataSource).getItemRefresh(mockItem.id());
        verify(localDataSource, never()).getItem(mockItem.id());
        verify(remoteDataSource).getItem(mockItem.id());
        testSubscriber1.assertValue(mockItem);
    }

    @Test
    public void testGetItemRefreshOnLocalSourceOnly()
    {
        Item mockItem = Item.createEmpty(0);

        setItemToRefresh(localDataSource, mockItem.id());

        TestSubscriber<Boolean> testSubscriber1 = new TestSubscriber<>();
        itemRepository.getItemRefresh(mockItem.id()).subscribe(testSubscriber1);

        verify(localDataSource).getItemRefresh(mockItem.id());
        verify(remoteDataSource, never()).getItemRefresh(mockItem.id());
        testSubscriber1.assertValue(true);
    }

    @Test
    public void testMarkItemForRefreshLocalSourceOnly()
    {
        Item mockItem = Item.createEmpty(0);

        itemRepository.markItemForRefresh(mockItem.id());

        verify(localDataSource).markItemForRefresh(mockItem.id());
        verify(remoteDataSource, never()).markItemForRefresh(mockItem.id());
    }

    @Test
    public void testMarkItemRefreshedLocalSourceOnly()
    {
        Item mockItem = Item.createEmpty(0);

        itemRepository.markItemRefreshed(mockItem.id());

        verify(localDataSource).markItemRefreshed(mockItem.id());
        verify(remoteDataSource, never()).markItemRefreshed(mockItem.id());
    }

    private void setTop500StoriesNotAvailable(ItemDataSource dataSource) {
        when(dataSource.getTop500Stories()).thenReturn(Observable.just(Collections.<Integer>emptyList()));
    }

    private void setTop500StoriesAvailable(ItemDataSource dataSource, List<Integer> ids) {
        // don't allow the data sources to complete.
        when(dataSource.getTop500Stories()).thenReturn(Observable.just(ids).concatWith(Observable.<List<Integer>>never()));
    }

    private void setItemNotAvailable(ItemDataSource dataSource, Integer itemId) {
        when(dataSource.getItem(eq(itemId))).thenReturn(Observable.<Item>just(null));
    }

    private void setItemAvailable(ItemDataSource dataSource, Item item) {
        when(dataSource.getItem(eq(item.id()))).thenReturn(Observable.just(item));
    }

    private void setItemToRefresh(ItemDataSource dataSource, Integer id)
    {
        when(dataSource.getItemRefresh(eq(id))).thenReturn(Observable.just(true));
    }

    private void setItemRefreshed(ItemDataSource dataSource, Integer id)
    {
        when(dataSource.getItemRefresh(eq(id))).thenReturn(Observable.just(false));
    }
}
