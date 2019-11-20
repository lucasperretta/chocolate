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

    /*
    // Classes.....
    public static final class convert extends UtilityClass {

        @ColorInt public static int toInt(@NotNull String hex) {
            return android.graphics.Color.parseColor(hex);
        }

        @ColorInt public static int toInt(int red, int green, int blue) {
            return toInt(red, green, blue, 255);
        }

        @ColorInt public static int toInt(int red, int green, int blue, int alpha) {
            return (alpha & 0xff) << 24 | (red & 0xff) << 16 | (green & 0xff) << 8 | (blue & 0xff);
        }

        @ColorInt public static int toInt(float hue, float saturation, float value) {

        }

        public static String toHex(@ColorInt int colorInt) {
            return '#' + Integer.toHexString(colorInt);
        }

        public static String toHex(@ColorInt int colorInt, boolean ignoreAlpha) {
            return ignoreAlpha ? toHex(colorInt).substring(2) : toHex(colorInt);
        }

        public static String toHex(int red, int green, int blue) {

        }

        public static String toHex(int red, int green, int blue, int alpha) {

        }

        public static String toHex(float hue, float saturation, float value) {

        }

        public static RGBColor toRGB(@ColorInt int colorInt) {
            return new RGBColor(colorInt);
        }

        public static RGBColor toRGB(@NotNull String hex) {

        }

        public static RGBColor toRGB(float hue, float saturation, float value) {

        }

        public static HSVColor toHSV(@ColorInt int colorInt) {
            return new HSVColor(colorInt);
        }

        public static HSVColor toHSV(@NotNull String hex) {
            return new HSVColor()
        }

        public static HSVColor toHSV(int red, int green, int blue) {
            return new HSVColor(red, green, blue);
        }

    }
     */

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

    public static class CMYKColor implements ColorModel {

        // Variables.....
        public int cyan;
        public int magenta;
        public int yellow;
        public int black;

        // Constructors.....
        public CMYKColor() {}

        public CMYKColor(@ColorInt int color, boolean useBlackInk) {
            setValuesFromRGBColor(new RGBColor(color), useBlackInk);
        }

        public CMYKColor(@NotNull String hex, boolean useBlackInk) {
            setValuesFromRGBColor(new RGBColor(hex), useBlackInk);
        }

        public CMYKColor(int cyan, int magenta, int yellow) {
            this(cyan, magenta, yellow, 0);
        }

        public CMYKColor(int cyan, int magenta, int yellow, int black) {
            this.cyan = cyan;
            this.magenta = magenta;
            this.yellow = yellow;
            this.black = black;
        }

        // Methods.....
        protected void setValuesFromRGBColor(@NotNull RGBColor rgbColor, boolean useBlackInk) {
            this.black = useBlackInk ? Math.max(Math.max(rgbColor.red, rgbColor.green), rgbColor.blue) : 0;
            this.cyan = (int) ((1-(rgbColor.red/255f)-black)/(1-black));
            this.magenta = (int) ((1-(rgbColor.green/255f)-black)/(1-black));
            this.yellow = (int) ((1-(rgbColor.blue/255f)-black)/(1-black));
        }

        protected RGBColor toRGBColor() {
            return new RGBColor(255*(1-cyan/100)*(1-black/100), 255*(1-magenta/100)*(1-black/100), 255*(1-yellow/100)*(1-black/100));
        }

        @Override public int toInt() {
            return toRGBColor().toInt();
        }

        @Override public String toHex() {
            return toRGBColor().toHex();
        }

        @Override public String toHex(boolean ignoreAlpha) {
            return toRGBColor().toHex(ignoreAlpha);
        }

    }

    /*
    public static class HSVColor implements ColorModel {

        // Variables.....
        public final float hue;
        public final float saturation;
        public final float value;

        // Constructors.....
        public HSVColor(@ColorInt int color) {
            float[] hsv = new float[3];
            RGBColor rgbaComponents = convert.toRGB(color);
            android.graphics.Color.RGBToHSV(rgbaComponents.red, rgbaComponents.green, rgbaComponents.blue, hsv);

            this.hue = hsv[0];
            this.saturation = hsv[1];
            this.value = hsv[2];
        }

        public HSVColor(int red, int green, int blue) {
            float[] hsv = new float[3];
            android.graphics.Color.RGBToHSV(red, green, blue, hsv);

            this.hue = hsv[0];
            this.saturation = hsv[1];
            this.value = hsv[2];
        }

    }
    */

    // Interfaces.....
    public interface ColorModel {

        @ColorInt int toInt();
        String toHex();
        String toHex(boolean ignoreAlpha);

    }

}
