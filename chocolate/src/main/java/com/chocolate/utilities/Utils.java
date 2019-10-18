package com.chocolate.utilities;

public class Utils extends UtilityClass {

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
