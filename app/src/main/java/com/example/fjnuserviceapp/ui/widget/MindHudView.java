package com.example.fjnuserviceapp.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.utils.AnimationUtils;

import java.util.Random;

public class MindHudView extends FrameLayout {

    private TextView tvEnergyValue;
    private ProgressBar progressEnergy;
    private TextView tvFocusLevel;
    private TextView tvDailyPrompt;

    public MindHudView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MindHudView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_mind_hud, this, true);

        tvEnergyValue = findViewById(R.id.tv_energy_value);
        progressEnergy = findViewById(R.id.progress_energy);
        tvFocusLevel = findViewById(R.id.tv_focus_level);
        tvDailyPrompt = findViewById(R.id.tv_daily_prompt);

        refreshDailyState();
    }

    public void refreshDailyState() {
        Random random = new Random();

        // 1. Energy (60-100)
        int energy = 60 + random.nextInt(41);
        AnimationUtils.animateRollingNumber(tvEnergyValue, 0, energy, false);
        progressEnergy.setProgress(energy);

        // 2. Focus
        String[] focusLevels = { "Low", "Mid", "High", "Zen" };
        String focus = focusLevels[random.nextInt(focusLevels.length)];
        tvFocusLevel.setText(focus);

        // 3. Prompt
        String[] prompts = {
                "今天不追求效率，只追求不摆烂。",
                "慢一点，也是在前进。",
                "你不是落后，只是在蓄力。",
                "保持节奏，无需慌张。",
                "允许自己休息，也是一种能力。",
                "星光不问赶路人。"
        };
        tvDailyPrompt.setText(prompts[random.nextInt(prompts.length)]);
    }
}
