package com.example.fjnuserviceapp.ui.notify;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fjnuserviceapp.R;

public class NoticeDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        // 获取传递的通知数据（用空数据占位）
        String title = getIntent().getStringExtra("title") == null ? "无标题" : getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content") == null ? "无内容" : getIntent().getStringExtra("content");
        String publisher = getIntent().getStringExtra("publisher") == null ? "未知发布者" : getIntent().getStringExtra("publisher");
        String time = getIntent().getStringExtra("time") == null ? "" : getIntent().getStringExtra("time");

        // 绑定控件并显示
        TextView tvTitle = findViewById(R.id.tvNoticeTitle);
        TextView tvPublisher = findViewById(R.id.tvNoticePublisher);
        TextView tvTime = findViewById(R.id.tvNoticeTime);
        TextView tvContent = findViewById(R.id.tvNoticeContent);

        tvTitle.setText(title);
        tvPublisher.setText("发布者：" + publisher);
        tvTime.setText(time);
        tvContent.setText(content);

        // 返回按钮
        findViewById(R.id.btnNoticeBack).setOnClickListener(v -> finish());
    }
}