package com.chocolate.logger;

import android.util.Log;

import androidx.annotation.NonNull;

import com.chocolate.utilities.Collections;
import com.chocolate.utilities.UtilityClass;

import java.util.Collection;

@SuppressWarnings("WeakerAccess")
final class LoggerUtils extends UtilityClass {

    // Methods.....
    static String getCallerCallerCallerClassName() {
        String className =  Thread.currentThread().getStackTrace()[7].getClassName();
        return className.substring(className.lastIndexOf('.') + 1);
    }

    static void log(String tag, @Logger.LoggerStyle int logStyle, @NonNull String log) {
        for (int i = 0; i <= log.length() / 1000; i++) {
            final int start = i * 1000;
            int end = (i+1) * 1000;
            final boolean isLast = end <= log.length();
            end = isLast ? end : log.length();
            performLog(tag + (i == 0 && !isLast ? "" : ("_" + (i + 1))), logStyle, log.substring(start, end));
        }
    }

    private static void performLog(String tag, @Logger.LoggerStyle int logStyle, @NonNull String log) {
        tag = Logger.tagPrefix + tag;
        switch (logStyle) {
            case Logger.STYLE_INFO:
                Log.i(tag, log);
                break;
            case Logger.STYLE_WARN:
                Log.w(tag, log);
                break;
            case Logger.STYLE_ERROR:
                Log.e(tag, log);
                break;
        }
    }

    static void logToString(String tag, @Logger.LoggerStyle int logStyle, Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                LoggerUtils.log(tag, logStyle, "NULL");
                return;
            }
            if (object instanceof Collection) {
                LoggerUtils.log(tag, logStyle, Collections.stringify((Collection<?>) object));
                return;
            }
            LoggerUtils.log(tag, logStyle, object.toString());
        }
    }

    static void logLineExecution(String tag, @Logger.LoggerStyle int logStyle, int stackOffset) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[5 + stackOffset];
        String className =  stackTraceElement.getClassName();
        className = className.substring(className.lastIndexOf('.') + 1);
        log(tag, logStyle, className + "." + stackTraceElement.getMethodName() + "() [" + stackTraceElement.getLineNumber() + "]");
    }

}
