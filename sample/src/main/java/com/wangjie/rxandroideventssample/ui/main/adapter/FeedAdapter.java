package com.wangjie.rxandroideventssample.ui.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.wangjie.androidbucket.support.recyclerview.adapter.ABRecyclerViewAdapter;
import com.wangjie.androidbucket.support.recyclerview.adapter.ABRecyclerViewHolder;
import com.wangjie.androidbucket.utils.ABTimeUtil;
import com.wangjie.androidbucket.utils.ABViewUtil;
import com.wangjie.androidbucket.utils.imageprocess.ABShape;
import com.wangjie.rxandroideventssample.R;
import com.wangjie.rxandroideventssample.provider.model.Feed;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
public class FeedAdapter extends ABRecyclerViewAdapter {
    public interface OnFeedAdapterListener {
        void onFeedItemClick(int position, Feed feed);
    }

    private OnFeedAdapterListener onFeedAdapterListener;

    public void setOnFeedAdapterListener(OnFeedAdapterListener onFeedAdapterListener) {
        this.onFeedAdapterListener = onFeedAdapterListener;
    }

    private Context context;

    public FeedAdapter(Context context) {
        this.context = context;
    }

    private List<Feed> list = new ArrayList<>();

    public List<Feed> getList() {
        return list;
    }

    public void setList(List<Feed> list) {
        this.list = list;
    }

    @Override
    public ABRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.feed_rv_item, null);
        ABRecyclerViewHolder holder = new ABRecyclerViewHolder(view);
        View rootView = holder.obtainView(R.id.feed_rv_item_root_view);
        ABViewUtil.setBackgroundDrawable(rootView, ABShape.selectorClickColorCornerSimple(0xffffffff, 0xffefefef, 0));
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if(null == lp){
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        view.setLayoutParams(lp);

        rootView.setOnClickListener(view1 -> {
            if (null != onFeedAdapterListener) {
                int position = holder.getAdapterPosition();
                onFeedAdapterListener.onFeedItemClick(position, list.get(position));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ABRecyclerViewHolder holder, int position) {
        Feed feed = list.get(position);
        holder.obtainView(R.id.feed_rv_item_title_tv, TextView.class).setText(feed.getTitle());
        holder.obtainView(R.id.feed_rv_item_content_tv, TextView.class).setText(feed.getContent());
        holder.obtainView(R.id.feed_rv_item_date_tv, TextView.class).setText(ABTimeUtil.millisToLifeString2(feed.getCreated()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
