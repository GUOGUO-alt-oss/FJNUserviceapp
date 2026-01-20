package com.example.fjnuserviceapp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 校园活跃度网络图
 * A dynamic node-link graph representing social influence.
 */
public class NetworkGraphView extends View {

    private final Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint nodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final List<Node> nodes = new ArrayList<>();
    private final Random random = new Random();

    private static class Node {
        float x, y;
        float radius;
        float speedX, speedY;
        int color;
        String label; // For future expansion (e.g. Club Name)
    }

    public NetworkGraphView(Context context) {
        super(context);
        init();
    }

    public NetworkGraphView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NetworkGraphView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(2f);
        linePaint.setAlpha(50);

        nodePaint.setStyle(Paint.Style.FILL);

        centerPaint.setColor(Color.parseColor("#00F2FE")); // Cyan
        centerPaint.setStyle(Paint.Style.FILL);
        centerPaint.setShadowLayer(20, 0, 0, Color.CYAN);
        setLayerType(LAYER_TYPE_SOFTWARE, centerPaint); // For shadow

        // Touch Interaction
        setOnTouchListener((v, event) -> {
            // Simple touch effect: repel nodes or select
            // For now, just trigger a ripple or shake
            return super.onTouchEvent(event);
        });
    }

    // ... generateNodes ...
    private void generateNodes(int w, int h) {
        nodes.clear();
        int count = 8;
        // Nodes represent: 3 Clubs, 2 Projects, 3 Friends
        for (int i = 0; i < count; i++) {
            Node node = new Node();
            // Initial positions clustered around center but spreading out
            float angle = (float) (random.nextDouble() * Math.PI * 2);
            float dist = 50 + random.nextInt(Math.min(w, h) / 3);

            node.x = w / 2f + (float) (Math.cos(angle) * dist);
            node.y = h / 2f + (float) (Math.sin(angle) * dist);

            node.radius = 6 + random.nextInt(8);
            node.speedX = (random.nextFloat() - 0.5f) * 0.8f; // Slower movement
            node.speedY = (random.nextFloat() - 0.5f) * 0.8f;

            // Color based on type (Mock)
            if (i < 3)
                node.color = Color.parseColor("#FF6B6B"); // Red for Clubs
            else if (i < 5)
                node.color = Color.parseColor("#4ECDC4"); // Teal for Projects
            else
                node.color = Color.WHITE; // Friends

            nodes.add(node);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        // Update positions
        for (Node node : nodes) {
            node.x += node.speedX;
            node.y += node.speedY;

            // Bounce
            if (node.x < 0 || node.x > getWidth())
                node.speedX *= -1;
            if (node.y < 0 || node.y > getHeight())
                node.speedY *= -1;

            // Draw connection to center
            canvas.drawLine(cx, cy, node.x, node.y, linePaint);

            // Draw Node
            nodePaint.setColor(node.color);
            canvas.drawCircle(node.x, node.y, node.radius, nodePaint);
        }

        // Draw Center (User)
        canvas.drawCircle(cx, cy, 12, centerPaint);

        invalidate(); // Animate
    }
}
