package com.wangjie.rxandroideventssample.events;

import com.wangjie.rxandroideventssample.provider.model.Feed;

import java.io.Serializable;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/15/15.
 */
public class FeedItemClickEvent implements Serializable {
    private String name = "ItemClickEvent";
    private int position;
    private Feed feed;

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public FeedItemClickEvent setPosition(int position) {
        this.position = position;
        return this;
    }

    public Feed getFeed() {
        return feed;
    }

    public FeedItemClickEvent setFeed(Feed feed) {
        this.feed = feed;
        return this;
    }

    @Override
    public String toString() {
        return "FeedItemClickEvent{" +
                "name='" + name + '\'' +
                ", position=" + position +
                ", feed=" + feed +
                '}';
    }
}
