package com.example.fjnuserviceapp.base.entity;

// 联系人实体类
public class Contact {
    private String id;       // 联系人ID
    private String name;     // 联系人名称
    private String avatar;   // 头像（暂用占位符）
    private String lastMsg;  // 最后一条消息
    private long lastTime;   // 最后一条消息时间

    // Getter & Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getLastMsg() { return lastMsg; }
    public void setLastMsg(String lastMsg) { this.lastMsg = lastMsg; }

    public long getLastTime() { return lastTime; }
    public void setLastTime(long lastTime) { this.lastTime = lastTime; }
}
