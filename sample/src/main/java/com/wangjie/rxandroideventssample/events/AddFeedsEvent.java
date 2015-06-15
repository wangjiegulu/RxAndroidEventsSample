package com.wangjie.rxandroideventssample.events;

import com.wangjie.rxandroideventssample.provider.model.Feed;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/15/15.
 */
public class AddFeedsEvent implements Serializable{
    private String name = "addFeeds";
    private List<Feed> feeds = new ArrayList<>();

    public List<Feed> getFeeds() {
        return feeds;
    }

    public String getName() {
        return name;
    }
}
