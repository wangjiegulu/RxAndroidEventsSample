package com.wangjie.rxandroideventssample.rxbus;

import android.os.Handler;
import com.wangjie.androidbucket.log.Logger;
import com.wangjie.androidbucket.utils.ABTextUtil;
import com.wangjie.rxandroideventssample.annotation.accept.Accept;
import com.wangjie.rxandroideventssample.annotation.accept.AcceptScheduler;
import com.wangjie.rxandroideventssample.annotation.accept.AcceptType;
import com.wangjie.rxandroideventssample.annotation.accept.DefaultAcceptConfiguration;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/11/15.
 */
public class RxBusAnnotationManager {
    private static final String TAG = RxBusAnnotationManager.class.getName();
    private Object object;

    public RxBusAnnotationManager(Object object) {
        this.object = object;
    }

    private List<ObservableWrapper> registeredObservable;

    public <T> void parserObservableEventAnnotations(Method method) throws Exception {
        if (null == method || !method.isAnnotationPresent(Accept.class)) {
            return;
        }
        Class[] params = method.getParameterTypes();
        // 参数必须是两个，第1个必须是Object类型的tag
        if (null == params || 2 != params.length || !Object.class.isAssignableFrom(params[0])) {
            throw new Exception("the method[" + method.getName() + "] must defined xxx(Object tag, T object)");
        }

        Accept accept = method.getAnnotation(Accept.class);
        AcceptType[] acceptTypes = accept.value();

        // 默认clazz参数类型
        Class<T> targetClazz = params[1];
        // 默认clazz参数类型的全类名
        String targetTag = targetClazz.getName();
        Class<T> specClazz;
        String specTag;
        int acceptTypeLength = null == acceptTypes ? 0 : acceptTypes.length;
        switch (acceptTypeLength) {
            case 0: // 如果acceptType是空，则说明具体的类型是params[1]，所以params[1]不能为Object类型
                if (Object.class.equals(targetClazz)) {
                    throw new Exception("the method[" + method.getName() + "] must defined xxx(Object tag, T object)");
                }
                registerObservable(method, targetTag, targetClazz, accept.acceptScheduler());
                break;
            case 1: // 如果只有一个，如果acceptType中tag不为空，则使用
                // 默认clazz参数类型，acceptType中指定clazz优先
                specClazz = acceptTypes[0].clazz();
                if (!Object.class.equals(specClazz)) {
                    targetClazz = specClazz;
                }
                if (Object.class.equals(targetClazz)) {
                    throw new Exception("the method[" + method.getName() + "] must defined xxx(Object tag, T object) OR clazz of @AcceptType");
                }
                targetTag = targetClazz.getName();
                // 默认tag参数类型的全类名，acceptType中指定tag优先
                specTag = acceptTypes[0].tag();
                if (!ABTextUtil.isEmpty(specTag)) {
                    targetTag = specTag;
                }
                registerObservable(method, targetTag, targetClazz, accept.acceptScheduler());
                break;
            default: // 如果有多个，则params[1]必须是Object
                if (!Object.class.equals(targetClazz)) {
                    throw new Exception("the method[" + method.getName() + "] must defined xxx(Object tag, Object object)");
                }
                for (AcceptType acceptType : acceptTypes) {
                    specClazz = acceptType.clazz();
                    specTag = acceptType.tag();
                    // 默认tag参数类型的全名，acceptType中指定tag优先
                    registerObservable(method, ABTextUtil.isEmpty(specTag) ? specClazz.getName() : specTag, specClazz, accept.acceptScheduler());
                }
                break;
        }


    }


    private void ensureRegisteredObservable() {
        if (null == registeredObservable) {
            registeredObservable = new ArrayList<>();
        }
    }

    private <T> void registerObservable(Method method, String tag, Class<T> clazz, AcceptScheduler acceptScheduler) {
        if (null == tag || null == clazz) {
            return;
        }
        ensureRegisteredObservable();
        Observable<T> observable = RxBus.get().register(tag, clazz);
        registeredObservable.add(new ObservableWrapper(tag, observable));

        Observable<T> schedulerObservable;
        switch (acceptScheduler) {
            case NEW_THREAD:
                schedulerObservable = observable.observeOn(Schedulers.newThread());
                break;
            case IO:
                schedulerObservable = observable.observeOn(Schedulers.io());
                break;
            case IMMEDIATE:
                schedulerObservable = observable.observeOn(Schedulers.immediate());
                break;
            case COMPUTATION:
                schedulerObservable = observable.observeOn(Schedulers.computation());
                break;
            case TRAMPOLINE:
                schedulerObservable = observable.observeOn(Schedulers.trampoline());
                break;
            case EXECUTOR:
                Executor executor = DefaultAcceptConfiguration.getInstance().applyAcceptExecutor();
                if (null == executor) {
                    throw new RuntimeException("DefaultAcceptConfiguration applyAcceptExecutor() return null, please register OnDefaultAcceptConfiguration in Application");
                }
                schedulerObservable = observable.observeOn(Schedulers.from(executor));
                break;
            case HANDLER:
                Handler handler = DefaultAcceptConfiguration.getInstance().applyAcceptHandler();
                if (null == handler) {
                    throw new RuntimeException("DefaultAcceptConfiguration applyAcceptHandler() return null, please register OnDefaultAcceptConfiguration in Application");
                }
                schedulerObservable = observable.observeOn(AndroidSchedulers.handlerThread(handler));
                break;
            default: // MAIN_THREAD default
                schedulerObservable = observable.observeOn(AndroidSchedulers.mainThread());
                break;
        }

        schedulerObservable.subscribe(o -> {
            try {
                method.invoke(object, tag, o);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        });

    }

    public void clear() {
        if (!ABTextUtil.isEmpty(registeredObservable)) {
            for (ObservableWrapper observableWrapper : registeredObservable) {
                RxBus.get().unregister(observableWrapper.getTag(), observableWrapper.getObservable());
            }
        }
    }

}
