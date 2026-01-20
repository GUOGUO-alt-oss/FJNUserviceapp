package com.example.fjnuserviceapp.auth.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fjnuserviceapp.auth.viewmodel.AuthState;
import com.example.fjnuserviceapp.auth.viewmodel.RegisterViewModel;
import com.example.fjnuserviceapp.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        initView();
        observeViewModel();
    }

    private void initView() {
        binding.ivBack.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).goBack();
            }
        });

        binding.btnSendCode.setOnClickListener(v -> {
            viewModel.phone.setValue(binding.etPhone.getText().toString());
            viewModel.sendCode();
        });

        binding.btnRegister.setOnClickListener(v -> {
            viewModel.phone.setValue(binding.etPhone.getText().toString());
            viewModel.code.setValue(binding.etCode.getText().toString());
            viewModel.password.setValue(binding.etPassword.getText().toString());
            viewModel.confirmPassword.setValue(binding.etConfirmPassword.getText().toString());
            viewModel.register();
        });
    }

    private void observeViewModel() {
        viewModel.getRegisterState().observe(getViewLifecycleOwner(), state -> {
            if (state == AuthState.LOADING) {
                binding.loading.setVisibility(View.VISIBLE);
                binding.btnRegister.setEnabled(false);
                binding.tvError.setVisibility(View.GONE);
            } else {
                binding.loading.setVisibility(View.GONE);
                binding.btnRegister.setEnabled(true);
                if (state == AuthState.SUCCESS) {
                    if (getActivity() instanceof AuthActivity) {
                        ((AuthActivity) getActivity()).navigateToMain();
                    }
                }
            }
        });

        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                binding.tvError.setText(error);
                binding.tvError.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getCountdown().observe(getViewLifecycleOwner(), seconds -> {
            if (seconds > 0) {
                binding.btnSendCode.setEnabled(false);
                binding.btnSendCode.setText(seconds + "s");
            } else {
                binding.btnSendCode.setEnabled(true);
                binding.btnSendCode.setText("获取验证码");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
