package com.schinizer.hackernews.features.newsfeed;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.malinskiy.superrecyclerview.OnMoreListener;
import com.schinizer.hackernews.HackerNewsApplication;
import com.schinizer.hackernews.R;
import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.databinding.ActivityNewsfeedBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NewsFeedActivity extends AppCompatActivity implements NewsFeedContract.View, OnMoreListener {

    @Inject NewsFeedPresenter presenter;
    @Inject NewsFeedAdapter adapter;

    ActivityNewsfeedBinding binding;
    RecyclerView.LayoutManager layoutManager;
    Parcelable recyclerViewState;
    ArrayList<Item> adapterState;
    Boolean firstUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_newsfeed);
        
        DaggerNewsFeedComponent.builder()
                .itemRepositoryComponent(((HackerNewsApplication)getApplication()).getItemRepositoryComponent())
                .newsFeedPresenterModule(new NewsFeedPresenterModule(this))
                .build()
                .inject(this);

        layoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(layoutManager);

        binding.recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.recyclerView.setOnMoreListener(NewsFeedActivity.this);
                presenter.loadTop500Stories(true);
            }
        });
        binding.recyclerView.getSwipeToRefresh().setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));

        binding.recyclerView.setOnMoreListener(this);

        getSupportActionBar().setTitle(R.string.activity_title);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        recyclerViewState = layoutManager.onSaveInstanceState();
        adapterState = adapter.onSaveInstanceState();
        firstUpdate = presenter.getFirstUpdate();
        outState.putParcelable("recyclerView", recyclerViewState);
        outState.putParcelableArrayList("recyclerViewAdapter", adapterState);
        outState.putBoolean("firstUpdate", firstUpdate);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable("recyclerView");
            adapterState = savedInstanceState.getParcelableArrayList("recyclerViewAdapter");
            firstUpdate = savedInstanceState.getBoolean("firstUpdate");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(recyclerViewState != null && adapterState != null && firstUpdate != null)
        {
            adapter.addItems(adapterState);
            layoutManager.onRestoreInstanceState(recyclerViewState);
            presenter.setFirstUpdate(firstUpdate);

            adapterState = null;
            recyclerViewState = null;
            firstUpdate = null;
        }

        binding.recyclerView.setRefreshing(true);
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void onMoreAsked(int overallItemsCount, int itemsBeforeMore, int maxLastVisiblePosition) {
        if(overallItemsCount + presenter.PAGING_SIZE >= 500) { // Remove paging once we hit 500 stories
            binding.recyclerView.removeMoreListener();
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
        Snackbar.make(findViewById(android.R.id.content), R.string.newsfeed_networkerror_text, Snackbar.LENGTH_LONG)
                .show();
        binding.recyclerView.getSwipeToRefresh().setRefreshing(false);
    }
}
