package com.wangjie.rxandroideventssample.rxbus;

import android.util.Log;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import java.util.HashMap;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/11/15.
 */
public class RxBusBak {
    private static final String TAG = RxBusBak.class.getSimpleName();
    private static RxBusBak instance;
    private static final boolean DEBUG = true;

    public static synchronized RxBusBak getInstance() {
        if (null == instance) {
            instance = new RxBusBak();
        }
        return instance;
    }

    private RxBusBak() {
    }

    private HashMap<Object, Subject> subjectMapper = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <T> Observable<T> register(Object tag, Class<T> clazz) {
        Subject<T, T> subject = findSubject(tag);
        if (null == subject) {
            subject = PublishSubject.create();
            synchronized (this) {
                subjectMapper.put(tag, subject);
            }
        }
        if (DEBUG) Log.d(TAG, "[register]subjectMapper: " + subjectMapper);
        return subject;
    }

    public void unregister(Object tag) {
        synchronized (this) {
            subjectMapper.remove(tag);
        }
        if (DEBUG) Log.d(TAG, "[unregister]subjectMapper: " + subjectMapper);
    }

    @SuppressWarnings("unchecked")
    public void send(Object tag, Object content) {
        Subject subject = findSubject(tag);
        if (null != subject) {
            subject.onNext(content);
        }
        if (DEBUG) Log.d(TAG, "[send]subjectMapper: " + subjectMapper);
    }

    @SuppressWarnings("unchecked")
    public <T> Observable<T> toObservable(Object tag, Class<T> clazz) {
        return findSubject(tag);
    }

    private Subject findSubject(Object tag) {
        Subject subject;
        synchronized (this) {
            subject = subjectMapper.get(tag);
        }
        return subject;
    }

}
