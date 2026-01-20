package com.example.fjnuserviceapp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple trend line view.
 * Displays a smooth curve representing a trend (e.g., last 7 days).
 * No axes, no values, just the shape.
 */
public class TrendLineView extends View {

    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();
    private final Path fillPath = new Path();
    private List<Float> dataPoints = new ArrayList<>();

    public TrendLineView(Context context) {
        super(context);
        init();
    }

    public TrendLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(6f);
        linePaint.setColor(Color.parseColor("#00F2FE")); // Cyan
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setPathEffect(new CornerPathEffect(30f)); // Smooth corners

        fillPaint.setStyle(Paint.Style.FILL);
    }

    public void setData(List<Float> data) {
        this.dataPoints = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (dataPoints == null || dataPoints.size() < 2)
            return;

        int w = getWidth();
        int h = getHeight();
        int padding = 20;
        int graphW = w - padding * 2;
        int graphH = h - padding * 2;

        float max = Float.MIN_VALUE;
        float min = Float.MAX_VALUE;
        for (float f : dataPoints) {
            max = Math.max(max, f);
            min = Math.min(min, f);
        }

        if (max == min)
            max = min + 1f; // Avoid divide by zero

        float range = max - min;
        float stepX = (float) graphW / (dataPoints.size() - 1);

        path.reset();
        fillPath.reset();

        float firstX = 0, firstY = 0;

        for (int i = 0; i < dataPoints.size(); i++) {
            float val = dataPoints.get(i);
            float normalized = (val - min) / range;

            float x = padding + i * stepX;
            // Invert Y (0 is top)
            float y = padding + graphH - (normalized * graphH);

            if (i == 0) {
                path.moveTo(x, y);
                firstX = x;
                firstY = y;
            } else {
                path.lineTo(x, y);
            }
        }

        // Draw Fill
        fillPath.set(path);
        fillPath.lineTo(w - padding, h);
        fillPath.lineTo(padding, h);
        fillPath.close();

        LinearGradient gradient = new LinearGradient(0, 0, 0, h,
                Color.parseColor("#4000F2FE"), Color.TRANSPARENT, Shader.TileMode.CLAMP);
        fillPaint.setShader(gradient);

        canvas.drawPath(fillPath, fillPaint);
        canvas.drawPath(path, linePaint);
    }
}
