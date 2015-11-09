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
package org.poweredrails.rails.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ScheduledQueueExecutor {

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
    private List<ScheduledFuture<?>> tasks = new ArrayList<>();

    public ScheduledQueueExecutor() {
        this.enqueueAsRepeating(() -> this.tasks.forEach(t -> {
            if (t.isCancelled()) {
                this.tasks.remove(t);
            }
        }), 0, 1, TimeUnit.MILLISECONDS);
    }

    /**
     * Queue a runnable to be executed by the executor service.
     * @param rnbl the runnable to execute
     */
    public void enqueue(Runnable rnbl) {
        this.executor.execute(rnbl);
    }

    /**
     * Queue a task to be executed after the given delay by the executor service.
     * @param rnbl the runnable to execute
     * @param delay the delay for the executor service to wait
     * @param unit the time unit in which the delay is specified in
     * @return the future object for this task
     */
    public ScheduledFuture<?> enqueueWithDelay(Runnable rnbl, long delay, TimeUnit unit) {
        return this.executor.schedule(rnbl, delay, unit);
    }

    /**
     * Queue a task to begin executing repeatedly, with the given period, after the delay by the executor service.
     * @param rnbl the runnable to execute
     * @param delay the delay for the executor service is to wait before starting the repeating task
     * @param period the interval between each execution of the task
     * @param unit the time unit in which both the delay and the period are specified in
     * @return the future object for this task
     */
    public ScheduledFuture<?> enqueueAsRepeating(Runnable rnbl, long delay, long period, TimeUnit unit) {
        ScheduledFuture<?> future = this.executor.scheduleAtFixedRate(rnbl, delay, period, unit);
        this.tasks.add(future);
        return future;
    }

    /**
     * Queue a callable to be executed after the given delay by the executor service.
     * @param callable the callable to execute
     * @param delay the delay for the executor service to wait
     * @param unit the time unit in which the delay is specified in
     * @param <T> generic object
     * @return the future object for this task
     */
    public <T> ScheduledFuture<T> enqueueCallable(Callable<T> callable, long delay, TimeUnit unit) {
        ScheduledFuture<T> future = this.executor.schedule(callable, delay, unit);
        this.tasks.add(future);
        return future;
    }

    /**
     * Interrupts all currently executing tasks.
     */
    public void shutdownNow() {
        this.executor.shutdownNow();
    }

    /**
     * Waits for all tasks to terminate within a given timeout, and interrupts all tasks that are still executing after
     * this.
     * @param timeout the timeout in which to wait
     * @param unit the unit that the timeout is specified in
     */
    public void shutdownWithTimeout(int timeout, TimeUnit unit) {
        this.tasks.forEach(t -> t.cancel(false));

        try {
            this.executor.awaitTermination(timeout, unit);
            this.executor.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
