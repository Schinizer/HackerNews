package com.schinizer.hackernews.features.comments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.schinizer.hackernews.HackerNewsApplication;
import com.schinizer.hackernews.R;
import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.databinding.ActivityCommentsBinding;

import java.util.List;

import javax.inject.Inject;

public class CommentsActivity extends AppCompatActivity implements CommentsContract.View {

    ActivityCommentsBinding binding;
    Item item;

    @Inject CommentsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_comments);
        item = getIntent().getParcelableExtra("data");
        DaggerCommentsComponent.builder()
                .itemRepositoryComponent(((HackerNewsApplication)getApplication()).getItemRepositoryComponent())
                .commentsPresenterModule(new CommentsPresenterModule(this, item))
                .build()
                .inject(this);

        binding.newsLayout.setStory(item);
    }

    @Override
    public void populateComments(List<Item> comments) {

    }
}
