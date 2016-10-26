package com.schinizer.hackernews.features.newsfeed;

import com.schinizer.hackernews.BasePresenter;
import com.schinizer.hackernews.BaseView;
import com.schinizer.hackernews.data.Item;

import java.util.List;

/**
 * Created by DPSUser on 10/25/2016.
 */

public interface NewsFeedContract {

    interface View extends BaseView
    {
        void populateView(List<Item> stories);
        void clearView();
    }

    interface Presenter extends BasePresenter
    {
        void loadTop500Stories(Boolean forceUpdate);
        void pageStories(Integer offset);
    }
}
