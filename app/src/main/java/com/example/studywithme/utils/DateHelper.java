package com.example.studywithme.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateHelper {
    public static String formatDate(double date) {
        SimpleDateFormat sfd = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);
        return sfd.format(date);
    }
}
