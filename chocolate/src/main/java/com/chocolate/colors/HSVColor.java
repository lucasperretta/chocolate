package com.chocolate.colors;

import androidx.annotation.ColorInt;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "WeakerAccess"})
public class HSVColor implements Color.Model {

    // Variables.....
    public float hue;
    public float saturation;
    public float value;

    // Constructors.....
    public HSVColor() {}

    public HSVColor(@ColorInt int color) {
        float[] hsv = new float[3];
        android.graphics.Color.colorToHSV(color, hsv);

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
