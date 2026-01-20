package com.example.fjnuserviceapp.auth.data.model;

public class ResetPwdRequest {
    private String phone;
    private String code;
    private String newPassword;

    public ResetPwdRequest(String phone, String code, String newPassword) {
        this.phone = phone;
        this.code = code;
        this.newPassword = newPassword;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
