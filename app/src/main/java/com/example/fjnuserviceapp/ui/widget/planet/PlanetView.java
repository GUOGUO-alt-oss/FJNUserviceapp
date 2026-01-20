package com.example.fjnuserviceapp.ui.widget.planet;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.utils.AnimationUtils;

public class PlanetView extends FrameLayout {

    private ImageView ivIcon;
    private TextView tvNumber;
    private TextView tvLabel;
    private View glowView;

    public PlanetView(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_planet, this, true);
        ivIcon = findViewById(R.id.iv_planet_icon);
        tvNumber = findViewById(R.id.tv_planet_number);
        tvLabel = findViewById(R.id.tv_planet_label);
        glowView = findViewById(R.id.view_planet_glow);
    }

    public void setIcon(int resId) {
        ivIcon.setImageResource(resId);
        ivIcon.setVisibility(VISIBLE);
        tvNumber.setVisibility(GONE);
    }

    public void setNumber(String number) {
        tvNumber.setText(number);
        tvNumber.setVisibility(VISIBLE);
        ivIcon.setVisibility(GONE);
    }

    public void setLabel(String label) {
        tvLabel.setText(label);
    }

    public void setGlowColor(int color) {
        // Simple tint or background change
        // For now keep default glass
    }

    public void startBreathing() {
        AnimationUtils.createBreathingAnimation(glowView);
    }
}
