package com.wangjie.rxandroideventssample.ui.tab.feed;

import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.mvp.ABNoneInteractorImpl;
import com.wangjie.rxandroideventssample.base.BasePresenter;
import com.wangjie.rxandroideventssample.provider.model.Feed;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public class TabFeedPresenter extends BasePresenter<TabFeedViewer, ABNoneInteractorImpl> {
    private static final String TAG = TabFeedPresenter.class.getSimpleName();
    private static int feedCount = 0;
    private Random random = new Random();
    private static final int ONE_HOUR = 1000 * 60 * 60;

    void loadFeeds(int size) {
        goSubscription(
                Observable.from(testLoadFeedsFromNet(size))
                        .subscribeOn(Schedulers.newThread())
                        .map(feed -> {
                            feed.setTitle(feed.getTitle() + "_ob");
                            return feed;
                        })
                        .toSortedList((feed, feed2) -> (int) (feed.getCreated() - feed2.getCreated()))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(viewer::onLoadReeds, throwable -> Logger.w(TAG, "error: " + throwable.getMessage()))
        );
    }

    /**
     * 模拟从网络获取数据
     *
     * @param size
     * @return
     */
    private List<Feed> testLoadFeedsFromNet(int size) {
        size = size < 1 ? 1 : size;
        List<Feed> feeds = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Feed feed = new Feed();
            feed.setTitle("title_" + feedCount);
            feed.setContent("content_" + feedCount);
            feed.setCreated(System.currentTimeMillis() - (random.nextInt(ONE_HOUR + 10) + ONE_HOUR));
            feeds.add(feed);
            feedCount++;
        }
        return feeds;
    }

}
