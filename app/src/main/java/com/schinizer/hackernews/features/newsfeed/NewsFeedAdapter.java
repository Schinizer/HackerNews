package com.schinizer.hackernews.features.newsfeed;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.schinizer.hackernews.R;
import com.schinizer.hackernews.data.Item;
import com.schinizer.hackernews.databinding.ViewNewsBinding;
import com.schinizer.hackernews.features.comments.CommentsActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by DPSUser on 10/25/2016.
 */

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>
{
    private List<Item> data = new ArrayList<>();

    @Inject
    public NewsFeedAdapter() { }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = data.get(position);
        holder.dataBinding.setStory(item);
        holder.dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CommentsActivity.class);
                intent.putExtra("data", item);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder((ViewNewsBinding)DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_news, parent, false));
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

    public void addItems(List<Item> items)
    {
        notifyItemRangeInserted(data.size(), items.size());
        data.addAll(items);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewNewsBinding dataBinding;

        public ViewHolder(ViewNewsBinding binding) {
            super(binding.getRoot());
            dataBinding = binding;
        }
    }
}
