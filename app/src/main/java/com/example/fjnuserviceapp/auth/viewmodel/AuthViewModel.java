package com.example.fjnuserviceapp.auth.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fjnuserviceapp.auth.data.model.ApiResponse;
import com.example.fjnuserviceapp.auth.data.model.AuthResponse;
import com.example.fjnuserviceapp.auth.data.model.LoginRequest;
import com.example.fjnuserviceapp.auth.data.model.RegisterRequest;
import com.example.fjnuserviceapp.auth.data.model.User;
import com.example.fjnuserviceapp.auth.manager.PreferenceManager;
import com.example.fjnuserviceapp.auth.manager.SessionManager;
import com.example.fjnuserviceapp.auth.manager.TokenManager;
import com.example.fjnuserviceapp.auth.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends AndroidViewModel {
    private final AuthRepository repository;
    private final TokenManager tokenManager;
    private final SessionManager sessionManager;
    private final PreferenceManager preferenceManager;

    private final MutableLiveData<AuthState> authState = new MutableLiveData<>(AuthState.IDLE);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();

    public AuthViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
        tokenManager = new TokenManager(application);
        sessionManager = SessionManager.getInstance(tokenManager);
        preferenceManager = new PreferenceManager(application);
    }

    public LiveData<AuthState> getAuthState() {
        return authState;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<User> getCurrentUser() {
        return currentUser;
    }

    public void login(String account, String password) {
        authState.setValue(AuthState.LOADING);
        repository.login(new LoginRequest(account, password, "android", null))
                .enqueue(new Callback<ApiResponse<AuthResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getCode() == 200) {
                                handleAuthSuccess(response.body().getData());
                            } else {
                                authState.setValue(AuthState.ERROR);
                                errorMessage.setValue(response.body().getMessage());
                            }
                        } else {
                            authState.setValue(AuthState.ERROR);
                            errorMessage.setValue("登录失败: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                        authState.setValue(AuthState.ERROR);
                        errorMessage.setValue("网络错误: " + t.getMessage());
                    }
                });
    }

    public void register(RegisterRequest request) {
        authState.setValue(AuthState.LOADING);
        repository.register(request).enqueue(new Callback<ApiResponse<AuthResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        handleAuthSuccess(response.body().getData());
                    } else {
                        authState.setValue(AuthState.ERROR);
                        errorMessage.setValue(response.body().getMessage());
                    }
                } else {
                    authState.setValue(AuthState.ERROR);
                    errorMessage.setValue("注册失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                authState.setValue(AuthState.ERROR);
                errorMessage.setValue("网络错误");
            }
        });
    }

    private void handleAuthSuccess(AuthResponse data) {
        tokenManager.saveToken(data.getToken());
        tokenManager.saveRefreshToken(data.getRefreshToken());
        tokenManager.saveTokenExpiresAt(data.getExpiresIn());
        sessionManager.saveSession(data.getUser());
        repository.saveUserToLocal(data.getUser());
        currentUser.setValue(data.getUser());
        authState.setValue(AuthState.SUCCESS);
    }

    public void sendCode(String phone, int type, Callback<ApiResponse<Void>> callback) {
        repository.sendCode(phone, type).enqueue(callback);
    }
    
    public void resetPassword(String phone, String code, String newPassword, Callback<ApiResponse<Void>> callback) {
        repository.resetPassword(phone, code, newPassword).enqueue(callback);
    }

    public void logout() {
        repository.logout().enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                // Ignore result
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                // Ignore
            }
        });
        sessionManager.clearSession();
        authState.setValue(AuthState.IDLE);
    }

    public boolean isAutoLogin() {
        return preferenceManager.isAutoLogin();
    }
    
    public boolean isLoggedIn() {
        return sessionManager.isLoggedIn();
    }
}
