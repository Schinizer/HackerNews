package com.schinizer.hackernews.features.comments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.format.DateUtils;
import android.view.MenuItem;

import com.schinizer.hackernews.HackerNewsApplication;
import com.schinizer.hackernews.R;
import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.databinding.ActivityCommentsBinding;

import java.util.Calendar;
import java.util.Locale;

import javax.inject.Inject;

public class CommentsActivity extends AppCompatActivity implements CommentsContract.View {

    @Inject CommentsPresenter presenter;

    Item item;
    CommentsAdapter adapter;
    ActivityCommentsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_comments);
        DaggerCommentsComponent.builder()
                .itemRepositoryComponent(((HackerNewsApplication)getApplication()).getItemRepositoryComponent())
                .commentsPresenterModule(new CommentsPresenterModule(this))
                .build()
                .inject(this);

        item = getIntent().getParcelableExtra("data");

        adapter = new CommentsAdapter(getApplication());
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.colorAccent));
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateComments(item);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(item.title());
        if(item.by() != null && item.time() != null) {
            getSupportActionBar().setSubtitle(String.format(Locale.getDefault(), "%s by %s", DateUtils.getRelativeTimeSpanString((item.time() * 1000L), Calendar.getInstance().getTimeInMillis(), DateUtils.SECOND_IN_MILLIS, 0), item.by()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onResume() {
        super.onResume();
        binding.swipeRefreshLayout.setRefreshing(true);
        populateComments(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void populateComments(Item comment) {
        adapter.clearItems();
        adapter.populateComments(comment);
        binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showNetworkError() {
        Snackbar.make(findViewById(android.R.id.content), R.string.newsfeed_networkerror_text, Snackbar.LENGTH_LONG)
                .show();
        binding.swipeRefreshLayout.setRefreshing(false);
    }
}
