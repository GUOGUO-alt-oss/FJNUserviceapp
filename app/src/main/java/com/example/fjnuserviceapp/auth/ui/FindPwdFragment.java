package com.example.fjnuserviceapp.auth.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fjnuserviceapp.auth.viewmodel.AuthState;
import com.example.fjnuserviceapp.auth.viewmodel.FindPwdViewModel;
import com.example.fjnuserviceapp.databinding.FragmentFindPwdBinding;

public class FindPwdFragment extends Fragment {

    private FragmentFindPwdBinding binding;
    private FindPwdViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFindPwdBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FindPwdViewModel.class);

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

        binding.btnAction.setOnClickListener(v -> {
            Integer step = viewModel.currentStep.getValue();
            if (step != null) {
                if (step == 1 || step == 2) { // Logic merged for simplicity in UI, verify code is step 2
                    // Actually ViewModel logic: step 1 is input phone, send code. step 2 is input code, verify. step 3 is reset.
                    // But UI has fields together.
                    // Let's assume user inputs phone and code, then clicks Next.
                    viewModel.phone.setValue(binding.etPhone.getText().toString());
                    viewModel.code.setValue(binding.etCode.getText().toString());
                    viewModel.verifyCode();
                } else if (step == 3) {
                    viewModel.newPassword.setValue(binding.etNewPassword.getText().toString());
                    viewModel.confirmPassword.setValue(binding.etConfirmPassword.getText().toString());
                    viewModel.resetPassword();
                }
            }
        });
    }

    private void observeViewModel() {
        viewModel.getState().observe(getViewLifecycleOwner(), state -> {
            if (state == AuthState.LOADING) {
                binding.loading.setVisibility(View.VISIBLE);
                binding.btnAction.setEnabled(false);
                binding.tvError.setVisibility(View.GONE);
            } else {
                binding.loading.setVisibility(View.GONE);
                binding.btnAction.setEnabled(true);
                if (state == AuthState.SUCCESS) {
                    Toast.makeText(getContext(), "密码重置成功，请登录", Toast.LENGTH_SHORT).show();
                    if (getActivity() instanceof AuthActivity) {
                        ((AuthActivity) getActivity()).replaceFragment(new LoginFragment());
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
        
        viewModel.currentStep.observe(getViewLifecycleOwner(), step -> {
            if (step == 3) {
                // Show password fields
                binding.etPhone.setVisibility(View.GONE);
                binding.layoutCode.setVisibility(View.GONE);
                binding.etNewPassword.setVisibility(View.VISIBLE);
                binding.etConfirmPassword.setVisibility(View.VISIBLE);
                binding.btnAction.setText("完成");
            } else {
                binding.etPhone.setVisibility(View.VISIBLE);
                binding.layoutCode.setVisibility(View.VISIBLE);
                binding.etNewPassword.setVisibility(View.GONE);
                binding.etConfirmPassword.setVisibility(View.GONE);
                binding.btnAction.setText("下一步");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
