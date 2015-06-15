package com.wangjie.rxandroideventssample.application;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import com.wangjie.rxandroideventssample.annotation.accept.DefaultAcceptConfiguration;
import com.wangjie.rxandroideventssample.rxbus.RxBus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/15/15.
 */
public class MyApplication extends Application {
    private Executor acceptExecutor = Executors.newCachedThreadPool();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        RxBus.DEBUG = true;

        DefaultAcceptConfiguration.getInstance().registerAcceptConfiguration(new DefaultAcceptConfiguration.OnDefaultAcceptConfiguration() {
            @Override
            public Executor applyAcceptExecutor() {
                return acceptExecutor;
            }

            @Override
            public Handler applyAcceptHandler() {
                return handler;
            }
        });
    }
}
