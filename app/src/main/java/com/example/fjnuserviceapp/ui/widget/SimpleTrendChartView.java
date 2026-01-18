package com.example.fjnuserviceapp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.fjnuserviceapp.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class SimpleTrendChartView extends View {

    private List<Float> dataPoints = new ArrayList<>();
    private Paint linePaint;
    private Paint pointPaint;
    private Paint textPaint;
    private float maxVal = 4.0f;
    private float minVal = 0.0f;

    public SimpleTrendChartView(Context context) {
        super(context);
        init();
    }

    public SimpleTrendChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        linePaint = new Paint();
        linePaint.setColor(0xFF00F2FE); // Neon Cyan
        linePaint.setStrokeWidth(DisplayUtils.dp2px(getContext(), 2));
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);

        pointPaint = new Paint();
        pointPaint.setColor(Color.WHITE);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setAntiAlias(true);

        textPaint = new Paint();
        textPaint.setColor(0xCCFFFFFF);
        textPaint.setTextSize(DisplayUtils.dp2px(getContext(), 10));
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void setData(List<Float> data) {
        this.dataPoints = data;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataPoints == null || dataPoints.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        int padding = DisplayUtils.dp2px(getContext(), 20);

        int drawWidth = width - padding * 2;
        int drawHeight = height - padding * 2;

        float xInterval = (float) drawWidth / (dataPoints.size() - 1);
        
        Path path = new Path();
        
        for (int i = 0; i < dataPoints.size(); i++) {
            float val = dataPoints.get(i);
            float x = padding + i * xInterval;
            // Map value to Y (invert Y because canvas 0 is top)
            float y = height - padding - (val / maxVal) * drawHeight;

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }

            // Draw Point
            canvas.drawCircle(x, y, DisplayUtils.dp2px(getContext(), 4), pointPaint);
            
            // Draw Text
            canvas.drawText(String.valueOf(val), x, y - DisplayUtils.dp2px(getContext(), 8), textPaint);
        }

        canvas.drawPath(path, linePaint);
    }
}