package com.example.fjnuserviceapp.ui.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.FragmentMineBinding;
import com.example.fjnuserviceapp.model.UserProfile;
import com.example.fjnuserviceapp.ui.study.StudyFragment;
import com.example.fjnuserviceapp.utils.ToastUtils;
import java.util.Locale;

public class MineFragment extends Fragment {
    private FragmentMineBinding binding;
    private ProfileViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMineBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        setupObservers();
        setupFunctionEntries();
        setupSettingsEntries();

        // 启动粒子背景动画（如果需要手动控制，ParticleView 默认自动运行）
        // 启动头像光环动画
        startAvatarHaloAnimation();
    }

    private void startAvatarHaloAnimation() {
        if (binding.ivAvatarHalo != null) {
            com.example.fjnuserviceapp.utils.AnimationUtils.createBreathingAnimation(binding.ivAvatarHalo);

            // 头像本身的脉冲
            binding.ivAvatar.setOnClickListener(v -> {
                com.example.fjnuserviceapp.utils.AnimationUtils.createPulseAnimation(v).start();
                // 原有跳转逻辑
                // ...
            });
        }
    }

    private void setupObservers() {
        // 1. 用户信息
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                binding.tvName.setText(profile.getName());
                binding.tvIdMajor.setText("ID: " + profile.getStudentId() + " | " + profile.getMajor());
                if (profile.getSignature() != null && !profile.getSignature().isEmpty()) {
                    binding.tvStatus.setText(profile.getSignature());
                }
            } else {
                binding.tvName.setText("未登录");
                binding.tvIdMajor.setText("点击头像登录");
            }
        });

        // 2. 统计数据
        viewModel.getStats().observe(getViewLifecycleOwner(), stats -> {
            if (stats == null)
                return;

            updateStatCard(binding.statCardTerm, "当前学期", stats.termName);
            updateStatCard(binding.statCardCourses, "已修课程", stats.courseCount + " 门");
            updateStatCard(binding.statCardGpa, "平均绩点", String.format(Locale.getDefault(), "%.2f", stats.gpa));
            updateStatCard(binding.statCardToday, "今日课程", stats.todayCourseCount + " 节");
        });
    }

    private void updateStatCard(Object bindingObject, String label, String value) {
        // 由于 ViewBinding 包含在 include 中，直接使用 binding 对象获取 View
        // 但这里为了简化，我们假设传入的是 ViewBinding 对象
        // 实际上，ViewBinding 会为 include 生成一个成员变量

        // 正确的做法是：
        // binding.statCardTerm.tvStatLabel.setText(label);
        // binding.statCardTerm.tvStatValue.setText(value);

        // 但我们需要一个通用的方法。
        // 由于 item_profile_stat_card.xml 生成了 ItemProfileStatCardBinding
        // 我们可以让方法接收 ItemProfileStatCardBinding
    }

    // 重载方法，直接操作 Specific Binding
    private void updateStatCard(com.example.fjnuserviceapp.databinding.ItemProfileStatCardBinding cardBinding,
            String label, String value) {
        cardBinding.tvStatLabel.setText(label);
        cardBinding.tvStatValue.setText(value);
    }

    private void setupFunctionEntries() {
        setupEntry(binding.entrySchedule, "我的课表", "查看本周课程", android.R.drawable.ic_menu_agenda, v -> {
            ToastUtils.showShort(getContext(), "请切换到[学习]页面查看课表");
        });

        setupEntry(binding.entryGrades, "我的成绩", "GPA 与排名", android.R.drawable.ic_menu_my_calendar, v -> {
            ToastUtils.showShort(getContext(), "请切换到[学习]页面查看成绩");
        });

        setupEntry(binding.entryFavorites, "我的收藏", "文章与通知", android.R.drawable.star_on, v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new FavoritesFragment())
                    .addToBackStack(null)
                    .commit();
        });

        setupEntry(binding.entryImportHistory, "导入记录", "查看 OCR 历史", android.R.drawable.ic_menu_recent_history, v -> {
            ToastUtils.showShort(getContext(), "功能开发中");
        });
    }

    private void setupSettingsEntries() {
        setupEntry(binding.entryEditProfile, "编辑资料", "修改头像与签名", android.R.drawable.ic_menu_edit, v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ProfileFragment())
                    .addToBackStack(null)
                    .commit();
        });

        setupEntry(binding.entrySettings, "系统设置", "通知与主题", android.R.drawable.ic_menu_preferences, v -> {
            ToastUtils.showShort(getContext(), "设置功能开发中");
        });

        setupEntry(binding.entryAbout, "关于应用", "Version 1.0.0", android.R.drawable.ic_menu_info_details, v -> {
            ToastUtils.showShort(getContext(), "福师大生活服务 App v1.0");
        });
    }

    private void setupEntry(com.example.fjnuserviceapp.databinding.ItemFunctionEntryBinding entryBinding, String title,
            String subtitle, int iconResId,
            View.OnClickListener listener) {
        entryBinding.tvTitle.setText(title);
        entryBinding.tvSubtitle.setText(subtitle);
        entryBinding.ivIcon.setImageResource(iconResId);
        entryBinding.getRoot().setOnClickListener(listener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
