package com.example.fjnuserviceapp.ui.life.detail;

import android.os.Bundle; import android.text.TextUtils; import android.view.inputmethod.EditorInfo;
import androidx.appcompat.app.AppCompatActivity; import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fjnuserviceapp.base.entity.ChatMessage; import com.example.fjnuserviceapp.databinding.ActivityChatBinding; import com.example.fjnuserviceapp.ui.life.adapter.ChatAdapter;
import java.util.ArrayList; import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding; private ChatAdapter adapter; private final List<ChatMessage> messages = new ArrayList<>();
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater()); setContentView(binding.getRoot());
        String contactName = getIntent().getStringExtra("contact_name");
        binding.tvTitle.setText(contactName == null ? "对方" : contactName);
        binding.btnBack.setOnClickListener(v -> finish());
        adapter = new ChatAdapter(messages);
        binding.recyclerChat.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerChat.setAdapter(adapter);
        addMsg("你好，我是 " + binding.tvTitle.getText(), false);
        addMsg("你好，我是我", true);
        binding.btnSend.setOnClickListener(v -> sendMsg());
        binding.etInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) { sendMsg(); return true; } return false;
        });
    }
    private void sendMsg() {
        String text = binding.etInput.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;
        addMsg(text, true); binding.etInput.setText("");
        binding.recyclerChat.postDelayed(() -> addMsg("收到你的消息啦！", false), 1200);
    }
    private void addMsg(String content, boolean isMe) {
        ChatMessage msg = new ChatMessage(); msg.setContent(content); msg.setMe(isMe);
        messages.add(msg); adapter.notifyItemInserted(messages.size() - 1);
        binding.recyclerChat.scrollToPosition(messages.size() - 1);
    }
}