package com.example.studywithme.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * a helper class for the Date class
 */
public class DateHelper {
    /**
     * static method to format date based on the template string
     * @param date the Date to be formatted
     * @return the formatted Date String
     */
    public static String formatDate(double date) {
        SimpleDateFormat sfd = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
        return sfd.format(date);
    }
}
