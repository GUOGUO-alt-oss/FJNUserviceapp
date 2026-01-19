package com.example.fjnuserviceapp.ui.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.example.fjnuserviceapp.databinding.FragmentStudyBinding;
import com.google.android.material.tabs.TabLayoutMediator;

public class StudyFragment extends Fragment {

    private FragmentStudyBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentStudyBinding.inflate(inflater, container, false);
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
                if (position == 0) {
                    return new TodoFragment();
                } else if (position == 1) {
                    return new ScheduleFragment();
                } else if (position == 2) {
                    return new GradesFragment();
                } else if (position == 3) {
                    return new CountdownFragment();
                } else {
                    return new MindfulnessFragment();
                }
            }

            @Override
            public int getItemCount() {
                return 5;
            }
        });

        // Setup Bottom Navigation (simulated with TabLayout for now or custom view)
        // actually, requirement says "Bottom navigation: Schedule, Grades"
        // Let's use TabLayout at bottom or standard BottomNavigationView linked to
        // ViewPager

        // For simplicity and style, let's use TabLayout at the bottom
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Todo");
                tab.setIcon(android.R.drawable.ic_menu_edit);
            } else if (position == 1) {
                tab.setText("课程表");
                tab.setIcon(android.R.drawable.ic_menu_agenda);
            } else if (position == 2) {
                tab.setText("成绩单");
                tab.setIcon(android.R.drawable.ic_menu_my_calendar);
            } else if (position == 3) {
                tab.setText("倒计时");
                tab.setIcon(android.R.drawable.ic_menu_recent_history);
            } else {
                tab.setText("正念");
                tab.setIcon(android.R.drawable.ic_menu_view); // Use a generic icon or custom
            }
        }).attach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
