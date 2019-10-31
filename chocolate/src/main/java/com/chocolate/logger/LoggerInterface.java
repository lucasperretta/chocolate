package com.chocolate.logger;

import androidx.annotation.NonNull;

@SuppressWarnings({"WeakerAccess", "RedundantSuppression"})
public interface LoggerInterface {

    default void log(@Logger.LoggerStyle int logStyle, Object... objects) {
        LoggerUtils.logToString(getTag(), logStyle, objects);
    }

    default void print(Object... objects) {
        LoggerUtils.logToString(getTag(), Logger.STYLE_INFO, objects);
    }

    default void warn(Object... objects) {
        LoggerUtils.logToString(getTag(), Logger.STYLE_WARN, objects);
    }

    default void error(Object... objects) {
        LoggerUtils.logToString(getTag(), Logger.STYLE_ERROR, objects);
    }

    @NonNull default String getTag() {
        return this.getClass().getSimpleName();
    }

}
