package com.schinizer.hackernews.features.newsfeed;

import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.data.ItemRepository;
import com.schinizer.hackernews.utility.schedulers.BaseSchedulerProvider;
import com.schinizer.hackernews.utility.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import rx.Observable;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by DPSUser on 11/7/2016.
 */

public class NewsFeedPresenterTest {

    @Mock
    private NewsFeedContract.View view;

    @Mock
    private ItemRepository itemRepository;

    private BaseSchedulerProvider schedulerProvider;

    private NewsFeedPresenter presenter;

    @Before
    public void setupCommentsPresenter()
    {
        MockitoAnnotations.initMocks(this);

        schedulerProvider = new ImmediateSchedulerProvider();

        presenter = new NewsFeedPresenter(itemRepository, view, schedulerProvider);
    }

    @Test
    public void loadTop500StoriesIntoView()
    {
        when(itemRepository.getTop500Stories()).thenReturn(Observable.range(0, 500).toList());
        when(itemRepository.getItems(ArgumentMatchers.<Integer>anyList())).thenReturn(Observable.just(Item.createEmpty(0), Item.createEmpty(1)).toList());

        presenter.loadTop500Stories(false); // first update
        presenter.loadTop500Stories(false); // local update

        verify(itemRepository).refreshTop500Stories();
        verify(view, times(2)).populateView(ArgumentMatchers.<Item>anyList());
    }

    @Test
    public void loadTop500StoriesIntoViewForcedUpdate()
    {
        when(itemRepository.getTop500Stories()).thenReturn(Observable.range(0, 500).toList());
        when(itemRepository.getItems(ArgumentMatchers.<Integer>anyList())).thenReturn(Observable.just(Item.createEmpty(0), Item.createEmpty(1)).toList());

        presenter.loadTop500Stories(true);

        verify(itemRepository).refreshTop500Stories();
        verify(view).clearView();
        verify(view).populateView(ArgumentMatchers.<Item>anyList());
    }

    @Test
    public void loadTop500StoriesError()
    {
        when(itemRepository.getTop500Stories()).thenReturn(Observable.<List<Integer>>error(new Exception()));

        presenter.loadTop500Stories(false);

        verify(itemRepository).refreshTop500Stories();
        verify(view).showNetworkError();
    }

    @Test
    public void pageStoriesIntoView()
    {
        when(itemRepository.getTop500Stories()).thenReturn(Observable.range(0, 500).toList());
        when(itemRepository.getItems(ArgumentMatchers.<Integer>anyList())).thenReturn(Observable.just(Item.createEmpty(0), Item.createEmpty(1)).toList());

        presenter.pageStories(0);

        verify(view).populateView(ArgumentMatchers.<Item>anyList());
    }

    @Test
    public void pageStoriesError()
    {
        when(itemRepository.getTop500Stories()).thenReturn(Observable.<List<Integer>>error(new Exception()));

        presenter.pageStories(0);

        verify(view).showNetworkError();
    }
}
