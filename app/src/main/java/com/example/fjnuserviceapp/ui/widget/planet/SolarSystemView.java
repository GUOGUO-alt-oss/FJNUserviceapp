package com.example.fjnuserviceapp.ui.widget.planet;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.List;

public class SolarSystemView extends ViewGroup {

    // Orbits
    private static final int ORBIT_COUNT = 3;
    // Radius in DP (Circumradius of the star)
    private final float[] orbitRadiiDp = { 80f, 130f, 180f };
    private final float[] orbitRadiiPx = new float[ORBIT_COUNT];
    // Current Rotation Angle for each orbit (System rotation)
    private final float[] orbitAngles = { 0f, 45f, 90f };
    // Initial static rotation for visual variety (Orbit 0: 0, Orbit 1: 36, Orbit 2:
    // 0)
    private final float[] orbitStaticOffsets = { 0f, 36f, 0f };

    // Drag Sensitivity Factors (Inner orbits rotate faster)
    private final float[] dragFactors = { 1.5f, 1.0f, 0.5f };

    // Auto Rotation Speed (Degrees per frame)
    private final float[] autoRotationSpeeds = { 0.2f, 0.1f, 0.05f };
    private boolean isAutoRotating = true;

    // Children management
    private final List<List<View>> orbitViews = new ArrayList<>();

    // Drawing
    private Paint orbitPaint;
    private int centerX, centerY;
    private final Path starPath = new Path(); // Reuse path

    // Interaction
    private GestureDetector gestureDetector;
    private Scroller scroller;

    public SolarSystemView(Context context) {
        this(context, null);
    }

    public SolarSystemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SolarSystemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false); // Enable onDraw for orbits

        // Orbit Paint
        orbitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        orbitPaint.setColor(Color.WHITE);
        orbitPaint.setStyle(Paint.Style.STROKE);
        orbitPaint.setStrokeWidth(2f);
        orbitPaint.setAlpha(50); // Faint lines
        orbitPaint.setPathEffect(new DashPathEffect(new float[] { 10, 10 }, 0));
        // Add Glow
        orbitPaint.setShadowLayer(10f, 0, 0, Color.parseColor("#00F2FE"));

        // Radii to Px
        float density = getResources().getDisplayMetrics().density;
        for (int i = 0; i < ORBIT_COUNT; i++) {
            orbitRadiiPx[i] = orbitRadiiDp[i] * density;
            orbitViews.add(new ArrayList<>());
        }

        // Interaction
        scroller = new Scroller(context, new DecelerateInterpolator());
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // Rotate based on X distance
                rotateSystem(-distanceX / 5f); // Sensitivity
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                scroller.fling(0, 0, (int) velocityX, 0, -10000, 10000, 0, 0);
                postInvalidate();
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                scroller.forceFinished(true);
                return true;
            }
        });

        // Start Auto Rotation
        post(autoRotationRunnable);
    }

    private final Runnable autoRotationRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isAutoRotating)
                return;

            for (int i = 0; i < ORBIT_COUNT; i++) {
                orbitAngles[i] += autoRotationSpeeds[i];
                if (orbitAngles[i] >= 360)
                    orbitAngles[i] -= 360;
            }
            requestLayout();
            postOnAnimation(this);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(autoRotationRunnable);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(autoRotationRunnable);
    }

    public void addPlanet(View view, int orbitIndex, float startAngle) {
        if (orbitIndex < 0 || orbitIndex >= ORBIT_COUNT)
            return;

        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.orbitIndex = orbitIndex;
        lp.angle = startAngle;
        view.setLayoutParams(lp);

        addView(view);
        orbitViews.get(orbitIndex).add(view);
    }

    private void rotateSystem(float deltaAngle) {
        for (int i = 0; i < ORBIT_COUNT; i++) {
            orbitAngles[i] += deltaAngle * dragFactors[i];
        }
        requestLayout(); // Re-position children
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            // Simplified fling logic
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
        centerX = width / 2;
        centerY = height / 2;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (lp.orbitIndex == -1) {
                // Center Core
                int w = child.getMeasuredWidth();
                int h = child.getMeasuredHeight();
                int left = centerX - w / 2;
                int top = centerY - h / 2;
                child.layout(left, top, left + w, top + h);
            } else {
                // Planet on Star Orbit
                float radius = orbitRadiiPx[lp.orbitIndex];
                float systemRotation = orbitAngles[lp.orbitIndex];
                float planetProgressAngle = lp.angle + systemRotation;
                float staticOffset = orbitStaticOffsets[lp.orbitIndex];

                // Calculate position on the Star Path
                PointF pos = getPointOnStarPath(radius, planetProgressAngle, staticOffset);

                int w = child.getMeasuredWidth();
                int h = child.getMeasuredHeight();
                int cx = (int) (centerX + pos.x);
                int cy = (int) (centerY + pos.y);

                child.layout(cx - w / 2, cy - h / 2, cx + w / 2, cy + h / 2);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw Star Orbits
        for (int i = 0; i < ORBIT_COUNT; i++) {
            float r = orbitRadiiPx[i];
            float staticOffset = orbitStaticOffsets[i];
            drawStarPath(canvas, r, staticOffset);
        }
    }

    /**
     * Draw a 5-pointed star (Pentagram)
     */
    private void drawStarPath(Canvas canvas, float radius, float rotationOffset) {
        starPath.reset();

        // Vertices at 0, 72, 144, 216, 288 (Base)
        // Order: 0 -> 2 -> 4 -> 1 -> 3 -> 0
        int[] indices = { 0, 2, 4, 1, 3, 0 };

        for (int k = 0; k < indices.length; k++) {
            int idx = indices[k];
            // Base angle: -90 (top) + idx * 72
            float angleDeg = -90 + idx * 72 + rotationOffset;
            double rad = Math.toRadians(angleDeg);
            float x = (float) (centerX + radius * Math.cos(rad));
            float y = (float) (centerY + radius * Math.sin(rad));

            if (k == 0)
                starPath.moveTo(x, y);
            else
                starPath.lineTo(x, y);
        }
        starPath.close();
        canvas.drawPath(starPath, orbitPaint);
    }

    /**
     * Calculate position on the star path given an angle (0-360)
     * Maps 0-360 to the perimeter of the pentagram.
     */
    private PointF getPointOnStarPath(float radius, float angleDeg, float rotationOffset) {
        // Normalize angle
        float angle = angleDeg % 360;
        if (angle < 0)
            angle += 360;

        // Pentagram has 5 segments.
        // Segment 0: 0 -> 2
        // Segment 1: 2 -> 4
        // Segment 2: 4 -> 1
        // Segment 3: 1 -> 3
        // Segment 4: 3 -> 0
        // We map 360 degrees evenly to these 5 segments (72 deg per segment)

        int segmentIndex = (int) (angle / 72);
        float t = (angle % 72) / 72f; // Progress along segment (0..1)

        // Vertex indices for the path sequence
        int[] pathSequence = { 0, 2, 4, 1, 3, 0 };

        int startIndex = pathSequence[segmentIndex];
        int endIndex = pathSequence[segmentIndex + 1];

        // Calculate coords of Start and End vertices
        // Vertex Angle = -90 + index * 72 + offset
        float startAngle = -90 + startIndex * 72 + rotationOffset;
        float endAngle = -90 + endIndex * 72 + rotationOffset;

        float sx = (float) (radius * Math.cos(Math.toRadians(startAngle)));
        float sy = (float) (radius * Math.sin(Math.toRadians(startAngle)));

        float ex = (float) (radius * Math.cos(Math.toRadians(endAngle)));
        float ey = (float) (radius * Math.sin(Math.toRadians(endAngle)));

        // Linear Interpolation
        float x = sx + (ex - sx) * t;
        float y = sy + (ey - sy) * t;

        return new PointF(x, y);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {
        public int orbitIndex = -1; // -1 means center
        public float angle = 0f;

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
