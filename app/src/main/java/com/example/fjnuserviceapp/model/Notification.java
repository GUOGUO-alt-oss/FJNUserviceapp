package com.example.fjnuserviceapp.model;

import java.io.Serializable;

public class Notification implements Serializable {
    private String id;
    private String title;
    private String content;
    private String time;
    private String sender;
    private boolean isRead;
    private int type; // 0: College, 1: Chat, 2: System

    public static final int TYPE_COLLEGE = 0;
    public static final int TYPE_CHAT = 1;
    public static final int TYPE_SYSTEM = 2;

    // 新增：优先级字段（0=普通，1=重要（星标），2=紧急（高亮星标））
    private int priority;

    // Getter/Setter（必须加）
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public Notification(String id, String title, String content, String time, String sender, int type) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.time = time;
        this.sender = sender;
        this.type = type;
        this.isRead = false;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getTime() { return time; }
    public String getSender() { return sender; }
    public boolean isRead() { return isRead; }
    public int getType() { return type; }

    public void setRead(boolean read) { isRead = read; }
}