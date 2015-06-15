package com.wangjie.rxandroideventssample.ui.tab.setting;

import android.content.Context;
import com.wangjie.androidinject.annotation.annotations.base.AILayout;
import com.wangjie.rxandroideventssample.R;
import com.wangjie.rxandroideventssample.ui.tab.TabContainer;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/10/15.
 */
@AILayout(R.layout.tab_setting)
public class TabSettingContainer extends TabContainer {
    public TabSettingContainer(Context context) {
        super(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
