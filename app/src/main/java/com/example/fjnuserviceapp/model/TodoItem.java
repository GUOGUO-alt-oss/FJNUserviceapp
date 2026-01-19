package com.example.fjnuserviceapp.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todos")
public class TodoItem {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title; // 课程/科目
    private String subtitle; // 章节/任务描述
    private int totalDuration; // 预计时长 (min)
    private int completedDuration; // 已完成时长 (min)
    private int state; // 0: PENDING, 1: ACTIVE, 2: DONE
    private long createdTime;

    // Constants for State
    public static final int STATE_PENDING = 0;
    public static final int STATE_ACTIVE = 1;
    public static final int STATE_DONE = 2;

    public TodoItem(String title, String subtitle, int totalDuration, int state, long createdTime) {
        this.title = title;
        this.subtitle = subtitle;
        this.totalDuration = totalDuration;
        this.state = state;
        this.createdTime = createdTime;
        this.completedDuration = 0;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public int getCompletedDuration() {
        return completedDuration;
    }

    public void setCompletedDuration(int completedDuration) {
        this.completedDuration = completedDuration;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }
}
