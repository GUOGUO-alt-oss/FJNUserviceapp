package com.example.fjnuserviceapp.ui.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fjnuserviceapp.databinding.FragmentMineBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class MineFragment extends Fragment {

    private FragmentMineBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMineBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup ViewPager2
        binding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new MineOverviewFragment();
                    case 1:
                        return new MineGrowthFragment();
                    case 2:
                        return new MineGoalFragment();
                    case 3:
                        return new MineRecordFragment();
                    case 4:
                        return new MineSettingsFragment();
                    default:
                        return new MineOverviewFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 5;
            }
        });

        // Setup TabLayout
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("概览");
                    tab.setIcon(android.R.drawable.ic_menu_compass);
                    break;
                case 1:
                    tab.setText("成长");
                    tab.setIcon(android.R.drawable.star_on);
                    break;
                case 2:
                    tab.setText("目标");
                    tab.setIcon(android.R.drawable.ic_menu_my_calendar);
                    break;
                case 3:
                    tab.setText("记录");
                    tab.setIcon(android.R.drawable.ic_menu_edit);
                    break;
                case 4:
                    tab.setText("设置");
                    tab.setIcon(android.R.drawable.ic_menu_preferences);
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
