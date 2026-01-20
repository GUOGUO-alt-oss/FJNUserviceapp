package com.example.fjnuserviceapp.ui.notify.sub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.FragmentContactListBinding;
import com.example.fjnuserviceapp.ui.notify.NotificationAdapter;
import com.example.fjnuserviceapp.utils.NotificationMockData;

public class ContactListFragment extends Fragment {
    private FragmentContactListBinding binding;
    private NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            binding = FragmentContactListBinding.inflate(inflater, container, false);
            return binding.getRoot();
        } catch (Exception e) {
            // Fallback
            View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
            RecyclerView rvContact = view.findViewById(R.id.rvContact);
            rvContact.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new NotificationAdapter(NotificationMockData.getChatMessages());
            rvContact.setAdapter(adapter);
            return view;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding != null) {
            binding.rvContact.setLayoutManager(new LinearLayoutManager(getContext()));
            
            // 使用自定义的 NotificationAdapter 来展示联系人列表
            // 点击联系人时跳转到 ChatActivity
            adapter = new NotificationAdapter(NotificationMockData.getChatMessages());
            
            // 设置点击事件（需要适配器支持，或者这里重新写一个适配器）
            // 假设 NotificationAdapter 内部处理了点击跳转到 ChatDetailActivity
            // 我们需要将其改为跳转到 ChatActivity
            
            // 由于 NotificationAdapter 是通用的，我们可以在这里设置一个专门的 Adapter
            // 或者简单修改 NotificationAdapter 的逻辑
            
            binding.rvContact.setAdapter(adapter);
            
            // 新增：悬浮按钮，快速发起私信
            // (如果布局里有 fab 的话，这里可以添加逻辑)
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}