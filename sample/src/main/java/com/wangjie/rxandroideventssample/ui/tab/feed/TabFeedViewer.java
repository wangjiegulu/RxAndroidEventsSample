package com.wangjie.rxandroideventssample.ui.tab.feed;

import com.wangjie.androidbucket.mvp.ABActivityViewer;
import com.wangjie.rxandroideventssample.provider.model.Feed;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public interface TabFeedViewer extends ABActivityViewer{
    void loadFeeds(int size);
    void onLoadReeds(List<Feed> feedList);

    void deleteFeed();
}
