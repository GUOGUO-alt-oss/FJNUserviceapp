package com.example.fjnuserviceapp.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AliveCardView extends FrameLayout {

    private static final float MAX_TILT_DEGREES = 4f;
    private Paint borderPaint;
    private float borderProgress = -1f; // -1 means no animation
    private ValueAnimator borderAnimator;

    public AliveCardView(@NonNull Context context) {
        super(context);
        init();
    }

    public AliveCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AliveCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Enable drawing for border
        setWillNotDraw(false);

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(6f); // 2dp approx

        // Setup Tilt
        // We handle touch event
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float cx = getWidth() / 2f;
        float cy = getHeight() / 2f;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float dx = event.getX() - cx;
                float dy = event.getY() - cy;

                // Calculate tilt (inverted logic often feels better: touch right -> tilt right
                // means rotate Y positive? No, rotate Y around Y axis)
                // If I push right side (positive X), I want it to go "down" (away from me).
                // RotationY: positive rotates Right side AWAY (into screen). So Positive X ->
                // Positive RotationY.
                // RotationX: positive rotates Bottom side UP (towards me).
                // If I push Bottom (positive Y), I want it to go "down" (away).
                // RotationX: positive rotates Top side AWAY. So Positive Y -> NEGATIVE
                // RotationX.

                float tiltX = -(dy / cy) * MAX_TILT_DEGREES;
                float tiltY = (dx / cx) * MAX_TILT_DEGREES;

                setRotationX(tiltX);
                setRotationY(tiltY);

                // Micro-sink
                animate().scaleX(0.98f).scaleY(0.98f).setDuration(100).start();

                // Trigger Border Flow if not running
                startBorderFlow();
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                animate().rotationX(0).rotationY(0).scaleX(1f).scaleY(1f).setDuration(200).start();
                performClick();
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    public void animateEntrance(long delay) {
        setAlpha(0f);
        setTranslationY(100f);
        setScaleX(0.9f);
        setScaleY(0.9f);

        animate()
                .alpha(1f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay(delay)
                .setDuration(600)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void startBorderFlow() {
        if (borderAnimator != null && borderAnimator.isRunning())
            return;

        borderAnimator = ValueAnimator.ofFloat(0f, 1f);
        borderAnimator.setDuration(300);
        borderAnimator.addUpdateListener(animation -> {
            borderProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        borderAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                borderProgress = -1f;
                invalidate();
            }
        });
        borderAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (borderProgress >= 0) {
            // Draw flowing border
            int w = getWidth();
            int h = getHeight();

            // Create a gradient that moves
            // Simple approach: Linear Gradient from Top-Left to Bottom-Right, shifted by
            // progress

            int[] colors = new int[] { 0x00FFFFFF, 0xFF00F2FE, 0xFF4FACFE, 0x00FFFFFF };
            float[] positions = new float[] { 0f, 0.4f, 0.6f, 1f };

            // Map progress 0..1 to gradient translation
            // Let's make the gradient larger than view

            // Actually, a SweepGradient might be cooler but harder to "flow" linearly.
            // Let's stick to a passing light beam.

            float startX = -w + (w * 2 * borderProgress);
            float startY = -h + (h * 2 * borderProgress);
            float endX = startX + w;
            float endY = startY + h;

            LinearGradient shader = new LinearGradient(startX, startY, endX, endY,
                    colors, positions, Shader.TileMode.CLAMP);

            borderPaint.setShader(shader);

            // Draw rect (assuming rounded corners handled by background, but we can draw
            // rounded rect)
            // Use a large radius to match glass card
            float radius = 16 * getResources().getDisplayMetrics().density;
            canvas.drawRoundRect(0, 0, w, h, radius, radius, borderPaint);
        }
    }
}
