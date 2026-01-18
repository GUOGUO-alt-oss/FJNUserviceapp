package com.example.fjnuserviceapp.ui.notify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fjnuserviceapp.databinding.FragmentNotifyBinding;
import com.example.fjnuserviceapp.ui.notify.sub.CollegeNotifyFragment;
import com.example.fjnuserviceapp.ui.notify.sub.ContactListFragment;
import com.example.fjnuserviceapp.ui.notify.sub.MessageCenterFragment;
import com.google.android.material.tabs.TabLayoutMediator;

public class NotifyFragment extends Fragment {
    private FragmentNotifyBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                switch (position) {
                    case 0: return new CollegeNotifyFragment();
                    case 1: return new ContactListFragment();
                    case 2: return new MessageCenterFragment();
                    default: return new CollegeNotifyFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 3; 
            }
        });

        // 2. 关联TabLayout和ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: 
                            tab.setText("学院通知"); 
                            tab.setIcon(android.R.drawable.ic_menu_info_details);
                            break;
                        case 1: 
                            tab.setText("私信"); 
                            tab.setIcon(android.R.drawable.ic_menu_send);
                            break;
                        case 2: 
                            tab.setText("消息中心"); 
                            tab.setIcon(android.R.drawable.ic_popup_reminder);
                            break;
                    }
                }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}