package com.example.fjnuserviceapp.ui.notify;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fjnuserviceapp.databinding.ActivityNotifyBinding;

public class NotifyActivity extends AppCompatActivity {
    private ActivityNotifyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotifyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.notifyContainer.getId(), new NotifyFragment())
                    .commit();
        }
    }
}