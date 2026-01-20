package com.example.fjnuserviceapp.auth.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.FragmentWelcomeBinding;

public class WelcomeFragment extends Fragment {

    private FragmentWelcomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        startAnimations();

        binding.btnLoginPhone.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).replaceFragment(new LoginFragment());
            }
        });

        binding.btnLoginStudent.setOnClickListener(v -> {
            // Can share LoginFragment but pass type or show specific logic
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).replaceFragment(new LoginFragment());
            }
        });

        binding.tvRegister.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).replaceFragment(new RegisterFragment());
            }
        });

        binding.tvForgotPwd.setOnClickListener(v -> {
            if (getActivity() instanceof AuthActivity) {
                ((AuthActivity) getActivity()).replaceFragment(new FindPwdFragment());
            }
        });
    }

    private void startAnimations() {
        // 1. 黄色（顺时针）
        startRotateAnimation(binding.hunhuan1, true);
        // 2. 黄色（逆时针）
        startRotateAnimation(binding.hunhuan2, false);
        // 3. 紫色（顺时针）
        startRotateAnimation(binding.hunhuan3, true);
        // 4. 紫色（逆时针）
        startRotateAnimation(binding.hunhuan4, false);
        // 5. 黑色（顺时针）
        startRotateAnimation(binding.hunhuan5, true);
        // 6. 黑色（逆时针）
        startRotateAnimation(binding.hunhuan6, false);
        // 7. 黑色（顺时针）
        startRotateAnimation(binding.hunhuan7, true);
        // 8. 黑色（逆时针）
        startRotateAnimation(binding.hunhuan8, false);
        // 9. 红色（顺时针）
        startRotateAnimation(binding.hunhuan9, true);
    }

    private void startRotateAnimation(View view, boolean isClockwise) {
        if (view == null) return;
        int animResId = isClockwise ? R.anim.hunhuan_rotate_clockwise : R.anim.hunhuan_rotate_counterclockwise;
        Animation animation = AnimationUtils.loadAnimation(getContext(), animResId);
        view.startAnimation(animation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
