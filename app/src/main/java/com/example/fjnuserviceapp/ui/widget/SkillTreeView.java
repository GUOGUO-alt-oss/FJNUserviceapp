package com.example.fjnuserviceapp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 技能成长树
 * A simplified tree visualization that grows.
 */
public class SkillTreeView extends View {

    private final Paint branchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint leafPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path treePath = new Path();

    public SkillTreeView(Context context) {
        super(context);
        init();
    }

    public SkillTreeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private float growProgress = 0f;

    private void init() {
        branchPaint.setColor(Color.WHITE);
        branchPaint.setStyle(Paint.Style.STROKE);
        branchPaint.setStrokeWidth(4f);
        branchPaint.setStrokeCap(Paint.Cap.ROUND);

        leafPaint.setColor(Color.parseColor("#00F2FE")); // Cyan Leaves
        leafPaint.setStyle(Paint.Style.FILL);

        // Start Growth Animation
        post(growthRunnable);
    }

    private final Runnable growthRunnable = new Runnable() {
        @Override
        public void run() {
            if (growProgress < 1f) {
                growProgress += 0.02f;
                invalidate();
                postOnAnimation(this);
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        int cx = w / 2;
        int bottom = h - 20;

        // Draw Trunk
        treePath.reset();
        treePath.moveTo(cx, bottom);

        // Animate Growth: Interpolate end points
        float trunkX = cx - 20 * growProgress;
        float trunkY = bottom - 100 * growProgress;
        treePath.quadTo(cx, bottom - 50 * growProgress, trunkX, trunkY);

        if (growProgress > 0.5f) {
            float branchProgress = (growProgress - 0.5f) * 2f;

            // Left Branch
            treePath.moveTo(trunkX, trunkY);
            treePath.lineTo(trunkX - 20 * branchProgress, trunkY - 40 * branchProgress);

            // Right Branch
            treePath.moveTo(trunkX, trunkY);
            treePath.lineTo(trunkX + 50 * branchProgress, trunkY - 50 * branchProgress);
        }

        canvas.drawPath(treePath, branchPaint);

        // Draw Leaves (Skills) only when fully grown
        if (growProgress >= 0.9f) {
            float alpha = (growProgress - 0.9f) * 10f;
            leafPaint.setAlpha((int) (255 * alpha));

            canvas.drawCircle(cx - 40, bottom - 140, 8, leafPaint); // Python
            canvas.drawCircle(cx + 30, bottom - 150, 8, leafPaint); // Java
            canvas.drawCircle(cx - 10, bottom - 180, 6, leafPaint); // New Skill
        }
    }
}
