package com.example.fjnuserviceapp.ui.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
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
    // 新增：本地存储（记录黑名单/免打扰状态）
    private SharedPreferences sp;
    private boolean isBlacklist; // 是否拉黑
    private boolean isMute;      // 是否免打扰

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 获取联系人名称
        contactName = getIntent().getStringExtra("contact_name");
        if (contactName == null) contactName = "私信助手";

        // 新增：初始化本地存储（持久化保存设置）
        sp = getSharedPreferences("ChatSettings", Context.MODE_PRIVATE);
        isBlacklist = sp.getBoolean("blacklist_" + contactName, false);
        isMute = sp.getBoolean("mute_" + contactName, false);

        initView();

        // 根据联系人发送不同的欢迎语（拉黑后不显示欢迎语）
        if (!isBlacklist) {
            String welcomeMsg = "你好！我是" + contactName + "，有什么事吗？";
            if (contactName.contains("辅导员")) {
                welcomeMsg = "同学你好，有什么学习或生活上的问题需要咨询吗？";
            } else if (contactName.contains("张三")) {
                welcomeMsg = "嗨！去打球吗？";
            } else if (contactName.contains("李四")) {
                welcomeMsg = "项目进度怎么样了？";
            }
            addMessage(welcomeMsg, false);
        } else {
            addMessage("该联系人已被拉黑，无法接收消息", false);
            etInput.setEnabled(false); // 拉黑后禁用输入
            btnSend.setEnabled(false);
        }
    }

    private void initView() {
        recyclerChat = findViewById(R.id.recycler_chat);
        etInput = findViewById(R.id.et_input);
        btnSend = findViewById(R.id.btn_send);
        ImageView btnBack = findViewById(R.id.btn_back);
        // 新增：绑定设置按钮
        ImageView ivSetting = findViewById(R.id.iv_chat_setting);

        // 设置标题（原有逻辑优化）
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(contactName);

        // 原有返回按钮逻辑
        btnBack.setOnClickListener(v -> finish());

        // 新增：设置按钮点击事件（弹出设置弹窗）
        ivSetting.setOnClickListener(v -> showSettingDialog());

        // 原有聊天列表布局逻辑
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // 从底部开始显示
        recyclerChat.setLayoutManager(layoutManager);

        adapter = new ChatAdapter(messages);
        recyclerChat.setAdapter(adapter);

        // 原有发送按钮逻辑
        btnSend.setOnClickListener(v -> sendMessage());
    }

    // 新增：显示聊天设置弹窗（黑名单+消息免打扰）
// 新增：显示聊天设置弹窗（黑名单+消息免打扰）
    private void showSettingDialog() {
        // 1. 创建自定义弹窗布局（原有逻辑不变）
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_chat_setting, null);
        Switch swBlacklist = dialogView.findViewById(R.id.sw_blacklist);
        Switch swMute = dialogView.findViewById(R.id.sw_mute);

        // 2. 初始化开关状态（原有逻辑不变）
        swBlacklist.setChecked(isBlacklist);
        swMute.setChecked(isMute);

        // 3. 黑名单开关监听（原有逻辑不变）
        swBlacklist.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isBlacklist = isChecked;
            sp.edit().putBoolean("blacklist_" + contactName, isChecked).apply();
            etInput.setEnabled(!isChecked);
            btnSend.setEnabled(!isChecked);
            if (isChecked) {
                addMessage("已拉黑「" + contactName + "」，无法发送/接收消息", false);
            } else {
                addMessage("已解除「" + contactName + "」的拉黑状态", false);
            }
        });

        // 4. 消息免打扰开关监听（原有逻辑不变）
        swMute.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isMute = isChecked;
            sp.edit().putBoolean("mute_" + contactName, isChecked).apply();
            if (isChecked) {
                addMessage("已开启「" + contactName + "」的消息免打扰", false);
            } else {
                addMessage("已关闭「" + contactName + "」的消息免打扰", false);
            }
        });

        // 5. 核心修改：创建弹窗并设置深色主题（替换原有弹窗创建代码）
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("聊天设置")
                .setView(dialogView)
                .setPositiveButton("确定", null)
                .create();

        // 关键：禁用系统默认背景，让布局的深色背景显示
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 可选：设置弹窗标题栏文字颜色为白色（匹配深色主题）
        dialog.setOnShowListener(dialogInterface -> {
            TextView titleView = dialog.findViewById(android.R.id.title);
            if (titleView != null) {
                titleView.setTextColor(Color.WHITE);
            }
            // 设置确定按钮文字颜色为霓虹蓝（匹配你的主题）
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.neon_cyan));
        });

        // 显示弹窗
        dialog.show();
    }

    // 原有发送消息逻辑（新增拉黑判断）
    private void sendMessage() {
        // 拉黑后禁止发送消息
        if (isBlacklist) return;

        String content = etInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) return;

        // 1. 发送用户消息
        addMessage(content, true);
        etInput.setText("");

        // 2. 模拟对方输入延迟 (0.5 ~ 1.5s)
        long delay = 500 + random.nextInt(1000);
        handler.postDelayed(() -> {
            // 3. 自动回复（免打扰模式下不回复）
            if (!isMute) {
                String reply = AUTO_REPLIES[random.nextInt(AUTO_REPLIES.length)];
                addMessage(reply, false);
            }
        }, delay);
    }

    // 原有添加消息逻辑（无修改）
    private void addMessage(String content, boolean isUser) {
        messages.add(new ChatMessage(content, isUser));
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerChat.smoothScrollToPosition(messages.size() - 1);
    }
}