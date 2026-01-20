package com.example.fjnuserviceapp.auth.network;

import com.example.fjnuserviceapp.auth.data.model.ApiResponse;
import com.example.fjnuserviceapp.auth.data.model.AuthResponse;
import com.example.fjnuserviceapp.auth.data.model.ChangePwdRequest;
import com.example.fjnuserviceapp.auth.data.model.LoginRequest;
import com.example.fjnuserviceapp.auth.data.model.RegisterRequest;
import com.example.fjnuserviceapp.auth.data.model.ResetPwdRequest;
import com.example.fjnuserviceapp.auth.data.model.SendCodeRequest;
import com.example.fjnuserviceapp.auth.data.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import com.example.fjnuserviceapp.auth.data.model.RefreshTokenRequest;
import com.example.fjnuserviceapp.auth.data.model.VerifyCodeRequest;

public interface AuthApi {
    // 用户注册
    @POST("auth/register")
    Call<ApiResponse<AuthResponse>> register(@Body RegisterRequest request);

    // 用户登录
    @POST("auth/login")
    Call<ApiResponse<AuthResponse>> login(@Body LoginRequest request);

    // 发送验证码
    @POST("auth/send-code")
    Call<ApiResponse<Void>> sendCode(@Body SendCodeRequest request);

    // 验证验证码
    @POST("auth/verify-code")
    Call<ApiResponse<Void>> verifyCode(@Body VerifyCodeRequest request);
    
    // 刷新Token
    @POST("auth/refresh-token")
    Call<ApiResponse<AuthResponse>> refreshToken(@Body RefreshTokenRequest request);

    // 找回密码
    @POST("auth/reset-password")
    Call<ApiResponse<Void>> resetPassword(@Body ResetPwdRequest request);

    // 修改密码
    @POST("auth/change-password")
    Call<ApiResponse<Void>> changePassword(@Header("Authorization") String token, @Body ChangePwdRequest request);

    // 获取用户信息
    @GET("auth/profile")
    Call<ApiResponse<User>> getUserProfile(@Header("Authorization") String token);

    // 退出登录
    @POST("auth/logout")
    Call<ApiResponse<Void>> logout(@Header("Authorization") String token);
}
