package com.example.fjnuserviceapp.ui.life;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.fjnuserviceapp.databinding.FragmentLifeBinding;
import com.example.fjnuserviceapp.ui.life.sub.LifeContactFragment;
import com.example.fjnuserviceapp.ui.life.sub.LostAndFoundFragment;


import com.google.android.material.tabs.TabLayoutMediator;

public class LifeFragment extends Fragment {

    private FragmentLifeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLifeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0) return new LostAndFoundFragment();
                if (position == 1) return new com.example.fjnuserviceapp.ui.life.IdleTradeFragment();
                return new LifeContactFragment();   // ← 新增：生活联系
            }

            @Override
            public int getItemCount() { return 3; }   // ← 改为 3
        });

        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("失物招领"); break;
                        case 1: tab.setText("闲置交易"); break;
                        case 2: tab.setText("生活联系"); break;   // ← 新增
                    }
                }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}