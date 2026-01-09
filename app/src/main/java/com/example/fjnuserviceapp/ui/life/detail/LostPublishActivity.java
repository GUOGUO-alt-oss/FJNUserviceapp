package com.example.fjnuserviceapp.ui.life.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fjnuserviceapp.base.entity.LostAndFound;
import com.example.fjnuserviceapp.databinding.ActivityLostPublishBinding;

import java.util.UUID;

public class LostPublishActivity extends AppCompatActivity {
    private ActivityLostPublishBinding binding;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLostPublishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSubmit.setOnClickListener(v -> {
            String title   = binding.etTitle.getText().toString().trim();
            String desc    = binding.etDesc.getText().toString().trim();
            String loc     = binding.etLocation.getText().toString().trim();
            String contact = binding.etContact.getText().toString().trim();
            int category   = binding.radioLost.isChecked() ? 0 : 1;
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(contact)) {
                Toast.makeText(this, "标题和联系方式不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            LostAndFound item = new LostAndFound();
            item.setId(UUID.randomUUID().toString());
            item.setTitle(title); item.setDesc(desc); item.setLocation(loc);
            item.setContact(contact); item.setCategory(category);
            item.setTime(System.currentTimeMillis());
            Toast.makeText(this, "发布成功（本地模拟）", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}