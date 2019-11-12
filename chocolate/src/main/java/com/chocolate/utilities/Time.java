package com.chocolate.utilities;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressWarnings({"unused", "WeakerAccess"})
public final class Time extends UtilityClass {

    // Constants.....
    public static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String ISO_DATE_FORMAT_ALTERNATIVE = "yyyy-MM-dd'T'HH:mm:ss";

    // Methods.....
    public static boolean isSameDay(@NotNull Date date1, @NotNull Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(date1);
        calendar2.setTime(date2);
        return calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR) && calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
    }

    // Classes.....
    public static final class convert extends UtilityClass {

        // Methods.....
        public static Date toDate(int timestamp) {
            return toDate(toMillis(timestamp));
        }

        public static Date toDate(long millis) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(millis);
            return calendar.getTime();
        }

        public static Date toDate(@NotNull String string, @NotNull String format) throws ParseException {
            return new SimpleDateFormat(format, Locale.getDefault()).parse(string);
        }

        public static Date toDate(@NotNull String isoDate) throws ISODateParseException {
            try {
                return new SimpleDateFormat(ISO_DATE_FORMAT, Locale.getDefault()).parse(isoDate);
            } catch (ParseException exception1) {
                try {
                    return new SimpleDateFormat(ISO_DATE_FORMAT_ALTERNATIVE, Locale.getDefault()).parse(isoDate);
                } catch (ParseException exception2) {
                    throw new ISODateParseException(exception1, exception2);
                }
            }
        }

        public static int toTimestamp(@NotNull Date date) {
            return toTimestamp(toMillis(date));
        }

        public static int toTimestamp(long millis) {
            return (int) (millis / 1000);
        }

        public static int toTimestamp(@NotNull String string, @NotNull String format) throws ParseException {
            return toTimestamp(toDate(string, format));
        }

        public static int toTimestamp(@NotNull String isoDate) throws ISODateParseException {
            return toTimestamp(toDate(isoDate));
        }

        public static long toMillis(@NotNull Date date) {
            return date.getTime();
        }

        public static long toMillis(int timestamp) {
            return timestamp * 1000;
        }

        public static long toMillis(@NotNull String string, @NotNull String format) throws ParseException {
            return toMillis(toDate(string, format));
        }

        public static long toMillis(@NotNull String isoDate) throws ISODateParseException {
            return toMillis(toDate(isoDate));
        }

        public static String toString(@NotNull Date date, @NotNull String format) {
            return new SimpleDateFormat(format, Locale.getDefault()).format(date);
        }

        public static String toString(int timestamp, @NotNull String format) {
            return toString(toDate(timestamp), format);
        }

        public static String toString(long millis, @NotNull String format) {
            return toString(toDate(millis), format);
        }

    }

    public static final class current extends UtilityClass {

        // Methods.....
        @NotNull public static Date date() {
            return date(TimeZone.getDefault());
        }

        @NotNull public static Date date(@NotNull TimeZone timeZone) {
            return Calendar.getInstance(timeZone).getTime();
        }

        public static int timestamp() {
            return timestamp(TimeZone.getDefault());
        }

        public static int timestamp(@NotNull TimeZone timeZone) {
            return convert.toTimestamp(current.millis());
        }

        public static long millis() {
            return millis(TimeZone.getDefault());
        }

        public static long millis(@NotNull TimeZone timeZone) {
            return Calendar.getInstance(timeZone).getTimeInMillis();
        }

        @NotNull public static String formatted(@NotNull String format) {
            return formatted(TimeZone.getDefault(), format);
        }

        @NotNull public static String formatted(@NotNull TimeZone timeZone, @NotNull String format) {
            return convert.toString(date(timeZone), format);
        }

    }

    // Exceptions.....
    public static final class ISODateParseException extends Exception {
        public final ParseException parseExceptionWithTimeZone;
        public final ParseException parseExceptionWithoutTimeZone;

        public ISODateParseException(ParseException parseExceptionWithTimeZone, ParseException parseExceptionWithoutTimeZone) {
            this.parseExceptionWithTimeZone = parseExceptionWithTimeZone;
            this.parseExceptionWithoutTimeZone = parseExceptionWithoutTimeZone;
        }

        public String getMessage() {
            return this.parseExceptionWithTimeZone.getMessage() + "\n\n" + this.parseExceptionWithoutTimeZone.getMessage();
        }

        public String getLocalizedMessage() {
            return this.parseExceptionWithTimeZone.getLocalizedMessage() + "\n\n" + this.parseExceptionWithoutTimeZone.getLocalizedMessage();
        }

        public String toString() {
            return this.parseExceptionWithTimeZone.toString() + "\n\n" + this.parseExceptionWithoutTimeZone.toString();
        }

        public void printStackTrace() {
            this.parseExceptionWithTimeZone.printStackTrace();
            this.parseExceptionWithoutTimeZone.printStackTrace();
        }

        public void printStackTrace(@NonNull PrintStream printStream) {
            this.parseExceptionWithTimeZone.printStackTrace(printStream);
            this.parseExceptionWithoutTimeZone.printStackTrace(printStream);
        }

        public void printStackTrace(@NonNull PrintWriter printWriter) {
            this.parseExceptionWithTimeZone.printStackTrace(printWriter);
            this.parseExceptionWithoutTimeZone.printStackTrace(printWriter);
        }

        @NonNull public StackTraceElement[] getStackTrace() {
            return Collections.concatenate(this.parseExceptionWithTimeZone.getStackTrace(), this.parseExceptionWithoutTimeZone.getStackTrace());
        }
    }

}
