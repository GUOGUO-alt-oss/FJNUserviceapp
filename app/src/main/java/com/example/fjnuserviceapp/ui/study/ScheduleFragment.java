package com.example.fjnuserviceapp.ui.study;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.fjnuserviceapp.databinding.FragmentScheduleBinding;
import com.example.fjnuserviceapp.model.Course;
import com.example.fjnuserviceapp.utils.DisplayUtils;
import com.example.fjnuserviceapp.utils.MockDataGenerator;

import java.util.List;

public class ScheduleFragment extends Fragment {

    private FragmentScheduleBinding binding;
    private int itemHeight;
    private int currentWeek = 5;
    private List<Course> allCourses;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentScheduleBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        itemHeight = DisplayUtils.dp2px(getContext(), 60);

        // Load Mock Data
        allCourses = MockDataGenerator.getMockCourses();

        // Initial Render
        updateWeekDisplay();
        renderCourses();

        // Setup Listeners
        setupWeekSwitcher();
        setupImportFeature();
    }

    private void setupWeekSwitcher() {
        binding.btnPrevWeek.setOnClickListener(v -> changeWeek(-1));
        binding.btnNextWeek.setOnClickListener(v -> changeWeek(1));

        // Remove click listener from title
        binding.tvWeekTitle.setOnClickListener(null);

        // Add Swipe Support
        android.view.GestureDetector gestureDetector = new android.view.GestureDetector(getContext(),
                new android.view.GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(android.view.MotionEvent e1, android.view.MotionEvent e2, float velocityX,
                            float velocityY) {
                        if (Math.abs(velocityX) > Math.abs(velocityY) && Math.abs(velocityX) > 100) {
                            if (velocityX > 0) {
                                changeWeek(-1); // Swipe Right -> Prev
                            } else {
                                changeWeek(1); // Swipe Left -> Next
                            }
                            return true;
                        }
                        return false;
                    }
                });

        binding.scrollView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return false; // Let ScrollView handle scrolling
        });
    }

    private void changeWeek(int offset) {
        int newWeek = currentWeek + offset;
        if (newWeek < 1) {
            Toast.makeText(getContext(), "已经是第一周了", Toast.LENGTH_SHORT).show();
            return;
        }
        if (newWeek > 20) {
            Toast.makeText(getContext(), "已经是最后一周了", Toast.LENGTH_SHORT).show();
            return;
        }
        currentWeek = newWeek;
        updateWeekDisplay();
        renderCourses();
    }

    private void updateWeekDisplay() {
        binding.tvWeekTitle.setText("第 " + currentWeek + " 周");
    }

    private void renderCourses() {
        binding.courseContainer.removeAllViews();

        int containerWidth = binding.courseContainer.getWidth();
        if (containerWidth == 0) {
            binding.courseContainer.post(this::renderCourses);
            return;
        }

        int itemWidth = containerWidth / 7;

        for (Course course : allCourses) {
            // Check week range
            if (currentWeek >= course.getStartWeek() && currentWeek <= course.getEndWeek()) {
                addCourseCard(course, itemWidth);
            }
        }
    }

    private void addCourseCard(Course course, int width) {
        // Create CardView for Glass/Rounded effect
        CardView card = new CardView(getContext());
        card.setCardElevation(0);
        card.setRadius(DisplayUtils.dp2px(getContext(), 8));
        card.setCardBackgroundColor(Color.TRANSPARENT); // We will set background on child

        // Layout Params
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width - 8,
                (course.getEndSection() - course.getStartSection() + 1) * itemHeight - 8);
        params.leftMargin = (course.getDayOfWeek() - 1) * width + 4;
        params.topMargin = (course.getStartSection() - 1) * itemHeight + 4;

        // Content
        TextView tv = new TextView(getContext());
        tv.setText(course.getName() + "\n@" + course.getLocation());
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(10);
        tv.setPadding(8, 8, 8, 8);
        tv.setGravity(Gravity.CENTER);

        // Custom Background with Color
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(Color.parseColor(course.getColorTag()));
        bg.setCornerRadius(DisplayUtils.dp2px(getContext(), 8));
        bg.setAlpha(200); // Semi-transparent
        tv.setBackground(bg);

        card.addView(tv);

        // Click Listener
        card.setOnClickListener(v -> {
            Toast.makeText(getContext(), course.getName() + " - " + course.getTeacher(), Toast.LENGTH_SHORT).show();
        });

        binding.courseContainer.addView(card, params);
    }

    private void setupImportFeature() {
        binding.btnImport.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("导入课表")
                    .setItems(new String[] { "从教务系统同步", "导入 PDF/图片 (OCR)" }, (dialog, which) -> {
                        if (which == 1) {
                            simulateOCR();
                        } else {
                            Toast.makeText(getContext(), "模拟从教务系统同步成功", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .show();
        });
    }

    private void simulateOCR() {
        // Show loading
        Toast.makeText(getContext(), "正在分析图片结构...", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Toast.makeText(getContext(), "AI 识别成功！已生成课表", Toast.LENGTH_LONG).show();
            // In a real app, this would parse the result and update `allCourses`
        }, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}