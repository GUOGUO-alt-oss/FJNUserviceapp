package com.example.fjnuserviceapp.base.entity;

public class IdleItem {
    private String id;
    private String title = "";
    private String desc  = "";
    private double price;
    private String contact = "";
    private long   time;
    private String img = "";

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title == null ? "" : title; }
    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc == null ? "" : desc; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact == null ? "" : contact; }
    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img == null ? "" : img; }
}