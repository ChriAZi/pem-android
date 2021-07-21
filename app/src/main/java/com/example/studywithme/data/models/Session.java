package com.example.studywithme.data.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.studywithme.utils.Constants;
import com.google.firebase.Timestamp;

import java.io.Serializable;

/**
 * Model Class: Represents a Focus-Session
 */
public class Session implements Serializable {

    /**
     * a unique database ID
     */
    private String uid;
    /**
     * the duration of a session (set by the owner)
     */
    private int duration;
    /**
     * timestamp indicating when a session was created on the server
     *
     * @see Timestamp
     */
    private Timestamp startedAt;
    /**
     * indicating whether a session is currently active
     */
    private boolean active;
    /**
     * indicating whether other users can join a session
     */
    private boolean isPublic;
    /**
     * indicating whether a partner is currently joining a session
     */
    private boolean hasPartner;

    /**
     * the owner (creator) of a session
     *
     * @see User
     */
    private User owner;
    /**
     * the partner of a session
     *
     * @see User
     */
    private User partner;
    /**
     * the settings as set by the owner of a session
     *
     * @see SessionSetting
     */
    private SessionSetting ownerSetting;
    /**
     * the settings as set by the partner of a session
     *
     * @see SessionSetting
     */
    private SessionSetting partnerSetting;

    /**
     * the reflection as made by the owner of a session
     *
     * @see SessionReflection
     */
    private SessionReflection ownerReflection;
    /**
     * the reflection as made by the partner of a session
     *
     * @see SessionReflection
     */
    private SessionReflection partnerReflection;

    /**
     * Empty constructor for FireStore mapping
     */
    public Session() {
    }

    public Session(int duration, boolean isPublic, SessionSetting ownerSetting) {
        this.duration = duration;
        this.isPublic = isPublic;
        this.ownerSetting = ownerSetting;
    }

    /**
     * Convenience method for accessing the active session ID from the shared preferences
     *
     * @param context the application context
     * @return the ID of the currently active session
     */
    public static String getIdFromPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(Constants.SESSION_ID, null);
    }

    /**
     * Convenience method for storing the active session ID in the shared preferences
     *
     * @param context   the application context
     * @param sessionId the ID of the currently active session
     */
    public static void setIdInPreferences(Context context, String sessionId) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SESSION_ID, sessionId);
        editor.apply();
    }


    public String getUid() {
        return uid;
    }

    public int getDuration() {
        return duration;
    }

    public Timestamp getStartedAt() {
        return startedAt;
    }

    public boolean isActive() {
        return active;
    }

    public User getOwner() {
        return owner;
    }

    public User getPartner() {
        return partner;
    }

    public SessionSetting getOwnerSetting() {
        return ownerSetting;
    }

    public SessionSetting getPartnerSetting() {
        return partnerSetting;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setStartedAt(Timestamp startedAt) {
        this.startedAt = startedAt;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setPartner(User partner) {
        this.partner = partner;
    }

    public void setOwnerSetting(SessionSetting ownerSetting) {
        this.ownerSetting = ownerSetting;
    }

    public void setPartnerSetting(SessionSetting partnerSetting) {
        this.partnerSetting = partnerSetting;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public SessionReflection getOwnerReflection() {
        return ownerReflection;
    }

    public SessionReflection getPartnerReflection() {
        return partnerReflection;
    }

    public void setOwnerReflection(SessionReflection ownerReflection) {
        this.ownerReflection = ownerReflection;
    }

    public void setPartnerReflection(SessionReflection partnerReflection) {
        this.partnerReflection = partnerReflection;
    }

    public boolean getHasPartner() {
        return hasPartner;
    }

    public void setHasPartner(boolean hasPartner) {
        this.hasPartner = hasPartner;
    }
}
