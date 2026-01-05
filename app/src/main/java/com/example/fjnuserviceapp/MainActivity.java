package com.example.fjnuserviceapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.ActivityMainBinding;
import com.example.fjnuserviceapp.ui.life.LifeFragment;
import com.example.fjnuserviceapp.ui.mine.MineFragment;
import com.example.fjnuserviceapp.ui.nav.NavFragment;
import com.example.fjnuserviceapp.ui.notify.NotifyFragment;
import com.example.fjnuserviceapp.ui.study.StudyFragment;

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

        // 圆形按钮点击事件
        binding.btnStudy.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new StudyFragment())
                    .commit();
        });

        binding.btnLife.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new LifeFragment())
                    .commit();
        });

        binding.btnNav.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NavFragment())
                    .commit();
        });

        binding.btnNotify.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new NotifyFragment())
                    .commit();
        });

        binding.btnMine.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MineFragment())
                    .commit();
        });

        // 启动按钮动画
        startButtonAnimations();
    }

    /**
     * 启动按钮动画，从底部中间一个一个以圆弧路径上来
     */
    private void startButtonAnimations() {
        // 设置初始位置：所有按钮都在底部中间
        binding.btnStudy.setTranslationY(500);
        binding.btnLife.setTranslationY(500);
        binding.btnNav.setTranslationY(500);
        binding.btnNotify.setTranslationY(500);
        binding.btnMine.setTranslationY(500);

        // 设置初始透明度
        binding.btnStudy.setAlpha(0f);
        binding.btnLife.setAlpha(0f);
        binding.btnNav.setAlpha(0f);
        binding.btnNotify.setAlpha(0f);
        binding.btnMine.setAlpha(0f);

        // 定义动画时长
        long duration = 1000;
        // 定义动画延迟间隔
        long delay = 150;

        // 学习按钮动画
        AnimatorSet studyAnimator = createButtonAnimator(binding.btnStudy, 0, duration);
        studyAnimator.start();

        // 生活按钮动画（延迟150ms）
        AnimatorSet lifeAnimator = createButtonAnimator(binding.btnLife, 72, duration);
        lifeAnimator.setStartDelay(delay);
        lifeAnimator.start();

        // 导航按钮动画（延迟300ms）
        AnimatorSet navAnimator = createButtonAnimator(binding.btnNav, 144, duration);
        navAnimator.setStartDelay(delay * 2);
        navAnimator.start();

        // 通知按钮动画（延迟450ms）
        AnimatorSet notifyAnimator = createButtonAnimator(binding.btnNotify, 216, duration);
        notifyAnimator.setStartDelay(delay * 3);
        notifyAnimator.start();

        // 我的按钮动画（延迟600ms）
        AnimatorSet mineAnimator = createButtonAnimator(binding.btnMine, 288, duration);
        mineAnimator.setStartDelay(delay * 4);
        mineAnimator.start();
    }

    /**
     * 创建按钮的圆弧路径动画
     * @param button 按钮
     * @param angle 最终角度
     * @param duration 动画时长
     * @return AnimatorSet
     */
    private AnimatorSet createButtonAnimator(View button, int angle, long duration) {
        // 创建动画集合
        AnimatorSet animatorSet = new AnimatorSet();

        // 计算目标位置（基于圆心和半径）
        float radius = 80f;
        float centerX = button.getWidth() / 2f;
        float centerY = button.getHeight() / 2f;
        float targetX = (float) Math.sin(Math.toRadians(angle)) * radius;
        float targetY = (float) -Math.cos(Math.toRadians(angle)) * radius;

        // 平移X动画
        ObjectAnimator translationX = ObjectAnimator.ofFloat(button, "translationX", 0f, targetX);
        translationX.setDuration(duration);
        translationX.setInterpolator(new AccelerateDecelerateInterpolator());

        // 平移Y动画
        ObjectAnimator translationY = ObjectAnimator.ofFloat(button, "translationY", 500f, targetY);
        translationY.setDuration(duration);
        translationY.setInterpolator(new AccelerateDecelerateInterpolator());

        // 透明度动画
        ObjectAnimator alpha = ObjectAnimator.ofFloat(button, "alpha", 0f, 1f);
        alpha.setDuration(duration);
        alpha.setInterpolator(new AccelerateDecelerateInterpolator());

        // 组合动画
        animatorSet.playTogether(translationX, translationY, alpha);
        return animatorSet;
    }
}