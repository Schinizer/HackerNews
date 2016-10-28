package com.schinizer.hackernews.features.comments;

import com.schinizer.hackernews.BasePresenter;
import com.schinizer.hackernews.BaseView;
import com.schinizer.hackernews.data.Item;

import java.util.List;

/**
 * Created by DPSUser on 10/28/2016.
 */

public class CommentsContract {

    interface View extends BaseView
    {
        void populateComments(List<Item> comments);
    }

    interface Presenter extends BasePresenter
    {
        void loadStoryAndComments(Boolean forceRefresh);
    }
}
