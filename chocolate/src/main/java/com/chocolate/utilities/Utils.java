package com.chocolate.utilities;

@SuppressWarnings({"WeakerAccess", "RedundantSuppression", "unused"})
public final class Utils extends UtilityClass {

    /**
     * @return Returns the StackTraceElement of the current function
     */
    public static StackTraceElement getCurrentFunction() {
        return Thread.currentThread().getStackTrace()[3];
    }

    /**
     * @return Returns the StackTraceElement of the function that called the current function
     */
    public static StackTraceElement getCallingFunction() {
        return Thread.currentThread().getStackTrace()[4];
    }

}
