package com.example.fjnuserviceapp.ui.study;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.FragmentCourseTableBinding;
import com.example.fjnuserviceapp.model.Course;
import com.example.fjnuserviceapp.utils.ToastUtils;
import java.util.List;
import java.util.Random;

public class CourseTableFragment extends Fragment {
    private FragmentCourseTableBinding binding;
    private StudyViewModel viewModel;
    // 莫兰迪色系数组
    private final String[] MORANDI_COLORS = {
            "#F4E8D1", "#E2E1D7", "#D6E4E5", "#EFF0F6", "#FAD9C1", "#D0E6A5", "#FFDD94"
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCourseTableBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireParentFragment()).get(StudyViewModel.class);

        // 观察数据
        viewModel.getCurrentWeek().observe(getViewLifecycleOwner(), week -> {
            binding.tvCurrentWeek.setText("第 " + week + " 周");
        });

        viewModel.getCourses().observe(getViewLifecycleOwner(), this::renderCourses);

        // 按钮事件
        binding.btnPrevWeek.setOnClickListener(v -> {
            Integer current = viewModel.getCurrentWeek().getValue();
            if (current != null)
                viewModel.setWeek(current - 1);
        });

        binding.btnNextWeek.setOnClickListener(v -> {
            Integer current = viewModel.getCurrentWeek().getValue();
            if (current != null)
                viewModel.setWeek(current + 1);
        });

        binding.fabAdd.setOnClickListener(v -> {
            // TODO: 跳转添加页面
            ToastUtils.showShort(getContext(), "跳转添加课程");
        });
    }

    private void renderCourses(List<Course> courses) {
        if (binding == null)
            return;

        binding.courseContainer.post(() -> {
            if (binding == null)
                return;
            int containerWidth = binding.courseContainer.getWidth();
            int colWidth = containerWidth / 7;
            int rowHeight = (int) (80 * getResources().getDisplayMetrics().density); // 80dp

            binding.courseContainer.removeAllViews();

            if (courses == null || courses.isEmpty())
                return;

            for (Course c : courses) {
                View cardView = getLayoutInflater().inflate(R.layout.item_course_card, binding.courseContainer, false);

                TextView tvName = cardView.findViewById(R.id.tv_course_name);
                TextView tvRoom = cardView.findViewById(R.id.tv_course_room);
                CardView card = cardView.findViewById(R.id.card_view);

                tvName.setText(c.getName());
                tvRoom.setText(c.getLocation());

                // 设置颜色
                String colorHex = c.getColorTag();
                if (colorHex == null || colorHex.isEmpty()) {
                    // 随机或基于名称Hash
                    int index = Math.abs(c.getName().hashCode()) % MORANDI_COLORS.length;
                    colorHex = MORANDI_COLORS[index];
                }
                try {
                    card.setCardBackgroundColor(Color.parseColor(colorHex));
                } catch (Exception e) {
                    card.setCardBackgroundColor(Color.parseColor("#E3F2FD"));
                }

                // 计算位置
                int startRow = c.getStartSection() - 1;
                int endRow = c.getEndSection() - 1;
                int col = c.getDayOfWeek() - 1;

                // 边界检查
                if (startRow < 0)
                    startRow = 0;
                if (endRow > 7)
                    endRow = 7;
                if (col < 0)
                    col = 0;
                if (col > 6)
                    col = 6;
                if (startRow > endRow)
                    continue;

                int height = (endRow - startRow + 1) * rowHeight;

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(colWidth - 8, height - 8);
                params.leftMargin = col * colWidth + 4;
                params.topMargin = startRow * rowHeight + 4;

                cardView.setLayoutParams(params);

                cardView.setOnClickListener(v -> {
                    ToastUtils.showShort(getContext(), c.getName() + " @" + c.getLocation());
                });

                binding.courseContainer.addView(cardView);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
