package com.example.studywithme.data.models;

import java.util.List;

/**
 * Model Class: Represents a Session Reflection for a user of a session
 */
public class SessionReflection {

    /**
     * the written feedback as entered by the user
     */
    private String feedback;
    /**
     * a list of distractions as entered by the user
     */
    private List<String> distractions;

    /**
     * Empty constructor for FireStore mapping
     */
    public SessionReflection() {

    }

    public SessionReflection(String feedback, List<String> distractions) {
        this.feedback = feedback;
        this.distractions = distractions;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public List<String> getDistractions() {
        return distractions;
    }

    public void setDistractions(List<String> distractions) {
        this.distractions = distractions;
    }
}
