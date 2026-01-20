package com.example.fjnuserviceapp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import androidx.annotation.Nullable;

/**
 * 技能进度环 (Skill Ring)
 * Displays skill level and progress.
 */
public class SkillRingView extends View {

    private final Paint bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();

    private float progress = 0f; // 0 to 1
    private float targetProgress = 0f;
    private int level = 1;
    private int color = Color.parseColor("#00F2FE"); // Default Cyan

    public SkillRingView(Context context) {
        super(context);
        init();
    }

    public SkillRingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        bgPaint.setStyle(Paint.Style.STROKE);
        bgPaint.setStrokeWidth(12f);
        bgPaint.setColor(Color.parseColor("#20FFFFFF"));
        bgPaint.setStrokeCap(Paint.Cap.ROUND);

        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(12f);
        progressPaint.setColor(color);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(40f);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setFakeBoldText(true);
    }

    public void setSkill(int level, float progress, int color) {
        this.level = level;
        this.targetProgress = progress;
        this.color = color;
        progressPaint.setColor(color);
        startAnimation();
    }

    private void startAnimation() {
        // Simple manual animation
        post(new Runnable() {
            @Override
            public void run() {
                if (progress < targetProgress) {
                    progress += 0.02f;
                    if (progress > targetProgress)
                        progress = targetProgress;
                    invalidate();
                    postOnAnimation(this);
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        float padding = 20f;

        rect.set(padding, padding, w - padding, h - padding);

        // Draw Background Ring
        canvas.drawArc(rect, 0, 360, false, bgPaint);

        // Draw Progress Ring (Start from top -90)
        canvas.drawArc(rect, -90, progress * 360, false, progressPaint);

        // Draw Level Text
        Paint.FontMetrics fm = textPaint.getFontMetrics();
        float y = h / 2f - (fm.ascent + fm.descent) / 2f;
        canvas.drawText("Lv." + level, w / 2f, y, textPaint);
    }
}
