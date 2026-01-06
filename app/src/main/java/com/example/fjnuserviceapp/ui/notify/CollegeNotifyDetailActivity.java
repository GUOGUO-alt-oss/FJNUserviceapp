package com.example.fjnuserviceapp.ui.notify;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fjnuserviceapp.R;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CollegeNotifyDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 强制使用布局文件，避免ViewBinding问题
        setContentView(R.layout.activity_college_notify_detail);

        // 1. 绑定所有控件（传统方式，最稳定）
        Button btnBack = findViewById(R.id.btnBack);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvSender = findViewById(R.id.tvSender);
        TextView tvTime = findViewById(R.id.tvTime);
        TextView tvContent = findViewById(R.id.tvContent);

        // 2. 接收数据（加默认值，避免空指针）
        String title = getIntent().getStringExtra("title") == null ? "无标题" : getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content") == null ? "无内容" : getIntent().getStringExtra("content");
        String sender = getIntent().getStringExtra("sender") == null ? "未知发布者" : getIntent().getStringExtra("sender");
        long time = getIntent().getLongExtra("time", System.currentTimeMillis());

        // 3. 填充数据（100%不会崩溃）
        tvTitle.setText(title);
        tvContent.setText(content);
        tvSender.setText("发布者：" + sender);
        // 时间格式化（加异常捕获）
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
            tvTime.setText(sdf.format(time));
        } catch (Exception e) {
            tvTime.setText("未知时间");
        }

        // 4. 返回按钮（最简单的点击事件）
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 关闭当前页，返回上一页
            }
        });
    }
}