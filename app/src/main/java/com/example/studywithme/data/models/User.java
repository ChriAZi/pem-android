package com.example.studywithme.data.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.studywithme.utils.Constants;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

/**
 * Model Class: Represents a User of the application
 */
public class User implements Serializable {
    /**
     * a unique database ID
     */
    private String uid;
    /**
     * the username as set by the user
     */
    private String name;
    /**
     * the email as set by the user
     */
    @SuppressWarnings("WeakerAccess")
    private String email;

    /**
     * indicating whether the user is authenticated by the server
     */
    @Exclude
    private boolean isAuthenticated;
    /**
     * indicating whether the user is new to the application
     */
    @Exclude
    private boolean isNew;
    /**
     * indicating whether the user has been stored in the database
     */
    @Exclude
    private boolean isCreated;

    /**
     * Empty constructor for FireStore mapping
     */
    public User() {
    }

    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    /**
     * Convenience method for accessing the active user ID from the shared preferences
     *
     * @param context the application context
     * @return the ID of the currently logged-in user
     */
    public static String getIdFromPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(Constants.USER_ID, null);
    }

    /**
     * Convenience method for storing the active user ID in the shared preferences
     *
     * @param context the application context
     * @param userId  the ID of the currently logged-in user
     */
    public static void setIdInPreferences(String userId, Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.USER_ID, userId);
        editor.apply();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isCreated() {
        return isCreated;
    }

    public void setCreated(boolean created) {
        isCreated = created;
    }

}