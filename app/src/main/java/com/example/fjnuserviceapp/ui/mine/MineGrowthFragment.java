package com.example.fjnuserviceapp.ui.mine;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.ui.widget.SkillRingView;

public class MineGrowthFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_growth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupSkills(view);
    }

    private void setupSkills(View view) {
        // Mock Skill Data initialization

        SkillRingView ring1 = view.findViewById(R.id.skill_ring_1);
        if (ring1 != null)
            ring1.setSkill(3, 0.7f, Color.parseColor("#00F2FE")); // Cyan

        SkillRingView ring2 = view.findViewById(R.id.skill_ring_2);
        if (ring2 != null)
            ring2.setSkill(2, 0.4f, Color.parseColor("#4FACFE")); // Blue

        SkillRingView ring3 = view.findViewById(R.id.skill_ring_3);
        if (ring3 != null)
            ring3.setSkill(4, 0.9f, Color.parseColor("#FF6B6B")); // Red/Orange

        SkillRingView ring4 = view.findViewById(R.id.skill_ring_4);
        if (ring4 != null)
            ring4.setSkill(5, 1.0f, Color.parseColor("#F093FB")); // Purple
    }
}
