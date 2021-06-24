package com.example.studywithme.data.models;

import java.util.List;

public class SessionReflection {

    private String feedback;
    private List<String> distractions;

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
