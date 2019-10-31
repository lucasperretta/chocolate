package com.chocolate.logger;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;

import com.chocolate.utilities.UtilityClass;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Logger extends UtilityClass {

    // Logger.....
    private static final LoggerInterface logger = new LoggerInterface() {
        @NonNull @Override public String getTag() {
            return LoggerUtils.getCallerCallerCallerClassName();
        }
    };

    // @Interfaces.....
    @IntDef({ STYLE_INFO, STYLE_WARN, STYLE_ERROR })
    public @interface LoggerStyle {}

    // Constants.....
    public static final int STYLE_INFO = 1;
    public static final int STYLE_WARN = 2;
    public static final int STYLE_ERROR = 3;

    // Variables.....
    public static String tagPrefix = "APP_";

    // Methods.....
    public static void log(@LoggerStyle int logStyle, Object... objects) {
        logger.log(logStyle, objects);
    }

    public static void print(Object... objects) {
        logger.print(objects);
    }

    public static void warn(Object... objects) {
        logger.warn(objects);
    }

    public static void error(Object... objects) {
        logger.error(objects);
    }

}
