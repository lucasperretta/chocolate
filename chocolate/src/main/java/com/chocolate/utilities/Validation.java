package com.chocolate.utilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings({"WeakerAccess", "unused", "RedundantSuppression"})
public final class Validation {

    // Methods.....
    public static Validation start() {
        return new Validation();
    }

    public static void tryOr(@NotNull TryHandler handler, @NotNull ValidationFailedHandler failedHandler) {
        try {
            handler.handle();
        } catch (Exception exception) {
            failedHandler.handle(exception);
        }
    }

    public Validation validate(boolean expected, @NotNull String message) throws Exception {
        return validate(expected, message, null);
    }

    public Validation validate(boolean expected, @NotNull ValidationFailedHandler handler) throws Exception {
        return validate(expected, null, handler);
    }

    public Validation validate(boolean expected, String message, ValidationFailedHandler handler) throws Exception {
        if (!expected) {
            final Exception exception = new Exception(message);
            if (handler != null) handler.handle(exception);
            throw exception;
        }
        return this;
    }

    // Exceptions.....
    public static class Exception extends java.lang.Exception {

        // Constructors.....
        public Exception(@Nullable String message) {
            super(message);
        }

    }

    // Interfaces.....
    @SuppressWarnings("RedundantThrows")
    public interface TryHandler {
        void handle() throws Exception;
    }

    public interface ValidationFailedHandler {
        void handle(Exception exception);
    }

}
