package com.example.fjnuserviceapp.ui.notify;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.base.entity.BaseMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatDetailActivity extends AppCompatActivity {
    private RecyclerView rvChat;
    private TextView tvTitle, etInput, btnBack, btnSend;
    private ChatAdapter adapter;
    private final List<BaseMessage> msgList = new ArrayList<>();
    private String contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 用传统方式加载布局（避免ViewBinding问题）
        setContentView(R.layout.activity_chat_detail);

        // 手动绑定所有控件
        bindViews();

        // 获取联系人信息（加默认值）
        contactName = getIntent().getStringExtra("contact_name");
        if (contactName == null) contactName = "未知联系人";
        tvTitle.setText(contactName);

        // 初始化消息列表
        initMsgData();
        initChatList();

        // 返回按钮
        btnBack.setOnClickListener(v -> finish());

        // 发送按钮
        btnSend.setOnClickListener(v -> sendMsg());
    }

    // 手动绑定控件
    private void bindViews() {
        tvTitle = findViewById(R.id.tvTitle);
        btnBack = findViewById(R.id.btnBack);
        rvChat = findViewById(R.id.rvChat);
        etInput = findViewById(R.id.etInput);
        btnSend = findViewById(R.id.btnSend);
    }

    // 模拟历史消息
    private void initMsgData() {
        BaseMessage msg1 = new BaseMessage();
        msg1.setContent("小明，你的作业我批改好了");
        msg1.setSender(contactName);
        msg1.setTime(System.currentTimeMillis() - 3600000);
        msg1.setType(2);

        BaseMessage msg2 = new BaseMessage();
        msg2.setContent("好的老师，我明天去办公室");
        msg2.setSender("我");
        msg2.setTime(System.currentTimeMillis() - 3500000);
        msg2.setType(2);

        msgList.add(msg1);
        msgList.add(msg2);
    }

    // 初始化聊天列表
    private void initChatList() {
        adapter = new ChatAdapter();
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(adapter);
    }

    // 发送消息
    private void sendMsg() {
        String content = etInput.getText().toString().trim();
        if (content.isEmpty()) return;

        // 自己发送的消息
        BaseMessage sendMsg = new BaseMessage();
        sendMsg.setContent(content);
        sendMsg.setSender("我");
        sendMsg.setTime(System.currentTimeMillis());
        sendMsg.setType(2);
        msgList.add(sendMsg);
        adapter.notifyItemInserted(msgList.size() - 1);
        rvChat.scrollToPosition(msgList.size() - 1);
        etInput.setText("");

        // 模拟对方回复（加Activity存活判断）
        if (isFinishing()) return;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    if (isFinishing()) return;
                    BaseMessage replyMsg = new BaseMessage();
                    replyMsg.setContent("收到你的消息啦！");
                    replyMsg.setSender(contactName);
                    replyMsg.setTime(System.currentTimeMillis());
                    replyMsg.setType(2);
                    msgList.add(replyMsg);
                    adapter.notifyItemInserted(msgList.size() - 1);
                    rvChat.scrollToPosition(msgList.size() - 1);
                });
            }
        }, 1500);
    }

    // 聊天适配器
    private class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_msg, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            BaseMessage msg = msgList.get(position);
            if (msg == null || msg.getContent() == null) return;

            holder.tvContent.setText(msg.getContent());
            LinearLayout itemContainer = (LinearLayout) holder.itemView;

            // 区分消息位置
            if (msg.getSender() != null && msg.getSender().equals("我")) {
                holder.tvContent.setBackgroundResource(R.drawable.bg_chat_me);
                itemContainer.setGravity(Gravity.END);
            } else {
                holder.tvContent.setBackgroundResource(R.drawable.bg_chat_other);
                itemContainer.setGravity(Gravity.START);
            }
        }

        @Override
        public int getItemCount() {
            return msgList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvContent;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvContent = itemView.findViewById(R.id.tvMsgContent);
            }
        }
    }
}