package com.example.studywithme.data.models;

import java.io.Serializable;

/**
 * Model Class: Represents a Session Task for a user of a session
 */
public class SessionTask implements Serializable {
    /**
     * the description of a task as set by the user
     */
    private String description;
    /**
     * indicating whether the task is marked as done by the user
     */
    private boolean done;

    /**
     * Empty constructor for FireStore mapping
     */
    public SessionTask() {

    }

    public SessionTask(String description, boolean done) {
        this.description = description;
        this.done = done;
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
