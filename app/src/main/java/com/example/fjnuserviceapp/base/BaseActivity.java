package com.example.fjnuserviceapp.base;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

/**
 * 所有Activity的基类，封装ViewBinding和通用逻辑
 */
public abstract class BaseActivity<VB extends ViewBinding> extends AppCompatActivity {
    protected VB binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 绑定ViewBinding
        binding = getViewBinding();
        setContentView(binding.getRoot());
        // 初始化页面
        initView();
        initData();
        initListener();
    }

    // 抽象方法：获取ViewBinding（由子类实现）
    protected abstract VB getViewBinding();

    // 可选实现：初始化View
    protected void initView() {
    }

    // 可选实现：初始化数据
    protected void initData() {
    }

    // 可选实现：初始化监听
    protected void initListener() {
    }
}