package com.example.fjnuserviceapp.ui.notify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.FragmentNotifyBinding;
import com.example.fjnuserviceapp.ui.notify.sub.CollegeNotifyFragment;
import com.example.fjnuserviceapp.ui.notify.sub.MessageCenterFragment;
// 核心修改1：导入新的联系人列表Fragment（替换原来的PrivateChatFragment）
import com.example.fjnuserviceapp.ui.notify.sub.ContactListFragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class NotifyFragment extends Fragment {
    private FragmentNotifyBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 初始化ViewBinding
        binding = FragmentNotifyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. 配置ViewPager2适配器
        binding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                // 对应三个子Fragment
                switch (position) {
                    case 0: return new CollegeNotifyFragment();
                    // 核心修改2：把PrivateChatFragment换成ContactListFragment
                    case 1: return new ContactListFragment();
                    case 2: return new MessageCenterFragment();
                    default: return new CollegeNotifyFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 3; // 三个Tab
            }
        });

        // 2. 关联TabLayout和ViewPager2（标签文字不变，还是“私信”）
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("学院通知"); break;
                        case 1: tab.setText("私信"); break; // 标签文字仍为“私信”
                        case 2: tab.setText("消息中心"); break;
                    }
                }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 清空Binding，避免内存泄漏
        binding = null;
    }
}