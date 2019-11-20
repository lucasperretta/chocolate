package com.chocolate.utilities;

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

    // Classes.....
    public static class RGBColor implements ColorModel {

        // Variables.....
        public int red;
        public int green;
        public int blue;
        public int alpha;

        // Constructors.....
        public RGBColor() {}

        public RGBColor(@ColorInt int color) {
            this.red = android.graphics.Color.red(color);
            this.green = android.graphics.Color.green(color);
            this.blue = android.graphics.Color.blue(color);
            this.alpha = android.graphics.Color.alpha(color);
        }

        public RGBColor(@NotNull String hex) {
            this(android.graphics.Color.parseColor(hex));
        }

        public RGBColor(int red, int green, int blue) {
            this(red, green, blue, 255);
        }

        public RGBColor(int red, int green, int blue, int alpha) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = 255;
        }

        // Methods.....
        @ColorInt @Override public int toInt() {
            return (alpha & 0xff) << 24 | (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
        }

        @Override public String toHex() {
            return toHex(true);
        }

        @Override public String toHex(boolean ignoreAlpha) {
            return '#' + (ignoreAlpha ? String.format("%08X", toInt()).substring(2) : String.format("%08X", toInt()));
        }

    }

    public static class HSVColor implements ColorModel {

        // Variables.....
        public float hue;
        public float saturation;
        public float value;

        // Constructors.....
        public HSVColor() {}

        public HSVColor(@ColorInt int color) {
            float[] hsv = new float[3];
            RGBColor rgbaComponents = new RGBColor(color);
            android.graphics.Color.RGBToHSV(rgbaComponents.red, rgbaComponents.green, rgbaComponents.blue, hsv);

            this.hue = hsv[0];
            this.saturation = hsv[1];
            this.value = hsv[2];
        }

        public HSVColor(@NotNull String hex) {
            this(android.graphics.Color.parseColor(hex));
        }

        public HSVColor(float hue, float saturation, float value) {
            this.hue = hue;
            this.saturation = saturation;
            this.value = value;
        }

        // Methods.....
        @Override public int toInt() {
            return android.graphics.Color.HSVToColor(new float[]{hue, saturation, value});
        }

        @Override public String toHex() {
            return new RGBColor(toInt()).toHex();
        }

        @Override public String toHex(boolean ignoreAlpha) {
            return new RGBColor(toInt()).toHex(ignoreAlpha);
        }

    }

    // Interfaces.....
    public interface ColorModel {
        @ColorInt int toInt();
        String toHex();
        String toHex(boolean ignoreAlpha);
    }

}
