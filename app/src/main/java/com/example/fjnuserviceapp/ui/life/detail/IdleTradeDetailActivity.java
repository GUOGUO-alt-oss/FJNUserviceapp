package com.example.fjnuserviceapp.ui.life.detail;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fjnuserviceapp.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class IdleTradeDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_trade_detail);

        String title   = getIntent().getStringExtra("title");
        String desc    = getIntent().getStringExtra("desc");
        double price   = getIntent().getDoubleExtra("price", 0);
        String contact = getIntent().getStringExtra("contact");
        long   time    = getIntent().getLongExtra("time", 0);

        ((TextView) findViewById(R.id.tvTitle)).setText(title);
        ((TextView) findViewById(R.id.tvDesc)).setText(desc);
        ((TextView) findViewById(R.id.tvPrice)).setText("¥ " + price);
        ((TextView) findViewById(R.id.tvContact)).setText(contact);
        ((TextView) findViewById(R.id.tvTime)).setText(
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(time));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}