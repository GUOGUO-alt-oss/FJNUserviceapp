package com.example.fjnuserviceapp.ui.life;

import android.os.Bundle; import android.text.TextUtils; import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity; import com.example.fjnuserviceapp.base.entity.IdleItem;
import com.example.fjnuserviceapp.databinding.ActivityIdlePublishBinding; import java.util.UUID;

public class IdlePublishActivity extends AppCompatActivity {
    private ActivityIdlePublishBinding binding;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIdlePublishBinding.inflate(getLayoutInflater()); setContentView(binding.getRoot());
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSubmit.setOnClickListener(v -> {
            String title  = binding.etTitle.getText().toString().trim();
            String desc   = binding.etDesc.getText().toString().trim();
            String priceS = binding.etPrice.getText().toString().trim();
            String contact= binding.etContact.getText().toString().trim();
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(priceS) || TextUtils.isEmpty(contact)) {
                Toast.makeText(this, "标题、价格、联系方式不能为空", Toast.LENGTH_SHORT).show(); return;
            }
            IdleItem item = new IdleItem();
            item.setId(UUID.randomUUID().toString()); item.setTitle(title); item.setDesc(desc);
            item.setPrice(Double.parseDouble(priceS)); item.setContact(contact);
            item.setTime(System.currentTimeMillis());
            Toast.makeText(this, "发布成功（本地模拟）", Toast.LENGTH_SHORT).show(); finish();
        });
    }
}