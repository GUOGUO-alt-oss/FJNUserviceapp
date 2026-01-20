package com.example.fjnuserviceapp.auth.data.model;

public class VerifyCodeRequest {
    private String phone;
    private String code;
    private int type;

    public VerifyCodeRequest(String phone, String code, int type) {
        this.phone = phone;
        this.code = code;
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
