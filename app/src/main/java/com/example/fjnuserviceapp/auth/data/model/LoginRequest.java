package com.example.fjnuserviceapp.auth.data.model;

public class LoginRequest {
    private String account;
    private String password;
    private String deviceType;
    private String deviceToken;

    public LoginRequest(String account, String password, String deviceType, String deviceToken) {
        this.account = account;
        this.password = password;
        this.deviceType = deviceType;
        this.deviceToken = deviceToken;
    }

    // Getters and Setters
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
