package com.example.studywithme.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * helper class for showing Toasts throughout the application
 */
public class ToastMaster {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
