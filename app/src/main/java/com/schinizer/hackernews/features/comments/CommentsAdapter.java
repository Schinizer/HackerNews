package com.schinizer.hackernews.features.comments;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schinizer.hackernews.HackerNewsApplication;
import com.schinizer.hackernews.R;
import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.databinding.ViewCommentBinding;
import com.schinizer.hackernews.databinding.ViewNewsBinding;
import com.schinizer.hackernews.features.newsfeed.NewsFeedAdapter;

import javax.inject.Inject;

/**
 * Created by DPSUser on 10/31/2016.
 */

public class CommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SparseBooleanArray data = new SparseBooleanArray();
    private Context context;
    private Item root;

    final int STORY = 0, COMMENT = 1;

    public CommentsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType())
        {
            case STORY:
                NewsFeedAdapter.ViewHolder newsView = (NewsFeedAdapter.ViewHolder)holder;
                newsView.dataBinding.setStory(root);
                newsView.dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(root.url())));
                    }
                });
                break;
            default:
                ViewHolder commentsView = (ViewHolder)holder;
                Integer key = root.kids().get(position);
                commentsView.dataBinding.setStory(Item.createEmpty(key));
                commentsView.presenter.loadComment(key, data.get(key));
                data.put(key, false); // Set consume the update after loading
                break;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case STORY:
                return new NewsFeedAdapter.ViewHolder((ViewNewsBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_news, parent, false));
            default:
                return new ViewHolder((ViewCommentBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_comment, parent, false), context);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(root != null && root.type().equals("story") && position == 0) {
            return STORY;
        }
        else {
            return COMMENT;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clearItems()
    {
        notifyItemRangeRemoved(0, (root != null && root.type().equals("story")) ? data.size() : data.size() + 1);
        data.clear();
    }

    public void populateComments(Item root, Boolean forceUpdate)
    {
        this.root = root;
        if(root.kids() != null) {
            for(Integer id : root.kids()) {
                data.put(id, forceUpdate);
            }
        }
        notifyItemInserted(root.type().equals("story") ? data.size() : data.size() + 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements CommentsContract.View
    {
        private ViewCommentBinding dataBinding;
        private CommentsAdapter adapter;
        private Boolean forceUpdate;

        @Inject CommentsPresenter presenter;

        public ViewHolder(ViewCommentBinding binding, Context context) {
            super(binding.getRoot());
            this.dataBinding = binding;
            this.adapter = new CommentsAdapter(context);

            DaggerCommentsComponent.builder()
                    .itemRepositoryComponent(((HackerNewsApplication) context).getItemRepositoryComponent())
                    .commentsPresenterModule(new CommentsPresenterModule(this))
                    .build()
                    .inject(this);
        }

        @Override
        public void populateComments(final Item comment, final Boolean forceUpdate) {
            this.forceUpdate = forceUpdate;
            dataBinding.setStory(comment);
            dataBinding.recyclerView.setAdapter(adapter);
            dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(dataBinding.getRoot().getContext()));
            dataBinding.moreCommentsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(adapter.getItemCount() > 0) {
                        adapter.clearItems();
                        dataBinding.recyclerView.setVisibility(View.GONE);
                        ViewCompat.animate(dataBinding.expandMoreIcon)
                                .rotation(0.0f);
                    }
                    else
                    {
                        adapter.populateComments(comment, ViewHolder.this.forceUpdate);
                        dataBinding.recyclerView.setVisibility(View.VISIBLE);
                        ViewCompat.animate(dataBinding.expandMoreIcon)
                                .rotation(180.0f);
                        ViewHolder.this.forceUpdate = false; // Consume the forceUpdate
                    }
                }
            });
        }

        @Override
        public void showNetworkError() {
        }
    }
}
