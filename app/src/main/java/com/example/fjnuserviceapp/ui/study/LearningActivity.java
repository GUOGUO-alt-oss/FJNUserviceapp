package com.example.fjnuserviceapp.ui.study;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fjnuserviceapp.databinding.ActivityLearningBinding;

public class LearningActivity extends AppCompatActivity {
    private ActivityLearningBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLearningBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(binding.learningContainer.getId(), new StudyFragment())
                    .commit();
        }
    }
}
