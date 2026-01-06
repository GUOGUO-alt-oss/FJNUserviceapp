package com.example.fjnuserviceapp.ui.notify.sub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fjnuserviceapp.adapter.CollegeNotifyAdapter;
import com.example.fjnuserviceapp.base.entity.BaseMessage;
import com.example.fjnuserviceapp.databinding.FragmentCollegeNotifyBinding;
import com.example.fjnuserviceapp.ui.notify.NoticeDetailActivity;
import com.example.fjnuserviceapp.ui.notify.viewmodel.MessageViewModel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CollegeNotifyFragment extends Fragment {
    private FragmentCollegeNotifyBinding binding;
    private MessageViewModel messageViewModel;
    private CollegeNotifyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCollegeNotifyBinding.inflate(inflater, container, false);
        initView();
        initViewModel();
        return binding.getRoot();
    }

    private void initView() {
        // 初始化列表
        binding.rvCollegeNotify.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CollegeNotifyAdapter(getContext(), new ArrayList<>());
        binding.rvCollegeNotify.setAdapter(adapter);

        // 核心修复：点击监听 - 所有通知都能打开详情，空内容兜底
        adapter.setOnItemClickListener(message -> {
            if (getContext() == null || message == null) return;

            Intent intent = new Intent(getContext(), NoticeDetailActivity.class);

            // 1. 标题：处理null + 空字符串，确保有默认值
            String title = message.getTitle();
            intent.putExtra("title", title == null || title.trim().isEmpty() ? "学院通知" : title);

            // 2. 内容：处理null + 空字符串，空则显示占位文字
            String content = message.getContent();
            intent.putExtra("content", content == null || content.trim().isEmpty()
                    ? "暂无详细内容（空数据占位）" : content);

            // 3. 发布者：固定值
            intent.putExtra("publisher", "学院教务处");

            // 4. 时间：格式化，0值兜底
            String timeStr = message.getTime() == 0
                    ? "2026-01-06"
                    : new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(message.getTime());
            intent.putExtra("time", timeStr);

            startActivity(intent);
        });
    }

    private void initViewModel() {
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);

        // 观察加载状态：显示/隐藏加载动画
        messageViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.llLoading.setVisibility(View.VISIBLE);
                binding.rvCollegeNotify.setVisibility(View.GONE);
                binding.llEmpty.setVisibility(View.GONE);
            } else {
                binding.llLoading.setVisibility(View.GONE);
            }
        });

        // 观察通知列表：更新UI
        messageViewModel.collegeNotifyList.observe(getViewLifecycleOwner(), this::updateNotifyList);

        // 调用Mock数据方法
        messageViewModel.getCollegeNotify("1001", getContext());
    }

    private void updateNotifyList(List<BaseMessage> notifyList) {
        if (notifyList == null || notifyList.isEmpty()) {
            binding.llEmpty.setVisibility(View.VISIBLE);
            binding.rvCollegeNotify.setVisibility(View.GONE);
        } else {
            binding.llEmpty.setVisibility(View.GONE);
            binding.rvCollegeNotify.setVisibility(View.VISIBLE);
            adapter.updateData(notifyList);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}