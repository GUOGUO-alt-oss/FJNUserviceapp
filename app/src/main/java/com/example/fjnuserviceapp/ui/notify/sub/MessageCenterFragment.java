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
import com.example.fjnuserviceapp.databinding.FragmentMessageCenterBinding;
import com.example.fjnuserviceapp.ui.notify.CollegeNotifyDetailActivity;
import com.example.fjnuserviceapp.ui.notify.viewmodel.MessageViewModel;
import java.util.ArrayList;
import java.util.List;

public class MessageCenterFragment extends Fragment {
    private FragmentMessageCenterBinding binding;
    private MessageViewModel messageViewModel;
    private CollegeNotifyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMessageCenterBinding.inflate(inflater, container, false);
        initView();
        initViewModel();
        return binding.getRoot();
    }

    private void initView() {
        binding.rvMessageCenter.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CollegeNotifyAdapter(getContext(), new ArrayList<>());
        binding.rvMessageCenter.setAdapter(adapter);

        // 点击事件：传递基础类型，避免对象序列化问题
        adapter.setOnItemClickListener(message -> {
            if (getContext() == null) return; // 防空指针
            Intent intent = new Intent(getContext(), CollegeNotifyDetailActivity.class);
            // 只传递必要的基础类型，100%稳定
            intent.putExtra("title", message.getTitle() == null ? "" : message.getTitle());
            intent.putExtra("content", message.getContent() == null ? "" : message.getContent());
            intent.putExtra("sender", message.getSender() == null ? "" : message.getSender());
            intent.putExtra("time", message.getTime());
            startActivity(intent);
        });
    }

    private void initViewModel() {
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.messageCenterList.observe(getViewLifecycleOwner(), this::updateMessageList);
        messageViewModel.getMessageCenter("1001", getContext());
    }

    private void updateMessageList(List<BaseMessage> messageList) {
        adapter.updateData(messageList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}