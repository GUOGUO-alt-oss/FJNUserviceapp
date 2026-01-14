package com.example.fjnuserviceapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

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

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 默认显示学习Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new StudyFragment())
                    .commit();
        }

        // 设置按钮点击事件
        binding.btnStudy.setOnClickListener(v -> switchFragment(new StudyFragment()));
        binding.btnLife.setOnClickListener(v -> switchFragment(new LifeFragment()));
        binding.btnNav.setOnClickListener(v -> switchFragment(new NavFragment()));
        binding.btnNotify.setOnClickListener(v -> switchFragment(new NotifyFragment()));
        binding.btnMine.setOnClickListener(v -> switchFragment(new MineFragment()));

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