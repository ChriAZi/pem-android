package com.example.studywithme.utils;

import android.util.Log;

/**
 * helper class for logging
 */
public class Logger {
    public static void log(String message) {
        Log.d(Constants.TAG, message);
    }
}
