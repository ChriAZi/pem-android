package com.example.studywithme.data.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SessionSetting implements Serializable {
    private String name;
    private String goal;
    private int duration;
    private SessionCategory category;
    private ArrayList<SessionTask> tasks;

    public SessionSetting() {

    }

    public SessionSetting(String name, String goal, SessionCategory category, ArrayList<SessionTask> tasks) {
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

    public void setTasks(ArrayList<SessionTask> tasks) {
        this.tasks = tasks;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
