package com.example.fjnuserviceapp.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 动画工具类，实现按钮的丝滑加载动画
 */
public class AnimationUtils {
    /**
     * 创建按钮从底部上来，绕圈后回到目标位置的动画
     * @param button 目标按钮
     * @param startY 起始Y坐标（屏幕底部）
     * @param centerX 圆心X坐标
     * @param centerY 圆心Y坐标
     * @param radius 圆半径
     * @param startAngle 起始角度
     * @param endAngle 结束角度
     * @param duration 动画总时长
     * @param delay 延迟时间
     * @return 动画集合
     */
    public static AnimatorSet createButtonAnimation(
        final View button,
        float startY,
        float centerX,
        float centerY,
        float radius,
        float startAngle,
        float endAngle,
        long duration,
        long delay) {

        AnimatorSet animatorSet = new AnimatorSet();

        // 保存按钮的初始位置
        final float originalX = button.getTranslationX();
        final float originalY = button.getTranslationY();

        // 计算按钮中心位置
        final float buttonCenterX = centerX - button.getWidth() / 2;
        final float buttonCenterY = centerY - button.getHeight() / 2;

        // 1. 从底部上来的动画
        ObjectAnimator translateUpY = ObjectAnimator.ofFloat(
            button, "translationY", startY, buttonCenterY
        );
        ObjectAnimator translateUpX = ObjectAnimator.ofFloat(
            button, "translationX", buttonCenterX, buttonCenterX
        );

        // 2. 绕圈动画
        final float[] animatedValue = new float[1];
        ValueAnimator circleAnimator = ValueAnimator.ofFloat(0, 1);
        circleAnimator.addUpdateListener(animation -> {
            animatedValue[0] = (float) animation.getAnimatedValue();
            float angle = startAngle + (endAngle - startAngle) * animatedValue[0];
            float x = buttonCenterX + radius * (float) Math.cos(Math.toRadians(angle));
            float y = buttonCenterY + radius * (float) Math.sin(Math.toRadians(angle));
            button.setTranslationX(x);
            button.setTranslationY(y);
        });

        // 3. 回到目标位置的动画
        ObjectAnimator finalPositionX = ObjectAnimator.ofFloat(
            button, "translationX", originalX
        );
        ObjectAnimator finalPositionY = ObjectAnimator.ofFloat(
            button, "translationY", originalY
        );

        // 4. 透明度动画
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(
            button, "alpha", 0, 1
        );

        // 5. 缩放动画，增加视觉效果
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(
            button, "scaleX", 0.8f, 1.1f, 1.0f
        );
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(
            button, "scaleY", 0.8f, 1.1f, 1.0f
        );

        // 设置动画时长和插值器
        long upDuration = duration / 4;
        long circleDuration = duration / 2;
        long finalDuration = duration / 4;

        translateUpX.setDuration(upDuration);
        translateUpY.setDuration(upDuration);
        circleAnimator.setDuration(circleDuration);
        finalPositionX.setDuration(finalDuration);
        finalPositionY.setDuration(finalDuration);
        alphaAnimator.setDuration(duration);
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);

        // 使用非线性插值器，使动画更丝滑
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        translateUpX.setInterpolator(interpolator);
        translateUpY.setInterpolator(interpolator);
        circleAnimator.setInterpolator(interpolator);
        finalPositionX.setInterpolator(interpolator);
        finalPositionY.setInterpolator(interpolator);
        alphaAnimator.setInterpolator(interpolator);
        scaleX.setInterpolator(interpolator);
        scaleY.setInterpolator(interpolator);

        // 组合动画
        animatorSet.play(translateUpX).with(translateUpY).with(alphaAnimator).with(scaleX).with(scaleY);
        animatorSet.play(circleAnimator).after(translateUpX);
        animatorSet.play(finalPositionX).with(finalPositionY).after(circleAnimator);
        animatorSet.setStartDelay(delay);

        return animatorSet;
    }

    /**
     * 创建按钮沿斐波那契螺旋线路径移动的动画
     * @param button 目标按钮
     * @param startX 起始X坐标
     * @param startY 起始Y坐标
     * @param targetX 目标X坐标
     * @param targetY 目标Y坐标
     * @param a 螺旋线参数a
     * @param startAngle 起始角度（弧度）
     * @param endAngle 结束角度（弧度）
     * @param duration 动画总时长
     * @param delay 延迟时间
     * @return 动画集合
     */
    public static AnimatorSet createFibonacciSpiralAnimation(
        final View button,
        float startX,
        float startY,
        final float targetX,
        final float targetY,
        final float a,
        final double startAngle,
        final double endAngle,
        long duration,
        long delay) {

        AnimatorSet animatorSet = new AnimatorSet();

        // 确保按钮在动画开始时是不可见的
        button.setAlpha(0f);
        button.setScaleX(0.5f);
        button.setScaleY(0.5f);

        // 透明度动画（0→1）
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(
            button, "alpha", 0, 1
        );

        // 缩放动画（0.5→1.0）
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(
            button, "scaleX", 0.5f, 1.0f
        );
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(
            button, "scaleY", 0.5f, 1.0f
        );

        // 斐波那契螺旋线动画
        ValueAnimator spiralAnimator = ValueAnimator.ofFloat(0, 1);
        spiralAnimator.addUpdateListener(animation -> {
            float progress = (float) animation.getAnimatedValue();
            
            // 计算当前角度（从startAngle到endAngle）
            double angle = startAngle + (endAngle - startAngle) * progress;
            
            // 计算斐波那契螺旋线半径
            double phi = (1 + Math.sqrt(5)) / 2; // 黄金比例
            double radius = a * Math.pow(phi, 2 * angle / Math.PI);
            
            // 计算当前位置
            float currentX = (float) (startX + radius * Math.cos(angle));
            float currentY = (float) (startY + radius * Math.sin(angle));
            
            // 平滑过渡到目标位置
            float finalX = currentX + (targetX - currentX) * progress;
            float finalY = currentY + (targetY - currentY) * progress;
            
            button.setTranslationX(finalX);
            button.setTranslationY(finalY);
        });

        // 设置动画时长和插值器
        spiralAnimator.setDuration(duration);
        alphaAnimator.setDuration(duration);
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);

        // 使用AccelerateDecelerateInterpolator实现平滑过渡
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        spiralAnimator.setInterpolator(interpolator);
        alphaAnimator.setInterpolator(interpolator);
        scaleX.setInterpolator(interpolator);
        scaleY.setInterpolator(interpolator);

        // 组合动画
        animatorSet.play(spiralAnimator).with(alphaAnimator).with(scaleX).with(scaleY);
        animatorSet.setStartDelay(delay);

        return animatorSet;
    }

    /**
     * 创建按钮从底部升起的动画
     * @param button 目标按钮
     * @param startY 起始Y坐标（屏幕底部外）
     * @param duration 动画总时长
     * @param delay 延迟时间
     * @return 动画集合
     */
    public static AnimatorSet createRiseFromBottomAnimation(
        final View button,
        float startY,
        long duration,
        long delay) {

        AnimatorSet animatorSet = new AnimatorSet();

        // 保存按钮的原始位置
        final float originalY = button.getTranslationY();

        // 确保按钮在动画开始时是不可见的
        button.setAlpha(0f);
        button.setScaleX(0.5f);
        button.setScaleY(0.5f);

        // 位移动画（从屏幕底部外移动到原始位置）
        ObjectAnimator translateY = ObjectAnimator.ofFloat(
            button, "translationY", startY, originalY
        );

        // 透明度动画（0→1）
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(
            button, "alpha", 0, 1
        );

        // 缩放动画（0.5→1.0）- 实现从小变大的效果
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(
            button, "scaleX", 0.5f, 1.0f
        );
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(
            button, "scaleY", 0.5f, 1.0f
        );

        // 设置动画时长和插值器
        translateY.setDuration(duration);
        alphaAnimator.setDuration(duration);
        scaleX.setDuration(duration);
        scaleY.setDuration(duration);

        // 使用AccelerateDecelerateInterpolator实现平滑过渡
        AccelerateDecelerateInterpolator interpolator = new AccelerateDecelerateInterpolator();
        translateY.setInterpolator(interpolator);
        alphaAnimator.setInterpolator(interpolator);
        scaleX.setInterpolator(interpolator);
        scaleY.setInterpolator(interpolator);

        // 组合动画
        animatorSet.play(translateY).with(alphaAnimator).with(scaleX).with(scaleY);
        animatorSet.setStartDelay(delay);

        return animatorSet;
    }
}
