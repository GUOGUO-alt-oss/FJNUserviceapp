package com.example.fjnuserviceapp.ui.life;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fjnuserviceapp.databinding.ActivityLifeContainerBinding;
import com.example.fjnuserviceapp.R;

public class LifeActivity extends AppCompatActivity {
    private ActivityLifeContainerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLifeContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.lifeContainer.getId(), new LifeFragment())
                    .commit();
        }
    }
}