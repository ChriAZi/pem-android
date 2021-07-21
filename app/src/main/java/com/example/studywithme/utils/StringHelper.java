package com.example.studywithme.utils;

/**
 * a helper class for the String class
 */
public class StringHelper {
    /**
     * static method to capitalize a string
     *
     * @param str the String to be capitalized
     * @return the capitalized String
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
