package com.schinizer.hackernews.features.newsfeed;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.schinizer.hackernews.BR;
import com.schinizer.hackernews.R;
import com.schinizer.hackernews.data.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DPSUser on 10/25/2016.
 */

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder>
{
    private List<Item> data = new ArrayList<>();

    public NewsFeedAdapter()
    {
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.dataBinding().setVariable(BR.story, data.get(position));
        holder.dataBinding().executePendingBindings();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.view_news, parent, false));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clearItems()
    {
        data.clear();
        notifyItemRangeRemoved(0, data.size());
    }

    public void addItems(List<Item> items)
    {
        data.addAll(items);
        notifyItemRangeInserted(data.size(), items.size());
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private ViewDataBinding dataBinding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            dataBinding = binding;
        }

        public ViewDataBinding dataBinding() { return dataBinding; }
    }
}
