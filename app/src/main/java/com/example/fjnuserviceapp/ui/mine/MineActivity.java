package com.example.fjnuserviceapp.ui.mine;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.ActivityMineBinding;

public class MineActivity extends AppCompatActivity {
    private ActivityMineBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MineFragment())
                    .commit();
        }
    }
}
