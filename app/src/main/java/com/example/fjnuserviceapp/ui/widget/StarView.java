package com.example.fjnuserviceapp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 3D Star View
 * Draws a connecting star shape between vertices
 */
public class StarView extends View {
    private final List<Float> vertices = new ArrayList<>();
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();

    public StarView(Context context) {
        this(context, null);
    }

    public StarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // Line Paint (Neon Glow)
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3f);
        paint.setShadowLayer(10f, 0, 0, Color.parseColor("#00F2FE")); // Cyan Glow

        // Fill Paint (Gradient)
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAlpha(30); // Very transparent
    }

    /**
     * Update vertices positions relative to the center of this view
     * @param newVertices List of [x0, y0, x1, y1, ...]
     */
    public void setVertices(List<Float> newVertices) {
        vertices.clear();
        vertices.addAll(newVertices);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (vertices.size() < 10) return; // Need 5 points (10 floats)

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        path.reset();

        // 5-Pointed Star Connectivity: 0 -> 2 -> 4 -> 1 -> 3 -> 0
        int[] indices = {0, 2, 4, 1, 3};

        // Move to first point
        float firstX = cx + vertices.get(indices[0] * 2);
        float firstY = cy + vertices.get(indices[0] * 2 + 1);
        path.moveTo(firstX, firstY);

        for (int i = 1; i < 5; i++) {
            int idx = indices[i];
            float x = cx + vertices.get(idx * 2);
            float y = cy + vertices.get(idx * 2 + 1);
            path.lineTo(x, y);
        }
        path.close();

        // Draw Fill
        if (fillPaint.getShader() == null) {
            fillPaint.setShader(new LinearGradient(0, 0, getWidth(), getHeight(),
                    Color.parseColor("#1AFFFFFF"),
                    Color.parseColor("#00FFFFFF"),
                    Shader.TileMode.CLAMP));
        }
        canvas.drawPath(path, fillPaint);

        // Draw Stroke
        canvas.drawPath(path, paint);
    }
}
