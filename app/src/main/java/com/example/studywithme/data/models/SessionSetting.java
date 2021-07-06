package com.example.studywithme.data.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SessionSetting implements Serializable {
    private String name;
    private String goal;
    private int duration;
    private ArrayList<SessionCategory> categories;
    private ArrayList<SessionTask> tasks;

    public SessionSetting() {

    }

    public SessionSetting(String name, String goal, ArrayList<SessionCategory> categories, ArrayList<SessionTask> tasks) {
        this.name = name;
        this.goal = goal;
        this.categories = categories;
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

    public List<SessionCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<SessionCategory> categories) {
        this.categories = categories;
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
