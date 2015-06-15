package com.wangjie.rxandroideventssample.annotation.accept;

import android.os.Handler;
import rx.Scheduler;

import java.util.concurrent.Executor;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 6/15/15.
 */
public enum AcceptScheduler {
    /**
     * {@link Scheduler} which will execute actions on the Android UI thread.
     */
    MAIN_THREAD,

    /**
     * Creates and returns a {@link Scheduler} that creates a new {@link Thread} for each unit of work.
     * <p>
     * Unhandled errors will be delivered to the scheduler Thread's {@link java.lang.Thread.UncaughtExceptionHandler}.
     */
    NEW_THREAD,

    /**
     * Creates and returns a {@link Scheduler} intended for IO-bound work.
     * <p>
     * The implementation is backed by an {@link Executor} thread-pool that will grow as needed.
     * <p>
     * This can be used for asynchronously performing blocking IO.
     * <p>
     * Do not perform computational work on this scheduler. Use computation() instead.
     * <p>
     * Unhandled errors will be delivered to the scheduler Thread's {@link java.lang.Thread.UncaughtExceptionHandler}.
     */
    IO,

    /**
     * Creates and returns a {@link Scheduler} intended for computational work.
     * <p>
     * This can be used for event-loops, processing callbacks and other computational work.
     * <p>
     * Do not perform IO-bound work on this scheduler. Use io() instead.
     * <p>
     * Unhandled errors will be delivered to the scheduler Thread's {@link java.lang.Thread.UncaughtExceptionHandler}.
     */
    COMPUTATION,

    /**
     * Creates and returns a {@link Scheduler} that queues work on the current thread to be executed after the
     * current work completes.
     */
    TRAMPOLINE,

    /**
     * Creates and returns a {@link Scheduler} that executes work immediately on the current thread.
     */
    IMMEDIATE,

    /**
     * Converts an {@link Executor} into a new Scheduler instance.
     */
    EXECUTOR,

    /**
     * {@link Scheduler} which uses the provided {@link Handler} to execute actions.
     */
    HANDLER

}
