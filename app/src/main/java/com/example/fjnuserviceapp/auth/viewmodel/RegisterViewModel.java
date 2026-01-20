package com.example.fjnuserviceapp.auth.viewmodel;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fjnuserviceapp.auth.data.model.ApiResponse;
import com.example.fjnuserviceapp.auth.data.model.AuthResponse;
import com.example.fjnuserviceapp.auth.data.model.RegisterRequest;
import com.example.fjnuserviceapp.auth.manager.SessionManager;
import com.example.fjnuserviceapp.auth.manager.TokenManager;
import com.example.fjnuserviceapp.auth.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends AndroidViewModel {
    private final AuthRepository repository;
    private final TokenManager tokenManager;
    private final SessionManager sessionManager;

    private final MutableLiveData<AuthState> registerState = new MutableLiveData<>(AuthState.IDLE);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> countdown = new MutableLiveData<>(0);
    
    // Form data
    public final MutableLiveData<String> phone = new MutableLiveData<>("");
    public final MutableLiveData<String> code = new MutableLiveData<>("");
    public final MutableLiveData<String> password = new MutableLiveData<>("");
    public final MutableLiveData<String> confirmPassword = new MutableLiveData<>("");

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
        tokenManager = new TokenManager(application);
        sessionManager = SessionManager.getInstance(tokenManager);
    }

    public LiveData<AuthState> getRegisterState() {
        return registerState;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Integer> getCountdown() {
        return countdown;
    }

    public void sendCode() {
        String ph = phone.getValue();
        if (ph == null || ph.isEmpty()) {
            errorMessage.setValue("请输入手机号");
            return;
        }
        
        startCountdown(); // Start immediately for UX, cancel on failure
        
        repository.sendCode(ph, 1).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (!response.isSuccessful() || (response.body() != null && response.body().getCode() != 200)) {
                    errorMessage.setValue(response.body() != null ? response.body().getMessage() : "发送失败");
                    // Stop countdown if needed, but usually keep it to prevent spam
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                errorMessage.setValue("网络错误: " + t.getMessage());
            }
        });
    }

    private void startCountdown() {
        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown.setValue((int) (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                countdown.setValue(0);
            }
        }.start();
    }

    public void register() {
        String ph = phone.getValue();
        String cd = code.getValue();
        String pwd = password.getValue();
        String cfmPwd = confirmPassword.getValue();

        if (ph == null || ph.isEmpty()) {
            errorMessage.setValue("请输入手机号");
            return;
        }
        if (cd == null || cd.isEmpty()) {
            errorMessage.setValue("请输入验证码");
            return;
        }
        if (pwd == null || pwd.isEmpty()) {
            errorMessage.setValue("请输入密码");
            return;
        }
        if (!pwd.equals(cfmPwd)) {
            errorMessage.setValue("两次密码不一致");
            return;
        }

        registerState.setValue(AuthState.LOADING);
        
        repository.register(new RegisterRequest(ph, cd, pwd))
                .enqueue(new Callback<ApiResponse<AuthResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<AuthResponse>> call, Response<ApiResponse<AuthResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                            AuthResponse data = response.body().getData();
                            tokenManager.saveToken(data.getToken());
                            tokenManager.saveRefreshToken(data.getRefreshToken());
                            tokenManager.saveTokenExpiresAt(data.getExpiresIn());
                            sessionManager.saveSession(data.getUser());
                            repository.saveUserToLocal(data.getUser());
                            registerState.setValue(AuthState.SUCCESS);
                        } else {
                            registerState.setValue(AuthState.ERROR);
                            errorMessage.setValue(response.body() != null ? response.body().getMessage() : "注册失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<AuthResponse>> call, Throwable t) {
                        registerState.setValue(AuthState.ERROR);
                        errorMessage.setValue("网络错误");
                    }
                });
    }
}
