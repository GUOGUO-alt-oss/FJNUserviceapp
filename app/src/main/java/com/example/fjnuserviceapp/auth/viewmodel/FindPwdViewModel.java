package com.example.fjnuserviceapp.auth.viewmodel;

import android.app.Application;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.fjnuserviceapp.auth.data.model.ApiResponse;
import com.example.fjnuserviceapp.auth.repository.AuthRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPwdViewModel extends AndroidViewModel {
    private final AuthRepository repository;

    private final MutableLiveData<AuthState> state = new MutableLiveData<>(AuthState.IDLE);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> countdown = new MutableLiveData<>(0);
    
    // Form data
    public final MutableLiveData<String> phone = new MutableLiveData<>("");
    public final MutableLiveData<String> code = new MutableLiveData<>("");
    public final MutableLiveData<String> newPassword = new MutableLiveData<>("");
    public final MutableLiveData<String> confirmPassword = new MutableLiveData<>("");
    
    public final MutableLiveData<Integer> currentStep = new MutableLiveData<>(1);

    public FindPwdViewModel(@NonNull Application application) {
        super(application);
        repository = new AuthRepository(application);
    }
    
    public LiveData<AuthState> getState() {
        return state;
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
        
        startCountdown();
        
        repository.sendCode(ph, 3).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (!response.isSuccessful() || (response.body() != null && response.body().getCode() != 200)) {
                    errorMessage.setValue(response.body() != null ? response.body().getMessage() : "发送失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                errorMessage.setValue("网络错误");
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
    
    public void verifyCode() {
        String ph = phone.getValue();
        String cd = code.getValue();
        
        if (cd == null || cd.isEmpty()) {
            errorMessage.setValue("请输入验证码");
            return;
        }
        
        state.setValue(AuthState.LOADING);
        
        repository.verifyCode(ph, cd, 3).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    state.setValue(AuthState.IDLE);
                    currentStep.setValue(3);
                } else {
                    state.setValue(AuthState.ERROR);
                    errorMessage.setValue(response.body() != null ? response.body().getMessage() : "验证失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                state.setValue(AuthState.ERROR);
                errorMessage.setValue("网络错误");
            }
        });
    }

    public void resetPassword() {
        String ph = phone.getValue();
        String cd = code.getValue();
        String pwd = newPassword.getValue();
        String cfm = confirmPassword.getValue();
        
        if (pwd == null || pwd.isEmpty()) {
            errorMessage.setValue("请输入新密码");
            return;
        }
        if (!pwd.equals(cfm)) {
            errorMessage.setValue("两次密码不一致");
            return;
        }
        
        state.setValue(AuthState.LOADING);
        
        repository.resetPassword(ph, cd, pwd).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                    state.setValue(AuthState.SUCCESS);
                } else {
                    state.setValue(AuthState.ERROR);
                    errorMessage.setValue(response.body() != null ? response.body().getMessage() : "重置失败");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                state.setValue(AuthState.ERROR);
                errorMessage.setValue("网络错误");
            }
        });
    }
}
