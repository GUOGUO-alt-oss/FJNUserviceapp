package com.example.fjnuserviceapp.ui.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.fjnuserviceapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddTodoDialog extends BottomSheetDialogFragment {

    private TodoViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_todo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ensure the dialog background is transparent so our custom drawable is visible
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        viewModel = new ViewModelProvider(requireParentFragment()).get(TodoViewModel.class);

        EditText etTitle = view.findViewById(R.id.et_title);
        EditText etSubtitle = view.findViewById(R.id.et_subtitle);
        RadioGroup rgDuration = view.findViewById(R.id.rg_duration);
        CheckBox cbStartNow = view.findViewById(R.id.cb_start_now);
        Button btnAdd = view.findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            if (title.isEmpty()) {
                etTitle.setError("请输入任务名称");
                return;
            }
            String subtitle = etSubtitle.getText().toString().trim();

            int duration = 25;
            int checkedId = rgDuration.getCheckedRadioButtonId();
            if (checkedId == R.id.rb_15)
                duration = 15;
            else if (checkedId == R.id.rb_45)
                duration = 45;

            boolean startNow = cbStartNow.isChecked();

            viewModel.addTodo(title, subtitle, duration, startNow);
            dismiss();
        });
    }
}
