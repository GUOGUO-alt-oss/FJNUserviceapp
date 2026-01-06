package com.example.fjnuserviceapp.ui.notify.sub;

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
import com.example.fjnuserviceapp.ui.notify.viewmodel.MessageViewModel;
import java.util.ArrayList;
import java.util.List;

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

        // 调用Mock数据方法（后续替换成真实接口只需改这行）
        messageViewModel.getCollegeNotify("1001", getContext());
    }

    private void updateNotifyList(List<BaseMessage> notifyList) {
        if (notifyList == null || notifyList.isEmpty()) {
            // 空数据：显示空数据视图
            binding.llEmpty.setVisibility(View.VISIBLE);
            binding.rvCollegeNotify.setVisibility(View.GONE);
        } else {
            // 有数据：显示列表
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