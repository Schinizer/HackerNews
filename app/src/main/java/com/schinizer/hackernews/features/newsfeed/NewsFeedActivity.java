package com.schinizer.hackernews.features.newsfeed;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.schinizer.hackernews.HackerNewsApplication;
import com.schinizer.hackernews.R;
import com.schinizer.hackernews.data.Item;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedActivity extends AppCompatActivity implements NewsFeedContract.View, OnMoreListener {

    @BindView(R.id.recyclerView)
    SuperRecyclerView recyclerView;

    NewsFeedAdapter adapter;

    @Inject NewsFeedPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        ButterKnife.bind(this);

        DaggerNewsFeedComponent.builder()
                .itemRepositoryComponent(((HackerNewsApplication)getApplication()).getItemRepositoryComponent())
                .newsFeedPresenterModule(new NewsFeedPresenterModule(this))
                .build()
                .inject(this);

        adapter = new NewsFeedAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setupMoreListener(NewsFeedActivity.this, 10);
                presenter.loadTop500Stories(true);
            }
        });

        recyclerView.setupMoreListener(this, 10);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        if(overallItemsCount + presenter.PAGING_SIZE >= 500) { // Top stories only have 500 items
            recyclerView.removeMoreListener();
        }
        presenter.pageStories(overallItemsCount);
    }

    @Override
    public void populateView(List<Item> stories) {
        adapter.addItems(stories);
    }

    @Override
    public void clearView() {
        adapter.clearItems();
    }

    @Override
    public void showNetworkError() {
        Snackbar.make(findViewById(android.R.id.content), "Something went wrong..", Snackbar.LENGTH_LONG)
                .show();
        recyclerView.getSwipeToRefresh().setRefreshing(false);
    }
}
