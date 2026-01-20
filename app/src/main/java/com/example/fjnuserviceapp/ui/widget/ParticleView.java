package com.example.fjnuserviceapp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 粒子背景View
 * 显示缓慢移动的半透明粒子
 */
public class ParticleView extends View {

    private static class Particle {
        float x;
        float y;
        float radius;
        float speedX;
        float speedY;
        float baseAlpha;
        float currentAlpha;
        float alphaOffset; // Random offset for blink cycle
        int color;
        int layer; // 0: Background (Small, Slow), 1: Midground, 2: Foreground (Large, Fast)

        public Particle(float w, float h, Random random) {
            this.x = random.nextFloat() * w;
            this.y = random.nextFloat() * h;
            this.layer = random.nextInt(3); // 0, 1, 2
            this.alphaOffset = random.nextFloat() * 1000;

            switch (layer) {
                case 0: // Far: Small, Dark, Fast (Depth parallax)
                    this.radius = 1f + random.nextFloat();
                    this.speedX = (random.nextFloat() - 0.5f) * 0.3f;
                    this.speedY = (random.nextFloat() - 0.5f) * 0.3f;
                    this.baseAlpha = 0.3f;
                    break;
                case 1: // Mid: Medium
                    this.radius = 2f + random.nextFloat();
                    this.speedX = (random.nextFloat() - 0.5f) * 0.6f;
                    this.speedY = (random.nextFloat() - 0.5f) * 0.6f;
                    this.baseAlpha = 0.5f;
                    break;
                case 2: // Near: Large, Bright, Slow (Focus)
                    this.radius = 3f + random.nextFloat() * 2;
                    this.speedX = (random.nextFloat() - 0.5f) * 0.9f;
                    this.speedY = (random.nextFloat() - 0.5f) * 0.9f;
                    this.baseAlpha = 0.7f;
                    break;
            }

            // Randomly assign Neon Colors or White
            if (random.nextFloat() > 0.7) { // 30% colored
                this.color = random.nextBoolean() ? 0xFF00F2FE : 0xFF4FACFE; // Neon Cyan or Purple
            } else {
                this.color = 0xFFFFFFFF; // White
            }
        }
    }

    private final List<Particle> particles = new ArrayList<>();
    private Paint paint;
    private final Random random = new Random();
    private static final int PARTICLE_COUNT = 80; // Increased count for layers

    public ParticleView(Context context) {
        super(context);
        init();
    }

    public ParticleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ParticleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        particles.clear();
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles.add(new Particle(w, h, random));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int w = getWidth();
        int h = getHeight();
        long time = System.currentTimeMillis();

        for (Particle p : particles) {
            // Update position
            p.x += p.speedX;
            p.y += p.speedY;

            // Bounce off edges (Wrap around for infinite space effect)
            if (p.x < -p.radius)
                p.x = w + p.radius;
            if (p.x > w + p.radius)
                p.x = -p.radius;
            if (p.y < -p.radius)
                p.y = h + p.radius;
            if (p.y > h + p.radius)
                p.y = -p.radius;

            // Blink Animation (Sine wave)
            // Cycle: 2-5 seconds
            float blinkFactor = (float) Math.sin((time + p.alphaOffset) / 500.0); // -1 to 1
            // Map -1..1 to -0.2..0.2 offset
            float alphaChange = blinkFactor * 0.2f;
            p.currentAlpha = Math.max(0.1f, Math.min(1.0f, p.baseAlpha + alphaChange));

            // Draw
            paint.setColor(p.color);
            paint.setAlpha((int) (p.currentAlpha * 255));
            canvas.drawCircle(p.x, p.y, p.radius, paint);
        }

        // Animate
        invalidate();
    }
}
