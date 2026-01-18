package com.example.fjnuserviceapp.ui.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerChat;
    private EditText etInput;
    private View btnSend;
    private ChatAdapter adapter;
    private List<ChatMessage> messages = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();

    // 预设回复库
    private final String[] AUTO_REPLIES = {
            "好滴，我收到了~",
            "嗯嗯，知道啦！",
            "👌 已收到您的反馈",
            "正在处理中，请稍候...",
            "这是个好问题，我会记下来的",
            "收到！还有其他需要帮忙的吗？",
            "好的，没问题！",
            "收到收到！"
    };

    private String contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 获取联系人名称
        contactName = getIntent().getStringExtra("contact_name");
        if (contactName == null) contactName = "私信助手";

        initView();
        
        // 根据联系人发送不同的欢迎语
        String welcomeMsg = "你好！我是" + contactName + "，有什么事吗？";
        if (contactName.contains("辅导员")) {
            welcomeMsg = "同学你好，有什么学习或生活上的问题需要咨询吗？";
        } else if (contactName.contains("张三")) {
            welcomeMsg = "嗨！去打球吗？";
        } else if (contactName.contains("李四")) {
            welcomeMsg = "项目进度怎么样了？";
        }
        
        addMessage(welcomeMsg, false);
    }

    private void initView() {
        recyclerChat = findViewById(R.id.recycler_chat);
        etInput = findViewById(R.id.et_input);
        btnSend = findViewById(R.id.btn_send);
        ImageView btnBack = findViewById(R.id.btn_back);
        
        // 设置标题
        android.widget.TextView tvTitle = findViewById(R.id.btn_back).getParent() instanceof android.view.ViewGroup ? 
            (android.widget.TextView) ((android.view.ViewGroup)findViewById(R.id.btn_back).getParent()).getChildAt(1) : null;
        if (tvTitle != null) tvTitle.setText(contactName);

        btnBack.setOnClickListener(v -> finish());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // 从底部开始显示
        recyclerChat.setLayoutManager(layoutManager);
        
        adapter = new ChatAdapter(messages);
        recyclerChat.setAdapter(adapter);

        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String content = etInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) return;

        // 1. 发送用户消息
        addMessage(content, true);
        etInput.setText("");

        // 2. 模拟对方输入延迟 (0.5 ~ 1.5s)
        long delay = 500 + random.nextInt(1000);
        handler.postDelayed(() -> {
            // 3. 自动回复
            String reply = AUTO_REPLIES[random.nextInt(AUTO_REPLIES.length)];
            addMessage(reply, false);
        }, delay);
    }

    private void addMessage(String content, boolean isUser) {
        messages.add(new ChatMessage(content, isUser));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerChat.smoothScrollToPosition(messages.size() - 1);
    }
}