package com.example.fjnuserviceapp;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
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
import com.example.fjnuserviceapp.ui.life.LifeActivity;
import com.example.fjnuserviceapp.ui.life.LifeFragment;
import com.example.fjnuserviceapp.ui.mine.MineActivity;
import com.example.fjnuserviceapp.ui.mine.MineFragment;
import com.example.fjnuserviceapp.ui.nav.NavFragment;
import com.example.fjnuserviceapp.ui.notify.NotifyActivity;
import com.example.fjnuserviceapp.ui.notify.NotifyFragment;
import com.example.fjnuserviceapp.ui.study.LearningActivity;
import com.example.fjnuserviceapp.ui.study.StudyFragment;
import com.example.fjnuserviceapp.ui.widget.WaterRippleView;
import com.example.fjnuserviceapp.utils.AnimationUtils;
import com.example.fjnuserviceapp.utils.BlurUtils;

// 新增权限请求码（常量）
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Light;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.Node;

import static android.Manifest.permission.POST_NOTIFICATIONS;

public class MainActivity extends AppCompatActivity {

    // 新增：通知权限请求码（自定义，只要是int即可）
    private static final int NOTIFICATION_PERMISSION_CODE = 1001;
    private ActivityMainBinding binding;
    private SceneView sceneView;
    private Node sphereNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 新增：Android 13+ 动态申请通知权限（核心修改）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 检查权限是否已授予
            if (ContextCompat.checkSelfPermission(this,
                    POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                // 未授予则申请权限
                ActivityCompat.requestPermissions(
                        this,
                        new String[] { POST_NOTIFICATIONS },
                        NOTIFICATION_PERMISSION_CODE);
            }
        }
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 初始化 3D 场景
        init3DScene();

        // 默认显示生活Fragment (学习模块已改为独立Activity跳转)
        // 核心修复：确保首页干净，移除可能残留的 Fragment
        if (savedInstanceState == null) {
            // 如果是全新启动，不加载任何Fragment
            androidx.fragment.app.Fragment fragment = getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        } else {
            // 如果是状态恢复（如旋转屏幕），需要检查是否有不该显示的 Fragment
            androidx.fragment.app.Fragment fragment = getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);
            if (fragment instanceof LifeFragment) {
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }

        // if (savedInstanceState == null) {
        // getSupportFragmentManager().beginTransaction()
        // .replace(R.id.fragment_container, new LifeFragment())
        // .commit();
        // binding.getRoot().post(() -> updateCenterStatus("生活"));
        // }

        // 圆形按钮点击事件
        binding.btnStudy.setOnClickListener(v -> {
            // 跳转到独立的学习页面
            Intent intent = new Intent(MainActivity.this, LearningActivity.class);
            startActivity(intent);

            // 播放点击反馈
            playPressFeedback(v);
            // updateCenterStatus("学习"); // 跳转后无需更新状态，或者保持现状
        });

        binding.btnLife.setOnClickListener(v -> {
            switchFragment(new LifeFragment());
            // ========== 新增：切回生活模块时显示所有悬浮按钮 ==========
            showAllFloatButtons();
            playPressFeedback(v);
            updateCenterStatus("生活");
        });

        binding.btnNav.setOnClickListener(v -> {
            switchFragment(new NavFragment());
            playPressFeedback(v);
            updateCenterStatus("导航");
        });

        binding.btnNotify.setOnClickListener(v -> {
            // 跳转到独立的通知页面
            Intent intent = new Intent(MainActivity.this, NotifyActivity.class);
            startActivity(intent);

            playPressFeedback(v);
            // updateCenterStatus("通知");
        });

        binding.btnMine.setOnClickListener(v -> {
            // 跳转到独立的个人中心页面
            Intent intent = new Intent(MainActivity.this, MineActivity.class);
            startActivity(intent);

            playPressFeedback(v);
            // updateCenterStatus("我的");
        });

        // 初始化液态玻璃按钮
        initGlassButtons();

        // 启动旋转动画
        startButtonAnimations();

        // 启动背景动画
        startBackgroundAnimation();

        // 初始化并显示日期/天气
        // initDateAndWeather(); // 已废弃，改用 Header

        // 启动中心卡片呼吸动画
        startCenterBreathing();

        // 启动天气图标微动动画
        // startWeatherFloating(); // 已废弃

        // 初始化长按菜单
        initLongPressMenu();

        // 初始化顶部 Header
        initHeader();

        // 演示：模拟通知模块有新消息时的脉冲提醒
        binding.btnNotify.postDelayed(() -> {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(binding.btnNotify, "scaleX", 1.0f, 1.2f, 1.0f);
            scaleX.setDuration(500);
            scaleX.setRepeatCount(3);

            ObjectAnimator scaleY = ObjectAnimator.ofFloat(binding.btnNotify, "scaleY", 1.0f, 1.2f, 1.0f);
            scaleY.setDuration(500);
            scaleY.setRepeatCount(3);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(scaleX, scaleY);
            set.start();
        }, 2000);
    }

    private void init3DScene() {
        sceneView = binding.sceneView;
        // 用户要求隐藏中间球体，直接隐藏视图并返回
        if (sceneView != null) {
            sceneView.setVisibility(View.GONE);
        }
        return;

        // 关键修复：设置 SceneView 透明背景，消除黑色方块
        /*
         * sceneView.setTransparent(true);
         * sceneView.setBackgroundColor(android.graphics.Color.TRANSPARENT);
         * 
         * // 创建五边形柱体 (Cylinder) 代替球体
         * Texture.builder()
         * .setSource(this, R.drawable.ic_school_logo)
         * .build()
         * .thenAccept(texture -> {
         * // ... (省略原有代码)
         * });
         */
    }

    private void startSphereRotation() {
        // 简单的自转动画
        ValueAnimator rotator = ValueAnimator.ofFloat(0, 360);
        rotator.setDuration(20000);
        rotator.setRepeatCount(ValueAnimator.INFINITE);
        rotator.setInterpolator(new android.view.animation.LinearInterpolator());
        rotator.addUpdateListener(animation -> {
            if (sphereNode != null) {
                float angle = (float) animation.getAnimatedValue();
                sphereNode.setLocalRotation(Quaternion.axisAngle(new Vector3(0.0f, 1.0f, 0.0f), angle));
            }
        });
        rotator.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sceneView != null) {
            try {
                sceneView.resume();
            } catch (Exception e) {
                // Ignore
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sceneView != null) {
            sceneView.pause();
        }
    }

    private void initHeader() {
        // 设置欢迎语
        // binding.tvWelcome.setText("欢迎回来，浦颖昊"); // 实际应从 UserInfo 获取
        // XML 已经设置了默认值，这里先注释掉，后续可从 UserProfile 读取

        // 头像点击跳转 (移除，因为没有头像控件了)
        // binding.ivAvatar.setOnClickListener(...)

        // 私信按钮跳转 (移除，入口改为通知模块内)
        // if (binding.btnChat != null) { ... }

        // 设置跑马灯选中状态（必须设置，否则跑马灯不滚动）
        binding.tvNotificationMarquee.setSelected(true);

        // 启动通知卡片浮动动画
        startNotificationFloating();
    }

    private void startNotificationFloating() {
        if (binding.cardNotification == null)
            return;

        // 上下漂浮动画 (TranslationY)
        ObjectAnimator floatAnim = ObjectAnimator.ofFloat(binding.cardNotification, "translationY", 0f, -10f, 0f);
        floatAnim.setDuration(3000);
        floatAnim.setRepeatCount(ValueAnimator.INFINITE);
        floatAnim.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());

        // 微旋转动画 (Rotation) - 模拟气流扰动
        ObjectAnimator rotateAnim = ObjectAnimator.ofFloat(binding.cardNotification, "rotation", -1f, 1f, -1f);
        rotateAnim.setDuration(4000);
        rotateAnim.setRepeatCount(ValueAnimator.INFINITE);
        rotateAnim.setInterpolator(new android.view.animation.LinearInterpolator());

        // 箭头呼吸动画 (Alpha)
        if (binding.ivNotificationArrow != null) {
            ObjectAnimator arrowAnim = ObjectAnimator.ofFloat(binding.ivNotificationArrow, "alpha", 0.4f, 1.0f, 0.4f);
            arrowAnim.setDuration(1500);
            arrowAnim.setRepeatCount(ValueAnimator.INFINITE);
            arrowAnim.start();
        }

        AnimatorSet set = new AnimatorSet();
        set.playTogether(floatAnim, rotateAnim);
        set.start();

        // 点击反馈
        binding.cardNotification.setOnClickListener(v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.CONTEXT_CLICK);
            // 简单缩放反馈
            v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100).start())
                    .start();
            // TODO: 跳转到通知详情
            android.widget.Toast.makeText(this, "查看今日提醒", android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    private void startCenterBreathing() {
        if (sphereNode != null) {
            // Sceneform nodes don't support Android View animations directly
            // We can implement scaling via ValueAnimator if needed
        }
    }

    private void startWeatherFloating() {
        // 已废弃，方法保留防止报错，或可用于 Logo 浮动
        if (binding.ivLogo != null) {
            AnimationUtils.createFloatingAnimation(binding.ivLogo, 5f).start();
        }
    }

    private void initLongPressMenu() {
        View.OnLongClickListener longClickListener = v -> {
            v.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS);
            // 这里可以弹出PopupMenu或BottomSheetDialog
            // 暂时简单演示：Toast提示
            String subFunc = "";
            if (v == binding.btnStudy)
                subFunc = "查看课表";
            else if (v == binding.btnLife)
                subFunc = "食堂菜单";
            else if (v == binding.btnNav)
                subFunc = "路线规划";
            else if (v == binding.btnNotify)
                subFunc = "系统通知";
            else if (v == binding.btnMine)
                subFunc = "个人设置";

            // updateCenterStatus(subFunc); // 移除旧逻辑
            android.widget.Toast.makeText(this, subFunc, android.widget.Toast.LENGTH_SHORT).show();
            return true;
        };

        binding.btnStudy.setOnLongClickListener(longClickListener);
        binding.btnLife.setOnLongClickListener(longClickListener);
        binding.btnNav.setOnLongClickListener(longClickListener);
        binding.btnNotify.setOnLongClickListener(longClickListener);
        binding.btnMine.setOnLongClickListener(longClickListener);
    }

    /**
     * 初始化日期和天气信息，并执行滑入动画
     * 已废弃，代码保留但不再调用
     */
    private void initDateAndWeather() {
        // if (binding.cardDateWeather == null)
        // return;
        // ...
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
    public void showAllFloatButtons() {
        binding.btnStudy.setVisibility(View.VISIBLE);
        binding.btnLife.setVisibility(View.VISIBLE);
        binding.btnNav.setVisibility(View.VISIBLE);
        binding.btnNotify.setVisibility(View.VISIBLE);
        binding.btnMine.setVisibility(View.VISIBLE);
    }

    // 旋转动画相关
    private ValueAnimator rotationAnimator;
    private boolean isRotating = true;
    private static final int ROTATION_DURATION = 20000; // 20秒一圈
    private static final int ORBIT_RADIUS_DP = 220; // 再次增大半径 (原180 -> 220)

    // 手势拖拽相关
    private float currentRotationOffset = 0f;
    private float touchLastX = 0f;
    private float flingVelocity = 0f;
    private ValueAnimator inertiaAnimator; // 惯性动画
    private float entryScale = 0f; // 入场动画缩放因子

    // 启动旋转动画
    private void startButtonAnimations() {
        if (rotationAnimator != null && rotationAnimator.isRunning()) {
            return;
        }

        // 入场动画：从中心散开
        ValueAnimator entryAnimator = ValueAnimator.ofFloat(0f, 1f);
        entryAnimator.setDuration(1200);
        entryAnimator.setInterpolator(new android.view.animation.OvershootInterpolator());
        entryAnimator.addUpdateListener(animation -> {
            entryScale = (float) animation.getAnimatedValue();
        });
        entryAnimator.start();

        final View[] buttons = {
                binding.btnStudy,
                binding.btnLife,
                binding.btnNav,
                binding.btnNotify,
                binding.btnMine
        };

        final com.example.fjnuserviceapp.ui.widget.StarView starView = binding.getRoot().findViewById(R.id.star_view);

        // 动态计算适配屏幕的半径
        android.util.DisplayMetrics metrics = new android.util.DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        // 按钮宽度约 85dp (约 230px)，留出 padding 20dp (约 55px)
        // 安全半径 = 屏幕一半 - 按钮一半 - 边距
        // 半径 X (水平) - 边距从 65dp 减少到 45dp，使五角星更大
        final int radiusPxX = (int) (screenWidth / 2f - com.example.fjnuserviceapp.utils.DisplayUtils.dp2px(this, 45));
        // 半径 Y (垂直) - 可以稍微大一点，或者保持一致
        final int radiusPxY = radiusPxX;

        final int count = buttons.length;
        final float angleStep = 360f / count;

        rotationAnimator = ValueAnimator.ofFloat(0, 360);
        rotationAnimator.setDuration(ROTATION_DURATION);
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setInterpolator(new android.view.animation.LinearInterpolator());

        // 3D 旋转参数 (Tilt)
        final double tiltX = Math.toRadians(60); // 恢复倾斜角度 (原70 -> 60)
        final double tiltZ = Math.toRadians(20); // 保持侧倾

        final java.util.List<Float> vertices = new java.util.ArrayList<>();
        for (int i = 0; i < count * 2; i++)
            vertices.add(0f);

        // 动画更新逻辑抽离，方便复用
        ValueAnimator.AnimatorUpdateListener updateListener = animation -> {
            float animatedValue = (float) animation.getAnimatedValue();

            // 叠加手动拖拽的偏移量
            float totalRotation = animatedValue + currentRotationOffset;

            // Parallax Effect (视差滚动)：背景粒子随旋转微动
            if (binding.particleView != null) {
                float parallaxX = (float) (Math.sin(Math.toRadians(totalRotation)) * 30);
                float parallaxY = (float) (Math.cos(Math.toRadians(totalRotation)) * 20);
                binding.particleView.setTranslationX(parallaxX);
                binding.particleView.setTranslationY(parallaxY);
            }

            for (int i = 0; i < count; i++) {
                // 1. 初始平面圆周运动 (XY平面)
                float currentAngle = i * angleStep + totalRotation;
                double radians = Math.toRadians(currentAngle - 90);

                double x0 = radiusPxX * Math.cos(radians);
                double y0 = radiusPxY * Math.sin(radians);
                double z0 = 0;

                // 2. 3D 旋转变换
                double y1 = y0 * Math.cos(tiltX) - z0 * Math.sin(tiltX);
                double z1 = y0 * Math.sin(tiltX) + z0 * Math.cos(tiltX);
                double x1 = x0;

                double x2 = x1 * Math.cos(tiltZ) - y1 * Math.sin(tiltZ);
                double y2 = x1 * Math.sin(tiltZ) + y1 * Math.cos(tiltZ);
                double z2 = z1;

                // 3. 投影与深度
                double zNormalized = z2 / (radiusPxY * Math.sin(tiltX)); // 使用 Y 半径归一化

                float scale = (float) (0.9 + (zNormalized * 0.2));
                float alpha = (float) (0.7 + (zNormalized + 1) * 0.15);
                float elevation = (float) (10 + (zNormalized * 10));

                // 应用入场动画缩放 (Entry Scale)
                float finalScale = scale * entryScale;
                double x2_scaled = x2 * entryScale;
                double y2_scaled = y2 * entryScale;

                buttons[i].setTranslationX((float) x2_scaled);
                buttons[i].setTranslationY((float) y2_scaled);
                buttons[i].setScaleX(finalScale);
                buttons[i].setScaleY(finalScale);
                buttons[i].setAlpha(alpha * (0.5f + 0.5f * entryScale)); // Alpha 也随入场渐变
                buttons[i].setElevation(elevation);

                vertices.set(i * 2, (float) x2_scaled);
                vertices.set(i * 2 + 1, (float) y2_scaled);
            }

            if (starView != null) {
                starView.setVertices(vertices);
            }
        };

        rotationAnimator.addUpdateListener(updateListener);
        rotationAnimator.start();

        // 全局手势监听 (在 ConstraintLayout 上)
        View container = (View) binding.centerPoint.getParent();
        container.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case android.view.MotionEvent.ACTION_DOWN:
                    touchLastX = event.getX();
                    if (rotationAnimator != null)
                        rotationAnimator.pause();
                    if (inertiaAnimator != null)
                        inertiaAnimator.cancel();
                    return true;

                case android.view.MotionEvent.ACTION_MOVE:
                    float deltaX = event.getX() - touchLastX;
                    currentRotationOffset += deltaX * 0.5f; // 灵敏度系数
                    touchLastX = event.getX();

                    // 手动触发一帧更新
                    if (rotationAnimator != null) {
                        updateListener.onAnimationUpdate(rotationAnimator);
                    }
                    return true;

                case android.view.MotionEvent.ACTION_UP:
                case android.view.MotionEvent.ACTION_CANCEL:
                    // 计算简单的惯性 (这里简化处理，直接恢复自动旋转)
                    // 实际可以计算 fling velocity 然后做减速动画
                    if (rotationAnimator != null && isRotating)
                        rotationAnimator.resume();
                    return true;
            }
            return false;
        });

        // 按钮点击监听保持不变 (处理点击反馈)
        View.OnClickListener clickListener = v -> {
            // 模拟点击事件分发
            if (v == binding.btnStudy) {
                Intent intent = new Intent(MainActivity.this, LearningActivity.class);
                startActivity(intent);
            } else if (v == binding.btnLife) {
                Intent intent = new Intent(MainActivity.this, LifeActivity.class);
                startActivity(intent);
                // switchFragment(new LifeFragment());
                // updateCenterStatus("生活");
            } else if (v == binding.btnNav) {
                switchFragment(new NavFragment());
                updateCenterStatus("导航");
            } else if (v == binding.btnNotify) {
                Intent intent = new Intent(MainActivity.this, NotifyActivity.class);
                startActivity(intent);
            } else if (v == binding.btnMine) {
                Intent intent = new Intent(MainActivity.this, MineActivity.class);
                startActivity(intent);
            }
            playPressFeedback(v);
        };

        for (View btn : buttons) {
            // 覆盖之前的 OnTouchListener，改为 OnClickListener
            // 因为父布局处理了滑动，这里只需要处理点击
            // 注意：如果父布局拦截了 MOVE，按钮可能收不到 UP，所以需要处理好冲突
            // 简单起见，这里按钮只响应点击，滑动由空白区域触发
            btn.setOnClickListener(clickListener);

            // 为按钮添加长按提示
            btn.setOnLongClickListener(v -> {
                v.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS);
                android.widget.Toast.makeText(this, "长按了功能模块", android.widget.Toast.LENGTH_SHORT).show();
                return true;
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rotationAnimator != null) {
            rotationAnimator.cancel();
        }
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
                getResources().getColor(R.color.gradient_end));
        colorAnimator.setDuration(10000);
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator.start();
    }

    /**
     * 切换Fragment
     */
    /**
     * 隐藏主页面元素（顶部Header、装饰圆形、粒子效果等）
     */
    private void hideMainPageElements() {
        binding.topBar.setVisibility(View.GONE);
        binding.decorCircleTopLeft.setVisibility(View.GONE);
        binding.particleView.setVisibility(View.GONE);
        binding.starView.setVisibility(View.GONE);
        binding.sceneView.setVisibility(View.GONE);
        binding.buttonsContainer.setVisibility(View.GONE);
    }

    /**
     * 显示主页面元素（顶部Header、装饰圆形、粒子效果等）
     */
    public void showMainPageElements() {
        binding.topBar.setVisibility(View.VISIBLE);
        binding.decorCircleTopLeft.setVisibility(View.VISIBLE);
        binding.particleView.setVisibility(View.VISIBLE);
        binding.starView.setVisibility(View.VISIBLE);
        binding.sceneView.setVisibility(View.GONE); // 保持隐藏，因为用户要求隐藏中间球体
        binding.buttonsContainer.setVisibility(View.VISIBLE);
    }

    /**
     * 切换Fragment
     */
    public void switchFragment(androidx.fragment.app.Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .commit();

        // 根据Fragment类型决定是否隐藏主页面元素
        if (fragment instanceof NavFragment) {
            hideMainPageElements();
            hideAllFloatButtons();
        } else {
            showMainPageElements();
            showAllFloatButtons();
        }
    }

    private void playPressFeedback(View v) {
        v.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP);
        // 脉冲动画 1.0 -> 1.1 -> 1.0
        AnimationUtils.createPulseAnimation(v).start();
    }

    private void updateCenterStatus(String text) {
        // Center status text has been replaced by Logo image
        // if (binding.tvCenterStatus == null) return;

        // binding.tvCenterStatus.animate()
        // .alpha(0f)
        // .setDuration(150)
        // .withEndAction(() -> {
        // binding.tvCenterStatus.setText(text);
        // binding.tvCenterStatus.animate()
        // .alpha(1f)
        // .setDuration(150)
        // .start();
        // })
        // .start();
    }
}
