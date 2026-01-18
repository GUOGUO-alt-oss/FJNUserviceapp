package com.example.fjnuserviceapp.model;

public class FavoriteItem {
    private String title;
    private String time;
    private String type; // e.g., "通知", "失物招领"

    public FavoriteItem() {
    }

    public FavoriteItem(String title, String time, String type) {
        this.title = title;
        this.time = time;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
