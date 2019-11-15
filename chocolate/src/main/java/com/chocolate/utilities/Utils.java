package com.chocolate.utilities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

@SuppressWarnings({"WeakerAccess", "RedundantSuppression", "unused"})
public final class Utils extends UtilityClass {

    // Classes.....
    public static final class java extends UtilityClass {

        // Methods.....
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

        /**
         * @param className The name of the class you are looking for
         * @return Returns the class or null if it wasn't found
         */
        @Nullable public static Class<?> getClassByName(@NotNull String className) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * @param class_ The class in which you want to look for the function
         * @param methodName The name of the method you are looking for
         * @param parameterTypes The parameters that the method should accept
         * @return Returns the method or null if it wasn't found or if it's of private access
         */
        @Nullable public static Method getMethodByName(@NotNull Class<?> class_, @NotNull String methodName, Class<?>... parameterTypes) {
            try {
                return class_.getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * @param object The object in which you want to look for the function
         * @param methodName The name of the method you are looking for
         * @param parameterTypes The parameters that the method should accept
         * @return Returns the method or null if it wasn't found or if it's of private access
         */
        @Nullable public static Method getObjectMethodByName(@NotNull Object object, @NotNull String methodName, Class<?>... parameterTypes) {
            try {
                return object.getClass().getMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            }
        }

    }

}
