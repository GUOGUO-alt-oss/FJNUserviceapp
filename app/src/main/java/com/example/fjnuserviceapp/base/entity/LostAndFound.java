package com.example.fjnuserviceapp.base.entity;

public class LostAndFound {
    private String id;
    private String title = "";
    private String desc  = "";
    private String location = "";
    private String contact  = "";
    private long   time;
    private int    category; // 0 丢失 1 拾得
    private String img = "";

    /* ==== Getter / Setter 生成即可 ==== */
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title == null ? "" : title; }
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc == null ? "" : desc; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location == null ? "" : location; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact == null ? "" : contact; }
    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }
    public int getCategory() { return category; }
    public void setCategory(int category) { this.category = category; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img == null ? "" : img; }
}