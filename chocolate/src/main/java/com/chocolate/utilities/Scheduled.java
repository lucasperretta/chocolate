package com.chocolate.utilities;

import androidx.annotation.NonNull;

@SuppressWarnings({"unused", "WeakerAccess"}) public class Scheduled {

    // Variables.....
    protected Delayed delayed;
    protected boolean cancelled = false;

    // Constructor.....
    public Scheduled(long interval, @NonNull Runnable runnable) {
        this(interval, 0, runnable);
    }

    public Scheduled(long interval, long delay, @NonNull Runnable runnable) {
        if (delay == 0) {
            start(interval, runnable);
        } else {
            Delayed.run(delay, () -> start(interval, runnable));
        }
    }

    // Methods.....
    protected void start(long interval, @NonNull Runnable runnable) {
        runnable.run();
        if (cancelled) return;
        delayed = Delayed.run(interval, () -> {
            if (cancelled) return;
            start(interval, runnable);
        });
    }

    public void cancel() {
        if (delayed != null) delayed.cancel();
        cancelled = true;
    }

    public static Scheduled run(long interval, @NonNull Runnable runnable) {
        return new Scheduled(interval, runnable);
    }

    public static Scheduled run(long interval, long delay, @NonNull Runnable runnable) {
        return new Scheduled(interval, delay, runnable);
    }

}
