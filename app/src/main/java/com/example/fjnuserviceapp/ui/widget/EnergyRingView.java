package com.example.fjnuserviceapp.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class EnergyRingView extends View {

    private Paint ringPaint;
    private float currentRotation = 0f;
    private float rotationSpeed = 2.0f; // Default speed
    private float scaleFactor = 1.0f;
    private boolean isAnimating = true;

    // Colors: Blue -> Purple -> Cyan -> Blue (loop)
    private final int[] colors = new int[] {
            0xFF00F2FE, // Cyan
            0xFF4FACFE, // Purple
            0xFF00F2FE // Cyan
    };

    private Matrix gradientMatrix;
    private SweepGradient sweepGradient;

    public EnergyRingView(Context context) {
        super(context);
        init();
    }

    public EnergyRingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EnergyRingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ringPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setStrokeWidth(8f); // Adjust as needed

        gradientMatrix = new Matrix();

        // Start breathing animation
        startBreathing();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Create gradient based on size
        sweepGradient = new SweepGradient(w / 2f, h / 2f, colors, null);
        ringPaint.setShader(sweepGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        float cx = w / 2f;
        float cy = h / 2f;
        float radius = (Math.min(w, h) / 2f) - ringPaint.getStrokeWidth();

        canvas.save();
        // Apply Scale (Breathing)
        canvas.scale(scaleFactor, scaleFactor, cx, cy);

        // Apply Rotation
        canvas.rotate(currentRotation, cx, cy);

        // Update Rotation for next frame
        if (isAnimating) {
            currentRotation += rotationSpeed;
            if (currentRotation >= 360)
                currentRotation -= 360;
            invalidate();
        }

        canvas.drawCircle(cx, cy, radius, ringPaint);
        canvas.restore();
    }

    private void startBreathing() {
        ValueAnimator breathingAnimator = ValueAnimator.ofFloat(0.97f, 1.03f);
        breathingAnimator.setDuration(2000);
        breathingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        breathingAnimator.setRepeatMode(ValueAnimator.REVERSE);
        breathingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        breathingAnimator.addUpdateListener(animation -> {
            scaleFactor = (float) animation.getAnimatedValue();
            // invalidate is handled by rotation loop, but just in case
            if (!isAnimating)
                invalidate();
        });
        breathingAnimator.start();
    }

    public void setRotationSpeed(float speed) {
        this.rotationSpeed = speed;
    }

    /**
     * Trigger explosion animation
     * 
     * @param onComplete Callback when explosion finishes
     */
    public void explode(Runnable onComplete) {
        // 1. Accelerate Rotation
        ValueAnimator speedAnim = ValueAnimator.ofFloat(rotationSpeed, rotationSpeed * 5);
        speedAnim.setDuration(200);
        speedAnim.setInterpolator(new LinearInterpolator());
        speedAnim.addUpdateListener(anim -> rotationSpeed = (float) anim.getAnimatedValue());

        // 2. Scale Up and Fade Out
        animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .alpha(0f)
                .setDuration(300)
                .setStartDelay(200) // Wait for acceleration
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (onComplete != null)
                            onComplete.run();
                        // Reset after a delay if needed, or keep it hidden
                        // For now, let's reset it after 1 second so it comes back
                        postDelayed(() -> reset(), 1000);
                    }
                })
                .start();

        speedAnim.start();
    }

    public void reset() {
        setScaleX(1f);
        setScaleY(1f);
        setAlpha(1f);
        rotationSpeed = 2.0f;
    }
}
