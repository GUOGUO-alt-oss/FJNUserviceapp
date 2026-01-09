package com.example.fjnuserviceapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.fjnuserviceapp.databinding.ActivityMainBinding;
import com.example.fjnuserviceapp.ui.life.LifeFragment;
import com.example.fjnuserviceapp.ui.mine.MineFragment;
import com.example.fjnuserviceapp.ui.nav.NavFragment;
import com.example.fjnuserviceapp.ui.notify.NotifyFragment;
import com.example.fjnuserviceapp.ui.study.StudyFragment;

import static android.Manifest.permission.POST_NOTIFICATIONS;

public class MainActivity extends AppCompatActivity {

    private static final int NOTIFICATION_PERMISSION_CODE = 1001;
    private ActivityMainBinding binding;

    /* ---------- 底部按钮显隐 ---------- */
    private void showAllFloatButtons() {
        binding.btnStudy.setVisibility(View.VISIBLE);
        binding.btnLife.setVisibility(View.VISIBLE);
        binding.btnNav.setVisibility(View.VISIBLE);
        binding.btnNotify.setVisibility(View.VISIBLE);
        binding.btnMine.setVisibility(View.VISIBLE);
    }

    private void hideAllFloatButtons() {
        binding.btnStudy.setVisibility(View.GONE);
        binding.btnLife.setVisibility(View.GONE);
        binding.btnNav.setVisibility(View.GONE);
        binding.btnNotify.setVisibility(View.GONE);
        binding.btnMine.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE);
            }
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new StudyFragment())
                    .commit();
        }

        binding.btnStudy.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new StudyFragment())
                    .commit();
            showAllFloatButtons();
        });

        binding.btnLife.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LifeFragment())
                    .commit();
            hideAllFloatButtons();   // ← 生活页也隐藏
        });

        binding.btnNav.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NavFragment())
                    .commit();
            showAllFloatButtons();
        });

        binding.btnNotify.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NotifyFragment())
                    .commit();
            hideAllFloatButtons();   // ← 通知页保持隐藏（原逻辑）
        });

        binding.btnMine.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MineFragment())
                    .commit();
            showAllFloatButtons();
        });

        startButtonAnimations();
    }

    /* 以下代码与原文件完全一致 */
    private void startButtonAnimations() {
        binding.btnStudy.setTranslationY(500);
        binding.btnLife.setTranslationY(500);
        binding.btnNav.setTranslationY(500);
        binding.btnNotify.setTranslationY(500);
        binding.btnMine.setTranslationY(500);
        binding.btnStudy.setAlpha(0f);
        binding.btnLife.setAlpha(0f);
        binding.btnNav.setAlpha(0f);
        binding.btnNotify.setAlpha(0f);
        binding.btnMine.setAlpha(0f);

        long duration = 1000;
        long delay = 150;

        AnimatorSet studyAnimator = createButtonAnimator(binding.btnStudy, 0, duration);
        studyAnimator.start();

        AnimatorSet lifeAnimator = createButtonAnimator(binding.btnLife, 72, duration);
        lifeAnimator.setStartDelay(delay);
        lifeAnimator.start();

        AnimatorSet navAnimator = createButtonAnimator(binding.btnNav, 144, duration);
        navAnimator.setStartDelay(delay * 2);
        navAnimator.start();

        AnimatorSet notifyAnimator = createButtonAnimator(binding.btnNotify, 216, duration);
        notifyAnimator.setStartDelay(delay * 3);
        notifyAnimator.start();

        AnimatorSet mineAnimator = createButtonAnimator(binding.btnMine, 288, duration);
        mineAnimator.setStartDelay(delay * 4);
        mineAnimator.start();
    }

    private AnimatorSet createButtonAnimator(View button, int angle, long duration) {
        AnimatorSet set = new AnimatorSet();
        float radius = 80f;
        float targetX = (float) Math.sin(Math.toRadians(angle)) * radius;
        float targetY = (float) -Math.cos(Math.toRadians(angle)) * radius;

        ObjectAnimator transX = ObjectAnimator.ofFloat(button, "translationX", 0f, targetX);
        ObjectAnimator transY = ObjectAnimator.ofFloat(button, "translationY", 500f, targetY);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f);

        transX.setDuration(duration);
        transY.setDuration(duration);
        alpha.setDuration(duration);
        transX.setInterpolator(new AccelerateDecelerateInterpolator());
        transY.setInterpolator(new AccelerateDecelerateInterpolator());
        alpha.setInterpolator(new AccelerateDecelerateInterpolator());

        set.playTogether(transX, transY, alpha);
        return set;
    }
}