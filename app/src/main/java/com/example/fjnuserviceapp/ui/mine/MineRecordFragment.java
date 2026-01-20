package com.example.fjnuserviceapp.ui.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MineRecordFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupQuickCapture(view);
    }

    private void setupQuickCapture(View view) {
        setupButton(view.findViewById(R.id.btn_record_mind), "正念记录");
        setupButton(view.findViewById(R.id.btn_record_note), "随手记");
        setupButton(view.findViewById(R.id.btn_record_mood), "今日感受");
    }

    private void setupButton(LinearLayout btn, String type) {
        if (btn == null)
            return;
        btn.setOnClickListener(v -> {
            String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            ToastUtils.showShort(getContext(), "已记录 " + type + " at " + time);
        });
    }
}
