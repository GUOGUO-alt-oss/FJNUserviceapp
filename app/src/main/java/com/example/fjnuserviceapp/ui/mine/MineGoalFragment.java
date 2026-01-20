package com.example.fjnuserviceapp.ui.mine;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.utils.ToastUtils;

public class MineGoalFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_goal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupInteractions(view);
    }

    private void setupInteractions(View view) {
        // 1. Goal Toggle
        SwitchCompat goalSwitch = view.findViewById(R.id.switch_goal_active);
        if (goalSwitch != null) {
            goalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    ToastUtils.showShort(getContext(), "目标激活：正在执行");
                } else {
                    ToastUtils.showShort(getContext(), "目标冻结");
                }
            });
        }

        // 2. Weekly Tasks (Delayed Check)
        setupDelayedCheckbox(view.findViewById(R.id.cb_task_1));
        setupDelayedCheckbox(view.findViewById(R.id.cb_task_2));
        setupDelayedCheckbox(view.findViewById(R.id.cb_task_3));

        // 3. Review Buttons
        setupReviewButton(view.findViewById(R.id.btn_review_smooth), "继续保持节奏");
        setupReviewButton(view.findViewById(R.id.btn_review_stuck), "尝试拆解问题");
        setupReviewButton(view.findViewById(R.id.btn_review_off), "目标没变，只是路径需要调整");

        // 4. Milestone Strike-through
        TextView milestoneDone = view.findViewById(R.id.tv_milestone_done);
        if (milestoneDone != null) {
            milestoneDone.setPaintFlags(milestoneDone.getPaintFlags() | android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void setupDelayedCheckbox(CheckBox cb) {
        if (cb == null)
            return;
        cb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Change color to Cyan when checked
                cb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#00F2FE")));
                ToastUtils.showShort(getContext(), "你履行了承诺。");
            } else {
                cb.setButtonTintList(ColorStateList.valueOf(Color.parseColor("#80FFFFFF")));
            }
        });
    }

    private void setupReviewButton(TextView btn, String hint) {
        if (btn == null)
            return;
        btn.setOnClickListener(v -> {
            TextView hintView = getView().findViewById(R.id.tv_review_hint);
            if (hintView != null)
                hintView.setText(hint);
            // Visual feedback
            v.setAlpha(0.5f);
            v.animate().alpha(1f).setDuration(300).start();
        });
    }
}
