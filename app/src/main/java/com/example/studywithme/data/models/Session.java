package com.example.studywithme.data.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.studywithme.utils.Constants;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class Session implements Serializable {
    private String uid;

    private int duration;
    private Timestamp startedAt;
    private boolean active;

    private DocumentReference owner;
    private DocumentReference partner;
    private SessionSetting ownerSetting;
    private SessionSetting partnerSetting;

    public Session() {

    }

    public Session(String uid, int duration,DocumentReference owner) {
          this.duration = duration;
          this.uid =uid;
          this.owner = owner;

    }

    public Session(int duration, Timestamp startedAt, boolean active, DocumentReference owner, DocumentReference partner, SessionSetting ownerSetting, SessionSetting partnerSetting) {
        this.duration = duration;
        this.startedAt = startedAt;
        this.active = active;
        this.owner = owner;
        this.partner = partner;
        this.ownerSetting = ownerSetting;
        this.partnerSetting = partnerSetting;
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

    public DocumentReference getOwner() {
        return owner;
    }

    public DocumentReference getPartner() {
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

    public void setOwner(DocumentReference owner) {
        this.owner = owner;
    }

    public void setPartner(DocumentReference partner) {
        this.partner = partner;
    }

    public void setOwnerSetting(SessionSetting ownerSetting) {
        this.ownerSetting = ownerSetting;
    }

    public void setPartnerSetting(SessionSetting partnerSetting) {
        this.partnerSetting = partnerSetting;
    }

    public static String getIdFromPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(Constants.SESSION_ID, null);
    }

    public static void setIdInPreferences(String sessionId, Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constants.SESSION_ID, sessionId);
        editor.apply();
    }
}
