package com.chocolate.utilities;

import android.os.Handler;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused") public class DelayedExecution {

    // Variables.....
    @SuppressWarnings("WeakerAccess") protected final Handler handler = new Handler();

    // Constructors.....
    @SuppressWarnings("WeakerAccess") public DelayedExecution(long delay, @NotNull Runnable runnable) {
        handler.postDelayed(runnable, delay);
    }

    // Methods.....
    public void cancel() {
        handler.removeCallbacksAndMessages(null);
    }

}
