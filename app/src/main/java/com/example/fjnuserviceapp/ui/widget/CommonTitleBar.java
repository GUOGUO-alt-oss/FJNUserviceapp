package com.example.fjnuserviceapp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.LayoutCommonTitleBarBinding;

/**
 * 统一标题栏组件
 * 支持左侧返回按钮、标题文字、右侧按钮
 */
public class CommonTitleBar extends FrameLayout {

    private final LayoutCommonTitleBarBinding binding;

    public CommonTitleBar(Context context) {
        this(context, null);
    }

    public CommonTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        binding = LayoutCommonTitleBarBinding.inflate(LayoutInflater.from(context), this, true);
        init();
    }

    private void init() {
        // 初始设置
        binding.tvTitle.setText("");
        binding.ivLeft.setVisibility(VISIBLE);
        binding.tvRight.setVisibility(GONE);
        binding.ivRight.setVisibility(GONE);
    }

    /**
     * 设置标题文字
     */
    public void setTitle(String title) {
        binding.tvTitle.setText(title);
    }

    /**
     * 设置左侧按钮点击事件
     */
    public void setLeftOnClickListener(OnClickListener listener) {
        binding.ivLeft.setOnClickListener(listener);
    }

    /**
     * 设置右侧按钮点击事件
     */
    public void setRightOnClickListener(OnClickListener listener) {
        binding.tvRight.setOnClickListener(listener);
        binding.ivRight.setOnClickListener(listener);
    }

    /**
     * 设置右侧按钮文字
     */
    public void setRightText(String text) {
        binding.tvRight.setText(text);
        binding.tvRight.setVisibility(VISIBLE);
        binding.ivRight.setVisibility(GONE);
    }

    /**
     * 设置右侧图标
     */
    public void setRightIcon(int resId) {
        binding.ivRight.setImageResource(resId);
        binding.ivRight.setVisibility(VISIBLE);
        binding.tvRight.setVisibility(GONE);
    }
}