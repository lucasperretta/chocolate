package com.chocolate.colors;

import androidx.annotation.ColorInt;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "WeakerAccess"})
public class RGBColor implements Color.Model {

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
