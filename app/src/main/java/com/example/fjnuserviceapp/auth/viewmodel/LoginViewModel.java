package com.example.fjnuserviceapp.auth.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fjnuserviceapp.auth.data.model.ApiResponse;
import com.example.fjnuserviceapp.auth.data.model.AuthResponse;
import com.example.fjnuserviceapp.auth.data.model.LoginRequest;
import com.example.fjnuserviceapp.auth.manager.PreferenceManager;
import com.example.fjnuserviceapp.auth.manager.SessionManager;
import com.example.fjnuserviceapp.auth.manager.TokenManager;
import com.example.fjnuserviceapp.auth.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private final AuthRepository repository;
    private final TokenManager tokenManager;
    private final SessionManager sessionManager;
    private final PreferenceManager preferenceManager;

    private final MutableLiveData<AuthState> loginState = new MutableLiveData<>(AuthState.IDLE);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    // Form data
    public final MutableLiveData<String> account = new MutableLiveData<>("");
    public final MutableLiveData<String> password = new MutableLiveData<>("");
    public final MutableLiveData<Boolean> rememberPassword = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> autoLogin = new MutableLiveData<>(false);

    public LoginViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
        tokenManager = new TokenManager(application);
        sessionManager = SessionManager.getInstance(tokenManager);
        preferenceManager = new PreferenceManager(application);
        
        // Load preferences
        rememberPassword.setValue(preferenceManager.isRememberPassword());
        autoLogin.setValue(preferenceManager.isAutoLogin());
        if (Boolean.TRUE.equals(rememberPassword.getValue())) {
            account.setValue(preferenceManager.getRememberedAccount());
            password.setValue(preferenceManager.getRememberedPassword()); // In real app, decrypt here
        }
    }

    public LiveData<AuthState> getLoginState() {
        return loginState;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void login() {
        String acc = account.getValue();
        String pwd = password.getValue();

        if (acc == null || acc.isEmpty()) {
            errorMessage.setValue("请输入账号");
            return;
        }
        if (pwd == null || pwd.isEmpty()) {
            errorMessage.setValue("请输入密码");
            return;
        }

        loginState.setValue(AuthState.LOADING);
        
        repository.login(new LoginRequest(acc, pwd, "android", null))
                .enqueue(new Callback<ApiResponse<AuthResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getCode() == 200) {
                                AuthResponse data = response.body().getData();
                                handleLoginSuccess(data);
                            } else {
                                loginState.setValue(AuthState.ERROR);
                                errorMessage.setValue(response.body().getMessage());
                            }
                        } else {
                            loginState.setValue(AuthState.ERROR);
                            errorMessage.setValue("登录失败: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                        loginState.setValue(AuthState.ERROR);
                        errorMessage.setValue("网络错误: " + t.getMessage());
                    }
                });
    }

    private void handleLoginSuccess(AuthResponse data) {
        tokenManager.saveToken(data.getToken());
        tokenManager.saveRefreshToken(data.getRefreshToken());
        tokenManager.saveTokenExpiresAt(data.getExpiresIn());
        sessionManager.saveSession(data.getUser());
        repository.saveUserToLocal(data.getUser());
        
        // Save preferences
        preferenceManager.saveRememberPassword(Boolean.TRUE.equals(rememberPassword.getValue()));
        preferenceManager.saveAutoLogin(Boolean.TRUE.equals(autoLogin.getValue()));
        
        if (Boolean.TRUE.equals(rememberPassword.getValue())) {
            preferenceManager.saveAccount(account.getValue(), password.getValue());
        } else {
            preferenceManager.saveAccount(account.getValue(), ""); // Clear password but keep account maybe? Or clear both.
            preferenceManager.clearPassword();
        }

        loginState.setValue(AuthState.SUCCESS);
    }
}
