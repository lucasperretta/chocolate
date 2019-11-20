package com.chocolate.colors;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.RequiresApi;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Color {

    // Methods.....
    @ColorInt @RequiresApi(api = Build.VERSION_CODES.M) public static int get(@NotNull Context context, @ColorRes int id, Resources.Theme theme) {
        return context.getResources().getColor(id, theme);
    }

    @SuppressWarnings("deprecation") @ColorInt public static int get(@NotNull Context context, @ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(id, null);
        } else {
            return context.getResources().getColor(id);
        }
    }

    // Interfaces.....
    public interface Model {
        @ColorInt int toInt();
        String toHex();
        String toHex(boolean ignoreAlpha);
    }

}
