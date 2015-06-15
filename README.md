# RxAndroidEventsSample
BusEvents implementation base RxJava/RxAndroid/AndroidInject.
## Dependencies：</br>
[AndroidBucket](https://github.com/wangjiegulu/AndroidBucket)：My base library</br>
[AndroidInject](https://github.com/wangjiegulu/androidInject)：My Inject library</br>
[RxJava](https://github.com/ReactiveX/RxJava)</br>
[RxAndroid](https://github.com/ReactiveX/RxAndroid)</br>
[retrolambda](https://github.com/evant/gradle-retrolambda)</br>

## How to use with Annotation(recommended)
`It's realy simple to use with annotation`
### 1. You must be config presents to support annotations(For example: BaseActivity which extends AIAppCompatActivity in the [AndroidInject](https://github.com/wangjiegulu/androidInject) library)
```java
private RxBusAnnotationManager rxBusAnnotationManager;
@Override
public void parserMethodAnnotations(Method method) throws Exception {
    if (method.isAnnotationPresent(Accept.class)) {
        if (null == rxBusAnnotationManager) {
            rxBusAnnotationManager = new RxBusAnnotationManager(this);
        }
        rxBusAnnotationManager.parserObservableEventAnnotations(method);
    }
}

@Override
protected void onDestroy() {
    super.onDestroy();
    if (null != rxBusAnnotationManager) {
        rxBusAnnotationManager.clear();
    }
}
```
### 2. Write a method in your present(eg. your activity), and use an @Accept annotation
```java
@Accept
public void onPostAccept(Object tag, AddFeedsEvent event) {
    // todo: Accept Message and process here (in main thread)
}
```
or
```java
@Accept(
            acceptScheduler = AcceptScheduler.NEW_THREAD,
            value = {
                    @AcceptType(tag = ActionEvent.CLOSE, clazz = String.class),
                    @AcceptType(tag = ActionEvent.BACK, clazz = String.class),
                    @AcceptType(tag = ActionEvent.EDIT, clazz = String.class),
                    @AcceptType(tag = ActionEvent.REFRESH, clazz = String.class)
            }
    )
    public void onPostAccept(Object tag, Object actionEvent) {
        Logger.d(TAG, "[ActionEvent]onPostAccept action event name: " + actionEvent);
        // todo: Accept Message and process here (in new thread)
    }
```

@Accept:
```
acceptScheduler: Perform this method on a specified scheduler;
value[]: AcceptType annotation array that specify the types this method can be accept. 
```
@AcceptType:
```
tag: The tag of the message.
clazz: The Class of the message.
```

### 3. Post a message:
```java
AddFeedsEvent addFeedsEvent = new AddFeedsEvent();
// Construct a AddFeedsEvent Object...
RxBus.get().post(addFeedsEvent);
```

### 4. Specify an Executor or Handler(Optional)
```java
public class MyApplication extends Application {
    private Executor acceptExecutor = Executors.newCachedThreadPool();
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
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
```

</br></br></br>

## How to use without Annotation(Not recommended)
### Register an Observer
```java
Observable<String> addOb = RxBus.get()
                .register("addFeedTag", String.class);
                
addOb.observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    // todo: Accept Message and process here
                });
```
### Post a message from any where
```java
RxBus.get().post("addFeedTag", "hello RxBus!");
```
### unregister the Observer
```java
RxBus.get().unregister("addFeedTag", addOb);
```

License
=======

    Copyright 2015 Wang Jie

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
