package com.chocolate.utilities;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

@SuppressWarnings({"unused", "WeakerAccess", "RedundantSuppression"})
public final class Text extends UtilityClass {

    // Constants.....
    private final static String REGEX_UPPER_CASE = ".*[A-Z]";
    private final static String REGEX_LOWER_CASE = ".*[a-z]";
    private final static String REGEX_NUMBERS = ".*[0-9]";
    private final static String REGEX_SPECIAL_CHARACTERS = ".*[^a-zA-Z0-9]";

    // Methods.....
    public static String capitalizeFirstLetter(String string) {
        return string == null || string.isEmpty() ? string : string.substring(0, 1).toUpperCase(Locale.getDefault()) + string.substring(1);
    }

    public static String capitalize(String string) {
        String[] words = string.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return builder.toString().trim();
    }

    private static String buildRegex(@NotNull String repeat, int amount) {
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < amount; i++) {
            regex.append(repeat);
        }
        return regex.toString() + ".*";
    }

    public static boolean hasAtLeastOneUpperCase(@NotNull String string) {
        return string.matches(REGEX_UPPER_CASE + ".*");
    }

    public static boolean hasAtLeastOneLowerCase(@NotNull String string) {
        return string.matches(REGEX_LOWER_CASE + ".*");
    }

    public static boolean hasAtLeastOneNumbers(@NotNull String string) {
        return string.matches(REGEX_NUMBERS + ".*");
    }

    public static boolean hasAtLeastOneSpecialCharacters(@NotNull String string) {
        return string.matches(REGEX_SPECIAL_CHARACTERS + ".*");
    }

    public static boolean hasAtLeastNAmountOfUpperCase(@NotNull String string, int amount) {
        return string.matches(buildRegex(REGEX_UPPER_CASE, amount));
    }

    public static boolean hasAtLeastNAmountOfLowerCase(@NotNull String string, int amount) {
        return string.matches(buildRegex(REGEX_LOWER_CASE, amount));
    }

    public static boolean hasAtLeastNAmountOfNumbers(@NotNull String string, int amount) {
        return string.matches(buildRegex(REGEX_NUMBERS, amount));
    }

    public static boolean hasAtLeastNAmountOfSpecialCharacters(@NotNull String string, int amount) {
        return string.matches(buildRegex(REGEX_SPECIAL_CHARACTERS, amount));
    }

}
