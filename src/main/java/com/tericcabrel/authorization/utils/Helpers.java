package com.tericcabrel.authorization.utils;

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
}
