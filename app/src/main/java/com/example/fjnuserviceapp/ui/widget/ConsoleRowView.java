package com.example.fjnuserviceapp.ui.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fjnuserviceapp.R;

public class ConsoleRowView extends FrameLayout {

    private ImageView ivIcon;
    private TextView tvTitle;
    private TextView tvSubtitle;
    private ImageView ivArrow;
    private TextView tvBadge;

    public enum AnimationType {
        NONE,
        BREATHING,
        SWING,
        SCAN
    }

    public ConsoleRowView(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ConsoleRowView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.item_console_row, this, true);

        ivIcon = findViewById(R.id.iv_icon);
        tvTitle = findViewById(R.id.tv_title);
        tvSubtitle = findViewById(R.id.tv_subtitle);
        ivArrow = findViewById(R.id.iv_arrow);
        tvBadge = findViewById(R.id.tv_badge);

        if (attrs != null) {
            // Can add styleables here, but for now we set manually
        }

        // Setup Touch Listener for Sink Effect
        setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    animateSink();
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    animateRebound();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        performClick();
                    }
                    return true;
            }
            return false;
        });
    }

    private void animateSink() {
        animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
        // Arrow glow
        ivArrow.animate().alpha(1f).scaleX(1.2f).scaleY(1.2f).setDuration(200).start();
    }

    private void animateRebound() {
        animate().scaleX(1f).scaleY(1f).setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
        // Arrow reset
        ivArrow.animate().alpha(0.5f).scaleX(1f).scaleY(1f).setDuration(200).start();
    }

    public void setIcon(int resId) {
        ivIcon.setImageResource(resId);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setSubtitle(String subtitle) {
        tvSubtitle.setText(subtitle);
    }

    public void setBadge(String text) {
        if (text != null && !text.isEmpty()) {
            tvBadge.setVisibility(VISIBLE);
            tvBadge.setText(text);
        } else {
            tvBadge.setVisibility(GONE);
        }
    }

    public void startAnimation(AnimationType type) {
        ivIcon.clearAnimation();
        switch (type) {
            case BREATHING:
                ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivIcon, "scaleX", 1f, 1.2f, 1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivIcon, "scaleY", 1f, 1.2f, 1f);
                scaleX.setRepeatCount(ValueAnimator.INFINITE);
                scaleY.setRepeatCount(ValueAnimator.INFINITE);
                scaleX.setDuration(1500);
                scaleY.setDuration(1500);
                scaleX.start();
                scaleY.start();
                break;
            case SWING:
                ObjectAnimator rotate = ObjectAnimator.ofFloat(ivIcon, "rotation", -15f, 15f, -15f);
                rotate.setRepeatCount(ValueAnimator.INFINITE);
                rotate.setDuration(1000);
                rotate.start();
                break;
            case SCAN:
                // Scan is harder on ImageView, let's just do alpha flash
                ObjectAnimator alpha = ObjectAnimator.ofFloat(ivIcon, "alpha", 0.5f, 1f, 0.5f);
                alpha.setRepeatCount(ValueAnimator.INFINITE);
                alpha.setDuration(800);
                alpha.start();
                break;
            case NONE:
            default:
                ivIcon.setScaleX(1f);
                ivIcon.setScaleY(1f);
                ivIcon.setRotation(0f);
                ivIcon.setAlpha(1f);
                break;
        }
    }
}
