package com.schinizer.hackernews.features.comments;

import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.data.ItemRepository;
import com.schinizer.hackernews.utility.schedulers.BaseSchedulerProvider;
import com.schinizer.hackernews.utility.schedulers.ImmediateSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;

import rx.Observable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by DPSUser on 11/7/2016.
 */

public class CommentsPresenterTest {

    @Mock
    private CommentsContract.View view;

    @Mock
    private ItemRepository itemRepository;

    private BaseSchedulerProvider schedulerProvider;

    private CommentsPresenter presenter;

    @Before
    public void setupCommentsPresenter()
    {
        MockitoAnnotations.initMocks(this);

        schedulerProvider = new ImmediateSchedulerProvider();

        presenter = new CommentsPresenter(itemRepository, view, schedulerProvider);
    }

    @Test
    public void loadCommentsIntoView()
    {
        when(itemRepository.getItem(anyInt())).thenReturn(Observable.just(Item.builder().setId(0).build()));
        presenter.loadComment(0, false);
        verify(view).populateComments(any(Item.class), eq(false));
    }

    @Test
    public void loadCommentsIntoViewForceUpdate()
    {
        when(itemRepository.getItem(anyInt())).thenReturn(Observable.just(Item.builder()
                .setId(0)
                .setKids(new ArrayList<>(Arrays.asList(1)))
                .build()));

        presenter.loadComment(0, true);

        verify(itemRepository, times(2)).markItemForRefresh(anyInt());
        verify(view).populateComments(any(Item.class), eq(true));
    }

    @Test
    public void loadCommentsError()
    {
        when(itemRepository.getItem(anyInt())).thenReturn(Observable.<Item>error(new Exception()));
        presenter.loadComment(0, false);
        verify(view).showNetworkError();
    }
}
