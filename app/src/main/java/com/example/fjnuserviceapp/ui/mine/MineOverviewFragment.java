package com.example.fjnuserviceapp.ui.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.FragmentMineOverviewBinding;
import com.example.fjnuserviceapp.ui.widget.TrendLineView;
import com.example.fjnuserviceapp.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MineOverviewFragment extends Fragment {
    private FragmentMineOverviewBinding binding;
    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMineOverviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        setupViews();
        startEntryAnimation();
    }

    private void setupViews() {
        // 1. Header Data
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            android.widget.TextView tvName = binding.getRoot().findViewById(R.id.tv_header_name);
            if (tvName != null && profile != null) {
                // Mock Day Count: Day 128
                tvName.setText(profile.getName() + " · Day 128");
            }
        });

        // 2. Trend Data
        TrendLineView trendView = binding.getRoot().findViewById(R.id.trend_view);
        if (trendView != null) {
            List<Float> data = new ArrayList<>();
            // Mock data: 7 days trend
            data.add(3f);
            data.add(4f);
            data.add(3.5f);
            data.add(5f);
            data.add(4.5f);
            data.add(6f);
            data.add(5.5f);
            trendView.setData(data);
        }

        // 3. Quote Interaction
        View quoteView = binding.getRoot().findViewById(R.id.tv_daily_quote);
        if (quoteView != null) {
            quoteView.setOnClickListener(v -> ToastUtils.showShort(getContext(), "Edit Quote"));
            quoteView.setOnLongClickListener(v -> {
                ToastUtils.showShort(getContext(), "Saved to Record");
                return true;
            });
        }
    }

    private void startEntryAnimation() {
        // Root is NestedScrollView
        ViewGroup root = (ViewGroup) binding.getRoot();
        if (root.getChildCount() > 0) {
            // Child 0 is FrameLayout
            View frameObj = root.getChildAt(0);
            if (frameObj instanceof ViewGroup) {
                ViewGroup frame = (ViewGroup) frameObj;
                // FrameLayout has ParticleView (0) and LinearLayout (1)
                if (frame.getChildCount() > 1) {
                    View linearObj = frame.getChildAt(1);
                    if (linearObj instanceof ViewGroup) {
                        ViewGroup container = (ViewGroup) linearObj;

                        // Animate each child of the LinearLayout
                        for (int i = 0; i < container.getChildCount(); i++) {
                            View child = container.getChildAt(i);
                            child.setAlpha(0f);
                            child.setTranslationY(30f);

                            child.animate()
                                    .alpha(1f)
                                    .translationY(0f)
                                    .setStartDelay(i * 80) // 80ms cascade
                                    .setDuration(400)
                                    .setInterpolator(new DecelerateInterpolator())
                                    .start();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
