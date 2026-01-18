package com.example.fjnuserviceapp.base.entity;
public class ChatMessage {
    private String content = ""; private boolean isMe;
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content == null ? "" : content; }
    public boolean isMe() { return isMe; }
    public void setMe(boolean me) { isMe = me; }
}