package com.wangjie.rxandroideventssample.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.androidinject.annotation.annotations.base.AIView;
import com.wangjie.rxandroideventssample.R;
import com.wangjie.rxandroideventssample.annotation.accept.Accept;
import com.wangjie.rxandroideventssample.base.BaseActivity;
import com.wangjie.rxandroideventssample.events.ActionEvent;
import com.wangjie.rxandroideventssample.events.AddFeedsEvent;
import com.wangjie.rxandroideventssample.events.DeleteFeedsEvent;
import com.wangjie.rxandroideventssample.events.FeedItemClickEvent;
import com.wangjie.rxandroideventssample.provider.model.Feed;
import com.wangjie.rxandroideventssample.rxbus.RxBus;
import com.wangjie.rxandroideventssample.ui.main.adapter.TabAdapter;
import com.wangjie.rxandroideventssample.ui.tab.TabContainer;
import com.wangjie.rxandroideventssample.ui.tab.chat.TabChatContainer;
import com.wangjie.rxandroideventssample.ui.tab.feed.TabFeedContainer;
import com.wangjie.rxandroideventssample.ui.tab.setting.TabSettingContainer;

import java.util.List;
import java.util.Random;

@AILayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getName();
    @AIView(R.id.activity_main_tb)
    private Toolbar toolbar;
    @AIView(R.id.activity_main_tl)
    private TabLayout tabLayout;
    @AIView(R.id.activity_main_vp)
    private ViewPager viewPager;

    private TabAdapter tabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();

        initViews();

    }

    private void initToolbar() {
        toolbar.setTitle("Hello Test");
        setSupportActionBar(toolbar);
    }

    private void initViews() {
        tabLayout.addTab(tabLayout.newTab().setText("feed"));
        tabLayout.addTab(tabLayout.newTab().setText("chat"));
        tabLayout.addTab(tabLayout.newTab().setText("setting"));
        tabAdapter = new TabAdapter();
        List<TabContainer> list = tabAdapter.getList();
        Context context = getContext();
        list.add(new TabFeedContainer(context));
        list.add(new TabChatContainer(context));
        list.add(new TabSettingContainer(context));

        viewPager.setAdapter(tabAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int state = ViewPager.SCROLL_STATE_DRAGGING;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabLayout.setScrollPosition(position, positionOffset, ViewPager.SCROLL_STATE_DRAGGING == state);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                this.state = state;
            }
        });
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private Random random = new Random();
    private static final int ONE_HOUR = 1000 * 60 * 60;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_main_add_feed:
                AddFeedsEvent addFeedsEvent = new AddFeedsEvent();
                Feed feed = new Feed();
                long currMillis = System.currentTimeMillis();
                feed.setTitle("rxTitle_" + currMillis);
                feed.setContent("rxContent_" + currMillis);
                feed.setCreated(System.currentTimeMillis() - (random.nextInt(ONE_HOUR + 10) + ONE_HOUR));
                addFeedsEvent.getFeeds().add(feed);

                RxBus.get().send(addFeedsEvent);
//                RxBus.getInstance().send(String.class.getName(), "hello aaa");
                break;
            case R.id.menu_main_delete_feed:
                DeleteFeedsEvent deleteFeedsEvent = new DeleteFeedsEvent();
                deleteFeedsEvent.setDeleteIndex(0);
                RxBus.get().send(deleteFeedsEvent);
//                RxBus.getInstance().send(String.class.getName(), "hello bbb");
                break;
            case R.id.menu_main_action_event_refresh:
                RxBus.get().send(ActionEvent.REFRESH, ActionEvent.REFRESH);
                break;
            case R.id.menu_main_action_event_close:
                RxBus.get().send(ActionEvent.CLOSE, ActionEvent.CLOSE);
                break;
            case R.id.menu_main_action_event_edit:
                RxBus.get().send(ActionEvent.EDIT, ActionEvent.EDIT);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Accept
    public void onPostAccept(Object tag, FeedItemClickEvent event) {
        Logger.d(TAG, "onPostAccept event: " + event);
        Feed feed = event.getFeed();
        showToastMessage("main_" + feed.getTitle() + "_" + event.getPosition());
    }
}
