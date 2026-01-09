package com.example.fjnuserviceapp.ui.life.detail;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fjnuserviceapp.R;

public class LostAndFoundDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_and_found_detail);

        String title = getIntent().getStringExtra("title");
        String desc  = getIntent().getStringExtra("desc");

        ((TextView) findViewById(R.id.tvTitle)).setText(title);
        ((TextView) findViewById(R.id.tvDesc)).setText(desc);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}