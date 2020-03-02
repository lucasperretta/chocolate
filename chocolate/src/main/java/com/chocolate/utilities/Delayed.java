package com.chocolate.utilities;

import android.os.Handler;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "WeakerAccess"}) public class Delayed {

    // Variables.....
    @SuppressWarnings("WeakerAccess") protected final Handler handler = new Handler();

    // Constructors.....
    @SuppressWarnings("WeakerAccess") public Delayed(long delay, @NotNull Runnable runnable) {
        handler.postDelayed(runnable, delay);
    }

    // Methods.....
    public void cancel() {
        handler.removeCallbacksAndMessages(null);
    }

    @NotNull public static Delayed run(long delay, @NotNull Runnable runnable) {
        return new Delayed(delay, runnable);
    }

}
