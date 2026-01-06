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
import com.example.fjnuserviceapp.databinding.FragmentPrivateChatBinding;
import com.example.fjnuserviceapp.ui.notify.viewmodel.MessageViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PrivateChatFragment extends Fragment {
    private FragmentPrivateChatBinding binding;
    private MessageViewModel messageViewModel;
    private CollegeNotifyAdapter adapter;
    // 优化：加final，消除"Field can be converted to final"警告
    private final List<BaseMessage> chatList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPrivateChatBinding.inflate(inflater, container, false);
        initView();
        initViewModel();
        return binding.getRoot();
    }

    private void initView() {
        binding.rvPrivateChat.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CollegeNotifyAdapter(getContext(), chatList);
        binding.rvPrivateChat.setAdapter(adapter);

        // 新增：发送按钮点击事件
        binding.btnSend.setOnClickListener(v -> {
            String inputContent = binding.etInput.getText().toString().trim();
            if (!inputContent.isEmpty()) {
                // 1. 构造自己发送的消息
                BaseMessage sendMsg = new BaseMessage();
                sendMsg.setId(String.valueOf(System.currentTimeMillis()));
                sendMsg.setType(2);
                sendMsg.setTitle("");
                sendMsg.setContent(inputContent);
                sendMsg.setSender("我");
                sendMsg.setTime(System.currentTimeMillis());
                sendMsg.setRead(true);
                // 添加到列表并更新UI
                chatList.add(sendMsg);
                adapter.updateData(chatList);
                // 滚动到最后一条
                binding.rvPrivateChat.scrollToPosition(chatList.size() - 1);
                // 清空输入框
                binding.etInput.setText("");

                // 2. 模拟对方回复（2秒后）
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        BaseMessage replyMsg = new BaseMessage();
                        replyMsg.setId(String.valueOf(System.currentTimeMillis() + 1));
                        replyMsg.setType(2);
                        replyMsg.setTitle("");
                        replyMsg.setContent("收到你的消息啦！");
                        replyMsg.setSender("小张");
                        replyMsg.setTime(System.currentTimeMillis());
                        replyMsg.setRead(false);

                        // ========== 核心修正：安全调用runOnUiThread ==========
                        // 先判断Fragment是否已依附Activity，且Activity不为null
                        if (isAdded() && getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                chatList.add(replyMsg);
                                adapter.updateData(chatList);
                                binding.rvPrivateChat.scrollToPosition(chatList.size() - 1);
                            });
                        }
                    }
                }, 2000);
            }
        });
    }

    private void initViewModel() {
        messageViewModel = new ViewModelProvider(this).get(MessageViewModel.class);
        messageViewModel.privateChatList.observe(getViewLifecycleOwner(), messages -> {
            chatList.clear();
            chatList.addAll(messages);
            adapter.updateData(chatList);
        });
        messageViewModel.getPrivateChat("1001", getContext());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
