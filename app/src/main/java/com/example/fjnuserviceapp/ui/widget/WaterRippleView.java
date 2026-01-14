package com.example.fjnuserviceapp.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 水面涟漪效果自定义View
 */
public class WaterRippleView extends View {

    // 涟漪数据类
    private static class Ripple {
        float x; // 涟漪中心X坐标
        float y; // 涟漪中心Y坐标
        float radius; // 涟漪当前半径
        float maxRadius; // 涟漪最大半径
        float alpha; // 涟漪透明度
        Paint paint; // 涟漪画笔

        public Ripple(float x, float y, float maxRadius) {
            this.x = x;
            this.y = y;
            this.radius = 0;
            this.maxRadius = maxRadius;
            this.alpha = 0.6f;
            
            // 初始化画笔
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(3f);
            paint.setColor(0xFFFFFFFF); // 白色涟漪
        }
    }

    private List<Ripple> ripples; // 涟漪列表
    private Paint clearPaint; // 清除画布的画笔
    private boolean isAnimating; // 是否正在动画中

    public WaterRippleView(Context context) {
        super(context);
        init();
    }

    public WaterRippleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterRippleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        ripples = new ArrayList<>();
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        isAnimating = false;
        setLayerType(LAYER_TYPE_HARDWARE, null); // 启用硬件加速
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 当用户触摸屏幕时，创建新的涟漪
            createRipple(event.getX(), event.getY());
        }
        return true;
    }

    /**
     * 创建新的涟漪
     */
    private void createRipple(float x, float y) {
        // 计算最大半径，确保涟漪能扩散到屏幕边缘
        float maxRadius = Math.max(
            Math.max(x, getWidth() - x),
            Math.max(y, getHeight() - y)
        );
        
        Ripple ripple = new Ripple(x, y, maxRadius);
        ripples.add(ripple);
        
        // 启动涟漪动画
        startRippleAnimation(ripple);
    }

    /**
     * 启动涟漪动画
     */
    private void startRippleAnimation(final Ripple ripple) {
        // 缩放动画（半径从小到大）
        ScaleAnimation scaleAnimation = new ScaleAnimation(
            0, 1, 0, 1,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(2000);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        
        // 透明度动画（从不透明到透明）
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.6f, 0);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        
        // 动画监听器
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 动画结束后，移除涟漪
                ripples.remove(ripple);
                if (ripples.isEmpty()) {
                    isAnimating = false;
                }
                invalidate();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        
        // 启动动画
        startAnimation(alphaAnimation);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        // 绘制所有涟漪
        for (int i = ripples.size() - 1; i >= 0; i--) {
            Ripple ripple = ripples.get(i);
            
            // 更新涟漪半径
            ripple.radius += 3; // 每次绘制增加半径
            if (ripple.radius > ripple.maxRadius) {
                ripple.radius = ripple.maxRadius;
            }
            
            // 更新涟漪透明度
            ripple.alpha = 0.6f * (1 - ripple.radius / ripple.maxRadius);
            if (ripple.alpha < 0) {
                ripple.alpha = 0;
            }
            
            // 设置画笔透明度
            ripple.paint.setAlpha((int) (ripple.alpha * 255));
            
            // 绘制涟漪
            canvas.drawCircle(ripple.x, ripple.y, ripple.radius, ripple.paint);
            
            // 如果涟漪透明度为0，移除它
            if (ripple.alpha <= 0) {
                ripples.remove(i);
            }
        }
        
        // 如果还有涟漪，继续绘制
        if (!ripples.isEmpty()) {
            invalidate();
        } else {
            isAnimating = false;
        }
    }

    /**
     * 清除所有涟漪
     */
    public void clearRipples() {
        ripples.clear();
        invalidate();
    }
}
