package com.schinizer.hackernews.features.comments;

import com.schinizer.hackernews.BasePresenter;
import com.schinizer.hackernews.BaseView;
import com.schinizer.hackernews.data.Item;

/**
 * Created by DPSUser on 10/28/2016.
 */

public class CommentsContract {

    interface View extends BaseView
    {
        void populateComments(Item comment);
        void showNetworkError();
    }

    interface Presenter extends BasePresenter
    {
        void loadComment(Integer id);
    }
}
