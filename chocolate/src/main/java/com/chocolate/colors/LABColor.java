package com.chocolate.colors;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "WeakerAccess"})
public class LABColor implements Color.ColorModel {

    // Variables.....
    public double l;
    public double a;
    public double b;

    // Constructors.....
    public LABColor() {}

    public LABColor(@ColorInt int color) {
        double[] lab = new double[3];
        ColorUtils.colorToLAB(color, lab);

        this.l = lab[0];
        this.a = lab[1];
        this.b = lab[2];
    }

    public LABColor(@NotNull String hex) {
        this(android.graphics.Color.parseColor(hex));
    }

    public LABColor(float l, float a, float b) {
        this.l = l;
        this.a = a;
        this.b = b;
    }

    // Methods.....
    @Override public int toInt() {
        return ColorUtils.LABToColor(l, a, b);
    }

    @Override public String toHex() {
        return new RGBColor(toInt()).toHex();
    }

    @Override public String toHex(boolean ignoreAlpha) {
        return new RGBColor(toInt()).toHex(ignoreAlpha);
    }

}
