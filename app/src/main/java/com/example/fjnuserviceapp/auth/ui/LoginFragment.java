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
import com.example.fjnuserviceapp.auth.viewmodel.LoginViewModel;
import com.example.fjnuserviceapp.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        initView();
        observeViewModel();
    }

    private void initView() {
        binding.ivBack.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).goBack();
            }
        });

        binding.btnLogin.setOnClickListener(v -> {
            viewModel.account.setValue(binding.etAccount.getText().toString());
            viewModel.password.setValue(binding.etPassword.getText().toString());
            viewModel.login();
        });

        binding.tvForgotPwd.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).replaceFragment(new FindPwdFragment());
            }
        });
        
        binding.switchRemember.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.rememberPassword.setValue(isChecked);
        });

        // Initialize edit texts from view model if saved
        if (viewModel.account.getValue() != null) {
            binding.etAccount.setText(viewModel.account.getValue());
        }
        if (viewModel.password.getValue() != null) {
            binding.etPassword.setText(viewModel.password.getValue());
        }
        if (viewModel.rememberPassword.getValue() != null) {
            binding.switchRemember.setChecked(viewModel.rememberPassword.getValue());
        }
    }

    private void observeViewModel() {
        viewModel.getLoginState().observe(getViewLifecycleOwner(), state -> {
            if (state == AuthState.LOADING) {
                binding.loading.setVisibility(View.VISIBLE);
                binding.btnLogin.setEnabled(false);
                binding.tvError.setVisibility(View.GONE);
            } else {
                binding.loading.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true);
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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
