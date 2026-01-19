package com.example.fjnuserviceapp.ui.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.app.DatePickerDialog;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Calendar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.model.CountdownEvent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddCountdownDialog extends BottomSheetDialogFragment {

    private CountdownViewModel viewModel;
    private Calendar selectedDate = Calendar.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_add_countdown, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        viewModel = new ViewModelProvider(requireParentFragment()).get(CountdownViewModel.class);

        EditText etTitle = view.findViewById(R.id.et_title);
        EditText etMotivation = view.findViewById(R.id.et_motivation);
        LinearLayout layoutDatePicker = view.findViewById(R.id.layout_date_picker);
        TextView tvSelectedDate = view.findViewById(R.id.tv_selected_date);
        Button btnAdd = view.findViewById(R.id.btn_add);

        // Default to tomorrow
        selectedDate.add(Calendar.DAY_OF_YEAR, 1);
        updateDateText(tvSelectedDate);

        layoutDatePicker.setOnClickListener(v -> {
            new DatePickerDialog(requireContext(), (view1, year, month, dayOfMonth) -> {
                selectedDate.set(year, month, dayOfMonth);
                updateDateText(tvSelectedDate);
            }, selectedDate.get(Calendar.YEAR), selectedDate.get(Calendar.MONTH),
                    selectedDate.get(Calendar.DAY_OF_MONTH)).show();
        });

        btnAdd.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            if (title.isEmpty()) {
                etTitle.setError("请输入名称");
                return;
            }

            String motivation = etMotivation.getText().toString().trim();
            if (motivation.isEmpty()) {
                motivation = "稳扎稳打，每天一点进步";
            }

            viewModel.addCountdown(title, selectedDate.getTimeInMillis(), CountdownEvent.TYPE_OTHER, motivation);
            dismiss();
        });
    }

    private void updateDateText(TextView tv) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        tv.setText(sdf.format(selectedDate.getTime()));
    }
}
