package com.example.fjnuserviceapp.ui.study;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fjnuserviceapp.databinding.FragmentMindfulnessBinding;

public class MindfulnessFragment extends Fragment {

    private FragmentMindfulnessBinding binding;
    private MindfulnessViewModel viewModel;
    private MindfulnessAdapter adapter;
    private boolean isExercising = false;
    private ObjectAnimator breathingAnimator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentMindfulnessBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(MindfulnessViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();
    }

    private void setupRecyclerView() {
        adapter = new MindfulnessAdapter();
        binding.recyclerHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerHistory.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getRecords().observe(getViewLifecycleOwner(), list -> adapter.submitList(list));

        viewModel.getTotalDuration().observe(getViewLifecycleOwner(),
                duration -> binding.tvTotalTime.setText("总时长: " + (duration == null ? 0 : duration) + " min"));

        viewModel.getTotalCount().observe(getViewLifecycleOwner(),
                count -> binding.tvTotalCount.setText("总次数: " + (count == null ? 0 : count)));
    }

    private void setupListeners() {
        binding.fabStart.setOnClickListener(v -> toggleExercise());
        binding.cardExercise.setOnClickListener(v -> toggleExercise());
    }

    private void toggleExercise() {
        if (isExercising) {
            stopExercise();
        } else {
            startExercise();
        }
    }

    private void startExercise() {
        isExercising = true;
        binding.fabStart.setImageResource(android.R.drawable.ic_media_pause);
        binding.tvGuide.setText("吸气...");

        // Breathing Animation: Scale Up (Inhale) -> Scale Down (Exhale)
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.5f);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.5f);

        breathingAnimator = ObjectAnimator.ofPropertyValuesHolder(binding.viewBreathingCircle, scaleX, scaleY);
        breathingAnimator.setDuration(4000); // 4s inhale
        breathingAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        breathingAnimator.setRepeatMode(ValueAnimator.REVERSE); // 4s exhale
        breathingAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

        breathingAnimator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            // Simple text update based on fraction
            // Note: Since it's REVERSE mode, fraction goes 0->1 then 1->0?
            // Actually ObjectAnimator REVERSE plays backwards.
            // Let's rely on listener for cycle change or just simple timer logic for text
        });

        breathingAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                // Toggle text on repeat
                if (binding.tvGuide.getText().toString().equals("吸气...")) {
                    binding.tvGuide.setText("呼气...");
                } else {
                    binding.tvGuide.setText("吸气...");
                }
            }
        });

        breathingAnimator.start();
    }

    private void stopExercise() {
        isExercising = false;
        binding.fabStart.setImageResource(android.R.drawable.ic_media_play);
        binding.tvGuide.setText("练习完成！点击再次开始");

        if (breathingAnimator != null) {
            breathingAnimator.cancel();
            binding.viewBreathingCircle.setScaleX(1f);
            binding.viewBreathingCircle.setScaleY(1f);
        }

        // Save Record (Mock 3 mins)
        viewModel.addRecord("呼吸练习", 3);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (breathingAnimator != null) {
            breathingAnimator.cancel();
        }
        binding = null;
    }
}
