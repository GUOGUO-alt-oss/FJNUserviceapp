package com.example.fjnuserviceapp.auth.data.model;

public class SendCodeRequest {
    private String phone;
    private int type; // 1:注册, 2:登录, 3:找回密码

    public SendCodeRequest(String phone, int type) {
        this.phone = phone;
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
