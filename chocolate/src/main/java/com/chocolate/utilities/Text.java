package com.chocolate.utilities;

public final class Text extends UtilityClass {

    // Constants.....
    public static final int camelCase = 1;
    public static final int snake_case = 2;
    public static final int PascalCase = 3;
    public static final int UPPER_SNAKE_CASE = 4;

    // Methods.....
    public static String capitalizeFirstLetter(String string) {
        return string == null || string.isEmpty() ? string : string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    public static String capitalize(String string) {
        String[] words = string.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
        }
        return builder.toString().trim();
    }

    // Classes.....
    public static final class convert {



    }

}
