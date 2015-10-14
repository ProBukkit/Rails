/*
 * This file is a part of the multiplayer platform Powered Rails, licensed under the MIT License (MIT).
 *
 * Copyright (c) Powered Rails
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.poweredrails.rails.task;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public final class Tasks {

    private ScheduledExecutorService scheduledExecutorService;
    private List<ScheduledFuture<?>> tasks;

    private Tasks() {
        scheduledExecutorService = Executors.newScheduledThreadPool(10);
        tasks = new ArrayList<>();
    }

    private static Tasks instance;

    public static Tasks getInstance() {
        if (instance == null) {
            instance = new Tasks();
        }
        return instance;
    }

    /**
     * Assigns a passed in {@link Runnable} instance to a {@link ScheduledFuture} task.
     *
     * @param runnable {@link Runnable} that is being assigned to a {@link ScheduledFuture} task.
     * @return {@link ScheduledFuture} instance.
     */
    public ScheduledFuture<?> runTask(Runnable runnable) {
        ScheduledFuture<?> future = scheduledExecutorService.schedule(runnable, 0, TimeUnit.SECONDS);
        tasks.add(future);
        return future;
    }

    /**
     * Assigns a passed in {@link Runnable} instance to a {@link ScheduledFuture} task.
     * Delay is calculated in seconds.
     *
     * @param runnable {@link Runnable} that is being assigned to a {@link ScheduledFuture} task.
     * @param delay The delay before the final task execution.
     * @return {@link ScheduledFuture} instance.
     */
    public ScheduledFuture<?> runDelayedTask(Runnable runnable, long delay) {
        ScheduledFuture<?> future = scheduledExecutorService.schedule(runnable, delay, TimeUnit.SECONDS);
        tasks.add(future);
        return future;
    }

    /**
     * Assigns a passed in {@link Runnable} instance to a {@link ScheduledFuture} task.
     *
     * @param runnable {@link Runnable} that is being assigned to a {@link ScheduledFuture} task.
     * @param repeat The delay between one task execution and the following execution.
     * @param delay The delay before the task start.
     * @return {@link ScheduledFuture} instance.
     */
    public ScheduledFuture<?> runRepeatingTask(Runnable runnable, long repeat, long delay) {
        ScheduledFuture<?> future = scheduledExecutorService.scheduleWithFixedDelay(runnable, repeat, delay, TimeUnit.SECONDS);
        tasks.add(future);
        return future;
    }

    /**
     * Terminates all {@link ScheduledFuture} tasks.
     * <p>If a task is in progress, it will not be interrupted/canceled.
     * The system waits 2 seconds to cancel every scheduled task.</p>
     */
    public void terminateAllTasks() {
        tasks.forEach(task -> task.cancel(false));
        tasks.clear();
        try {
            scheduledExecutorService.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return String.format("%s scheduledTasks=%s", getClass(), String.valueOf(tasks.size()));
    }
}
