package com.wangjie.rxandroideventssample.events;

import java.io.Serializable;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/15/15.
 */
public class DeleteFeedsEvent implements Serializable {
    private String name = "deleteFeeds";
    private int deleteIndex;

    public String getName() {
        return name;
    }

    public int getDeleteIndex() {
        return deleteIndex;
    }

    public DeleteFeedsEvent setDeleteIndex(int deleteIndex) {
        this.deleteIndex = deleteIndex;
        return this;
    }
}
