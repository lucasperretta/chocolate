package com.chocolate.colors;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "WeakerAccess", "RedundantSuppression"})
public class HSLColor implements Color.Model {

    // Variables.....
    public float hue;
    public float saturation;
    public float lightness;

    // Constructors.....
    public HSLColor() {}

    public HSLColor(@ColorInt int color) {
        float[] hsl = new float[3];
        ColorUtils.colorToHSL(color, hsl);

        this.hue = hsl[0];
        this.saturation = hsl[1];
        this.lightness = hsl[2];
    }

    public HSLColor(@NotNull String hex) {
        this(android.graphics.Color.parseColor(hex));
    }

    public HSLColor(float hue, float saturation, float lightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.lightness = lightness;
    }

    // Methods.....
    @Override public int toInt() {
        return ColorUtils.HSLToColor(new float[]{hue, saturation, lightness});
    }

    @Override public String toHex() {
        return new RGBColor(toInt()).toHex();
    }

    @Override public String toHex(boolean ignoreAlpha) {
        return new RGBColor(toInt()).toHex(ignoreAlpha);
    }

}
