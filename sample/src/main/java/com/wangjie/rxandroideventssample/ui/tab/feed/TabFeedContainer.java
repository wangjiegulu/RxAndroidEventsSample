package com.wangjie.rxandroideventssample.ui.tab.feed;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.support.recyclerview.layoutmanager.ABaseLinearLayoutManager;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.androidinject.annotation.annotations.base.AIClick;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.androidinject.annotation.annotations.base.AIView;
import com.wangjie.androidinject.annotation.annotations.mvp.AIPresenter;
import com.wangjie.rxandroideventssample.R;
import com.wangjie.rxandroideventssample.annotation.accept.Accept;
import com.wangjie.rxandroideventssample.annotation.accept.AcceptScheduler;
import com.wangjie.rxandroideventssample.annotation.accept.AcceptType;
import com.wangjie.rxandroideventssample.events.ActionEvent;
import com.wangjie.rxandroideventssample.events.AddFeedsEvent;
import com.wangjie.rxandroideventssample.events.DeleteFeedsEvent;
import com.wangjie.rxandroideventssample.events.FeedItemClickEvent;
import com.wangjie.rxandroideventssample.provider.model.Feed;
import com.wangjie.rxandroideventssample.rxbus.RxBus;
import com.wangjie.rxandroideventssample.ui.main.adapter.FeedAdapter;
import com.wangjie.rxandroideventssample.ui.tab.TabContainer;

import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
@AILayout(R.layout.tab_feed)
public class TabFeedContainer extends TabContainer implements TabFeedViewer, FeedAdapter.OnFeedAdapterListener {
    private static final String TAG = TabFeedContainer.class.getSimpleName();

    public TabFeedContainer(Context context) {
        super(context);
    }

    @AIView(R.id.tab_feed_load_feeds_rv)
    private RecyclerView feedRv;
    private FeedAdapter adapter;

    private ABaseLinearLayoutManager layoutManager;

    @AIPresenter
    private TabFeedPresenter presenter;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        Context context = getContext();
        layoutManager = new ABaseLinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        feedRv.setLayoutManager(layoutManager);
        adapter = new FeedAdapter(context);
        adapter.setOnFeedAdapterListener(this);
        feedRv.setAdapter(adapter);
        loadFeeds(10);
    }

    @Override
    @AIClick({R.id.tab_feed_load_feeds_btn})
    public void onClickCallbackSample(View view) {
        switch (view.getId()) {
            case R.id.tab_feed_load_feeds_btn:
                loadFeeds(1);
                break;
            default:
                break;
        }
    }

    @Override
    public void loadFeeds(int size) {
        presenter.loadFeeds(size);
    }

    @Override
    public void onLoadReeds(List<Feed> feedList) {
        if (!ABTextUtil.isEmpty(feedList)) {
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
            int insertPos = firstVisibleItemPosition < 0 ? 0 : firstVisibleItemPosition + 1;
            adapter.getList().addAll(insertPos, feedList);
            adapter.notifyItemInserted(insertPos);
        }

    }

    @Override
    public void deleteFeed() {
        List<Feed> list = adapter.getList();
        if (!ABTextUtil.isEmpty(list)) {
            list.remove(0);
            adapter.notifyItemRemoved(0);
        }
    }

    @Override
    public void onFeedItemClick(int position, Feed feed) {
//        showToastMessage(feed.getTitle());
        RxBus.get().post(new FeedItemClickEvent().setPosition(position).setFeed(feed));
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /*
    @Accept(@AcceptType(clazz = AddFeedsEvent.class))
    public void onPostAcceptX(Object tag, Object event) {
        Logger.d(TAG, "[AddFeedsEvent]onPostAccept tag: " + tag + ", object: " + event);
        onLoadReeds(((AddFeedsEvent) event).getFeeds());
    }
    */

    @Accept
    public void onPostAccept(Object tag, AddFeedsEvent event) {
        Logger.d(TAG, "[AddFeedsEvent]onPostAccept tag: " + tag + ", object: " + event);
        onLoadReeds(event.getFeeds());
    }

    @Accept(acceptScheduler = AcceptScheduler.MAIN_THREAD)
    public void onPostAccept(Object tag, DeleteFeedsEvent event) {
        deleteFeed();
    }

    @Accept(
            {
                    @AcceptType(tag = ActionEvent.CLOSE, clazz = String.class),
                    @AcceptType(tag = ActionEvent.BACK, clazz = String.class),
                    @AcceptType(tag = ActionEvent.EDIT, clazz = String.class),
                    @AcceptType(tag = ActionEvent.REFRESH, clazz = String.class)
            }
    )
    public void onPostAccept(Object tag, Object actionEvent) {
        Logger.d(TAG, "[ActionEvent]onPostAccept action event name: " + actionEvent);
        showToastMessage(actionEvent.toString());
    }


}
