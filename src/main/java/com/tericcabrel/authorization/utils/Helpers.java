package com.tericcabrel.authorization.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Helpers {
    /**
     * Generates a random string of the length passed in parameter
     *
     * @param length Length of the string to generate
     *
     * @return String
     */
    public static String generateRandomString(int length) {
        String possibleChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i += 1) {
            int position = (int) Math.floor(Math.random() * possibleChars.length());
            result.append(possibleChars.charAt(position));
        }

        return result.toString();
    }

    /**
     * Get the extension of the file name provided
     *
     * @param fileName Name of the file we want to get the extension
     *
     * @return a string representing the extension
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        String[] fileNameParts = fileName.split("\\.");

        return fileNameParts[fileNameParts.length - 1];
    }

    /**
     * Transform to uppercase the first character of a string
     *
     * @param str
     *
     * @return the string capitalized
     */
    public static String capitalize(String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);

        return String.valueOf(chars);
    }

    public static Map<String, List<String>> updateErrorHashMap(
        Map<String, List<String>> errors, String field, String message
    ) {
        if (errors.containsKey(field)) {
            List<String> strings = errors.get(field);
            strings.add(message);

            errors.put(field, strings);
        } else {
            List<String> strings = new ArrayList<>();
            strings.add(message);

            errors.put(field, strings);
        }

        return errors;
    }
}
