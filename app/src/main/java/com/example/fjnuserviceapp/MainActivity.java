package com.example.fjnuserviceapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.ActivityMainBinding;
import com.example.fjnuserviceapp.ui.life.LifeFragment;
import com.example.fjnuserviceapp.ui.mine.MineFragment;
import com.example.fjnuserviceapp.ui.nav.NavFragment;
import com.example.fjnuserviceapp.ui.notify.NotifyFragment;
import com.example.fjnuserviceapp.ui.study.StudyFragment;
import com.example.fjnuserviceapp.ui.widget.WaterRippleView;
import com.example.fjnuserviceapp.utils.AnimationUtils;
import com.example.fjnuserviceapp.utils.BlurUtils;

// 新增权限请求码（常量）
import static android.Manifest.permission.POST_NOTIFICATIONS;

public class MainActivity extends AppCompatActivity {

    // 新增：通知权限请求码（自定义，只要是int即可）
    private static final int NOTIFICATION_PERMISSION_CODE = 1001;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 新增：Android 13+ 动态申请通知权限（核心修改）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 检查权限是否已授予
            if (ContextCompat.checkSelfPermission(this, POST_NOTIFICATIONS)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // 未授予则申请权限
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{POST_NOTIFICATIONS},
                        NOTIFICATION_PERMISSION_CODE
                );
            }
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 默认显示学习Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new StudyFragment())
                    .commit();
        }

        // 圆形按钮点击事件
        binding.btnStudy.setOnClickListener(v -> {
            switchFragment(new StudyFragment());
            // ========== 新增：切回学习模块时显示所有悬浮按钮 ==========
            showAllFloatButtons();
        });

        binding.btnLife.setOnClickListener(v -> {
            switchFragment(new LifeFragment());
            // ========== 新增：切回生活模块时显示所有悬浮按钮 ==========
            showAllFloatButtons();
        });

        binding.btnNav.setOnClickListener(v -> {
            switchFragment(new NavFragment());
            // ========== 新增：切回导航模块时显示所有悬浮按钮 ==========
            showAllFloatButtons();
        });

        binding.btnNotify.setOnClickListener(v -> {
            switchFragment(new NotifyFragment());
            // ========== 新增：进入通知模块时隐藏所有悬浮按钮 ==========
            hideAllFloatButtons();
        });

        binding.btnMine.setOnClickListener(v -> {
            switchFragment(new MineFragment());
            // ========== 新增：切回我的模块时显示所有悬浮按钮 ==========
            showAllFloatButtons();
        });

        // 初始化液态玻璃按钮
        initGlassButtons();

        // 启动按钮动画
        startButtonAnimations();

        // 启动背景动画
        startBackgroundAnimation();
    }

    /**
     * 初始化液态玻璃按钮
     */
    private void initGlassButtons() {
        View[] buttons = {
            binding.btnStudy,
            binding.btnLife,
            binding.btnNav,
            binding.btnNotify,
            binding.btnMine
        };

        for (View button : buttons) {
            // 设置液态玻璃背景
            button.setBackgroundResource(R.drawable.glass_morphism_background);
            // 应用模糊效果
            BlurUtils.applyBlur(this, button, 10);
            // 添加阴影效果
            button.setElevation(12f);
            button.setTranslationZ(8f);
        }
    }

    /**
     * ========== 新增方法：隐藏所有底部悬浮按钮 ==========
     * 进入通知模块时调用，让通知页面无遮挡
     */
    private void hideAllFloatButtons() {
        binding.btnStudy.setVisibility(View.GONE);
        binding.btnLife.setVisibility(View.GONE);
        binding.btnNav.setVisibility(View.GONE);
        binding.btnNotify.setVisibility(View.GONE);
        binding.btnMine.setVisibility(View.GONE);
    }

    /**
     * ========== 新增方法：显示所有底部悬浮按钮 ==========
     * 切回其他模块时调用，恢复按钮显示
     */
    private void showAllFloatButtons() {
        binding.btnStudy.setVisibility(View.VISIBLE);
        binding.btnLife.setVisibility(View.VISIBLE);
        binding.btnNav.setVisibility(View.VISIBLE);
        binding.btnNotify.setVisibility(View.VISIBLE);
        binding.btnMine.setVisibility(View.VISIBLE);
    }

    /**
     * 启动按钮动画
     */
    private void startButtonAnimations() {
        // 获取屏幕尺寸
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float screenHeight = metrics.heightPixels;

        // 动画总时长
        long duration = 1500;
        // 按钮动画延迟间隔
        long delayInterval = 100;

        // 按钮起始位置（屏幕底部外）
        float startY = screenHeight + 100;

        // 重置按钮的初始状态，确保它们在动画开始前是可见的
        resetButtonState(binding.btnStudy);
        resetButtonState(binding.btnLife);
        resetButtonState(binding.btnNav);
        resetButtonState(binding.btnNotify);
        resetButtonState(binding.btnMine);

        // 创建每个按钮的动画
        AnimatorSet studyAnim = AnimationUtils.createRiseFromBottomAnimation(
            binding.btnStudy, startY, duration, 0
        );

        AnimatorSet lifeAnim = AnimationUtils.createRiseFromBottomAnimation(
            binding.btnLife, startY, duration, delayInterval
        );

        AnimatorSet navAnim = AnimationUtils.createRiseFromBottomAnimation(
            binding.btnNav, startY, duration, delayInterval * 2
        );

        AnimatorSet notifyAnim = AnimationUtils.createRiseFromBottomAnimation(
            binding.btnNotify, startY, duration, delayInterval * 3
        );

        AnimatorSet mineAnim = AnimationUtils.createRiseFromBottomAnimation(
            binding.btnMine, startY, duration, delayInterval * 4
        );

        // 启动所有动画
        studyAnim.start();
        lifeAnim.start();
        navAnim.start();
        notifyAnim.start();
        mineAnim.start();
    }

    /**
     * 重置按钮的初始状态
     */
    private void resetButtonState(View button) {
        button.setAlpha(1f);
        button.setScaleX(1f);
        button.setScaleY(1f);
        button.setTranslationX(0f);
        button.setTranslationY(0f);
    }

    /**
     * 启动背景动画
     */
    private void startBackgroundAnimation() {
        // 启动背景颜色渐变动画
        ObjectAnimator colorAnimator = ObjectAnimator.ofObject(
            binding.backgroundView,
            "backgroundColor",
            new android.animation.ArgbEvaluator(),
            getResources().getColor(R.color.gradient_start),
            getResources().getColor(R.color.gradient_mid),
            getResources().getColor(R.color.gradient_end)
        );
        colorAnimator.setDuration(10000);
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator.start();
    }

    /**
     * 切换Fragment
     */
    private void switchFragment(androidx.fragment.app.Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}