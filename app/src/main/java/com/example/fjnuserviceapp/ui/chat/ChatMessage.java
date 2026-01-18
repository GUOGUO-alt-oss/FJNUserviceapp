package com.example.fjnuserviceapp.ui.chat;

public class ChatMessage {
    private String content;
    private boolean isUser; // true: User (Right), false: System (Left)

    public ChatMessage(String content, boolean isUser) {
        this.content = content;
        this.isUser = isUser;
    }

    public String getContent() {
        return content;
    }

    public boolean isUser() {
        return isUser;
    }
}