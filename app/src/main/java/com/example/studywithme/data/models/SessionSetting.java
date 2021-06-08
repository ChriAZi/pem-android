package com.example.studywithme.data.models;

import java.io.Serializable;
import java.util.List;

public class SessionSetting implements Serializable {
    private String uid;
    private String name;
    private String goal;
    private List<SessionCategory> categories;
    private List<SessionTask> tasks;

    public SessionSetting() {

    }

    public SessionSetting(String name, String goal, List<SessionCategory> categories, List<SessionTask> tasks) {
        this.name = name;
        this.goal = goal;
        this.categories = categories;
        this.tasks = tasks;
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

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public List<SessionCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<SessionCategory> categories) {
        this.categories = categories;
    }

    public List<SessionTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<SessionTask> tasks) {
        this.tasks = tasks;
    }
}
