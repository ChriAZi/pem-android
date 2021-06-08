package com.example.studywithme.data.models;

import java.io.Serializable;

public class SessionTask implements Serializable {
    private String uid;
    private String description;
    private boolean done;

    public SessionTask() {

    }

    public SessionTask(String description, boolean done) {
        this.description = description;
        this.done = done;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
