package com.wangjie.rxandroideventssample.ui.main.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.wangjie.rxandroideventssample.ui.tab.TabContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public class TabAdapter extends PagerAdapter {
    private List<TabContainer> list = new ArrayList<>();

    public List<TabContainer> getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object == view;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TabContainer tabContainer = list.get(position);
        if (null == tabContainer.getParent()) {
            container.addView(tabContainer);
        }
        return tabContainer;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
