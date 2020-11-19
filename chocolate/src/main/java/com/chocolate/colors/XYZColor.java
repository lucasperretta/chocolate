package com.chocolate.colors;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings({"unused", "WeakerAccess", "RedundantSuppression"})
public class XYZColor implements Color.Model {

    // Variables.....
    public double x;
    public double y;
    public double z;

    // Constructors.....
    public XYZColor() {}

    public XYZColor(@ColorInt int color) {
        double[] xyz = new double[3];
        ColorUtils.colorToXYZ(color, xyz);

        this.x = xyz[0];
        this.y = xyz[1];
        this.z = xyz[2];
    }

    public XYZColor(@NotNull String hex) {
        this(android.graphics.Color.parseColor(hex));
    }

    public XYZColor(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Methods.....
    @Override public int toInt() {
        return ColorUtils.XYZToColor(x, y, z);
    }

    @Override public String toHex() {
        return new RGBColor(toInt()).toHex();
    }

    @Override public String toHex(boolean ignoreAlpha) {
        return new RGBColor(toInt()).toHex(ignoreAlpha);
    }

}
