package com.schinizer.hackernews.features.comments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schinizer.hackernews.HackerNewsApplication;
import com.schinizer.hackernews.R;
import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.databinding.ViewCommentBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by DPSUser on 10/31/2016.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private List<Integer> data = new ArrayList<>();
    Context context;

    public CommentsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.dataBinding.setStory(Item.createEmpty(data.get(position)));
        holder.presenter.loadComment(data.get(position));
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((ViewCommentBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_comment, parent, false), context);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clearItems()
    {
        notifyItemRangeRemoved(0, data.size());
        data.clear();
    }

    public void populateComments(Item root)
    {
        if(root.kids() == null) {
            return;
        }
        data.addAll(root.kids());
        notifyItemInserted(data.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements CommentsContract.View
    {
        private ViewCommentBinding dataBinding;
        private CommentsAdapter adapter;
        private Context context;

        @Inject CommentsPresenter presenter;

        public ViewHolder(ViewCommentBinding binding, Context context) {
            super(binding.getRoot());
            this.dataBinding = binding;
            this.context = context;
            this.adapter = new CommentsAdapter(context);

            DaggerCommentsComponent.builder()
                    .itemRepositoryComponent(((HackerNewsApplication) context).getItemRepositoryComponent())
                    .commentsPresenterModule(new CommentsPresenterModule(this))
                    .build()
                    .inject(this);
        }

        @Override
        public void populateComments(final Item comment) {
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
                        adapter.populateComments(comment);
                        dataBinding.recyclerView.setVisibility(View.VISIBLE);
                        ViewCompat.animate(dataBinding.expandMoreIcon)
                                .rotation(180.0f);
                    }
                }
            });
        }

        @Override
        public void showNetworkError() {
        }
    }
}
