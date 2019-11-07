package com.chocolate.logger;

import androidx.annotation.NonNull;

@SuppressWarnings({"WeakerAccess", "RedundantSuppression", "unused"})
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

    default void logLineExecution(@Logger.LoggerStyle int logStyle) {
        LoggerUtils.logLineExecution(getTag(), logStyle, 0);
    }

    default void logLineExecution(@Logger.LoggerStyle int logStyle, int stackOffset) {
        LoggerUtils.logLineExecution(getTag(), logStyle, stackOffset);
    }

    default void printLineExecution() {
        LoggerUtils.logLineExecution(getTag(), Logger.STYLE_INFO, 0);
    }

    default void warnLineExecution() {
        LoggerUtils.logLineExecution(getTag(), Logger.STYLE_WARN, 0);
    }

    default void errorLineExecution() {
        LoggerUtils.logLineExecution(getTag(), Logger.STYLE_ERROR, 0);
    }

    @NonNull default String getTag() {
        return this.getClass().getSimpleName();
    }

}
