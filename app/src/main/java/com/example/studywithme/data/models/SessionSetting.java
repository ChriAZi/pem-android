package com.example.studywithme.data.models;

import java.io.Serializable;
import java.util.List;

/**
 * Model Class: Represents a Session Setting for a user of a session
 */
public class SessionSetting implements Serializable {
    /**
     * the name of a session as given by the user
     */
    private String name;
    /**
     * the goal of a session as set by the user
     */
    private String goal;
    /**
     * the category as set by the user
     *
     * @see SessionCategory
     */
    private SessionCategory category;
    /**
     * A list of tasks as created by the user
     * @see SessionTask
     */
    private List<SessionTask> tasks;

    /**
     * Empty constructor for FireStore mapping
     */
    public SessionSetting() {

    }

    public SessionSetting(String name, String goal, SessionCategory category, List<SessionTask> tasks) {
        this.name = name;
        this.goal = goal;
        this.category = category;
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public SessionCategory getCategory() {
        return category;
    }

    public void setCategory(SessionCategory category) {
        this.category = category;
    }

    public List<SessionTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<SessionTask> tasks) {
        this.tasks = tasks;
    }
}
