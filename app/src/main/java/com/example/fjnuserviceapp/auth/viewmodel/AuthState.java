package com.example.fjnuserviceapp.auth.viewmodel;

public enum AuthState {
    IDLE,      // 空闲
    LOADING,   // 加载中
    SUCCESS,   // 成功
    ERROR,     // 错误
    NEED_CAPTCHA,  // 需要验证码
    ACCOUNT_LOCKED  // 账号锁定
}
