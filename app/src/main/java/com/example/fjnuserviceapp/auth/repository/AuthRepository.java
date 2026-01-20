package com.example.fjnuserviceapp.auth.repository;

import android.content.Context;

import com.example.fjnuserviceapp.auth.data.local.UserDao;
import com.example.fjnuserviceapp.auth.data.model.ApiResponse;
import com.example.fjnuserviceapp.auth.data.model.AuthResponse;
import com.example.fjnuserviceapp.auth.data.model.ChangePwdRequest;
import com.example.fjnuserviceapp.auth.data.model.LoginRequest;
import com.example.fjnuserviceapp.auth.data.model.RefreshTokenRequest;
import com.example.fjnuserviceapp.auth.data.model.RegisterRequest;
import com.example.fjnuserviceapp.auth.data.model.ResetPwdRequest;
import com.example.fjnuserviceapp.auth.data.model.SendCodeRequest;
import com.example.fjnuserviceapp.auth.data.model.User;
import com.example.fjnuserviceapp.auth.data.model.VerifyCodeRequest;
import com.example.fjnuserviceapp.auth.manager.TokenManager;
import com.example.fjnuserviceapp.auth.network.AuthApi;
import com.example.fjnuserviceapp.auth.network.NetworkClient;
import com.example.fjnuserviceapp.db.AppDatabase;

import retrofit2.Call;

public class AuthRepository {
    private AuthApi authApi;
    private UserDao userDao;
    private TokenManager tokenManager;

    public AuthRepository(Context context) {
        this.authApi = NetworkClient.getInstance(context).createService(AuthApi.class);
        this.userDao = AppDatabase.getDatabase(context).authUserDao();
        this.tokenManager = new TokenManager(context);
    }

    public Call<ApiResponse<AuthResponse>> login(LoginRequest request) {
        return authApi.login(request);
    }

    public Call<ApiResponse<AuthResponse>> register(RegisterRequest request) {
        return authApi.register(request);
    }

    public Call<ApiResponse<Void>> sendCode(String phone, int type) {
        return authApi.sendCode(new SendCodeRequest(phone, type));
    }

    public Call<ApiResponse<Void>> verifyCode(String phone, String code, int type) {
        return authApi.verifyCode(new VerifyCodeRequest(phone, code, type));
    }

    public Call<ApiResponse<AuthResponse>> refreshToken(String refreshToken) {
        return authApi.refreshToken(new RefreshTokenRequest(refreshToken));
    }

    public Call<ApiResponse<Void>> resetPassword(String phone, String code, String newPassword) {
        return authApi.resetPassword(new ResetPwdRequest(phone, code, newPassword));
    }

    public Call<ApiResponse<Void>> changePassword(String oldPwd, String newPwd) {
        String token = tokenManager.getToken();
        return authApi.changePassword("Bearer " + token, new ChangePwdRequest(oldPwd, newPwd));
    }

    public Call<ApiResponse<User>> getUserProfile() {
        String token = tokenManager.getToken();
        return authApi.getUserProfile("Bearer " + token);
    }

    public Call<ApiResponse<Void>> logout() {
        String token = tokenManager.getToken();
        return authApi.logout("Bearer " + token);
    }
    
    // Database operations can be added here, e.g., saveUser, getUser
    public void saveUserToLocal(User user) {
        new Thread(() -> userDao.insert(user)).start();
    }
}
