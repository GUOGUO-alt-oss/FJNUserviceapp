package com.example.fjnuserviceapp.ui.study;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.fjnuserviceapp.databinding.FragmentCourseEditBinding;
import com.example.fjnuserviceapp.model.Course;
import com.example.fjnuserviceapp.utils.ToastUtils;

public class CourseEditFragment extends Fragment {
    private FragmentCourseEditBinding binding;
    private CourseEditViewModel editViewModel;
    private StudyViewModel studyViewModel;
    private ActivityResultLauncher<String[]> picker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCourseEditBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 标题栏
        binding.titleBar.setLeftOnClickListener(v -> {
            requireParentFragment().getChildFragmentManager().popBackStack();
        });

        editViewModel = new ViewModelProvider(this).get(CourseEditViewModel.class);
        studyViewModel = new ViewModelProvider(requireParentFragment()).get(StudyViewModel.class);
        
        // 观察解析结果
        editViewModel.getName().observe(getViewLifecycleOwner(), v -> binding.etName.setText(v));
        editViewModel.getTeacher().observe(getViewLifecycleOwner(), v -> binding.etTeacher.setText(v));
        editViewModel.getTime().observe(getViewLifecycleOwner(), v -> binding.etTime.setText(v));
        editViewModel.getLocation().observe(getViewLifecycleOwner(), v -> binding.etLocation.setText(v));

        binding.btnSubmit.setOnClickListener(v -> {
            String name = getText(binding.etName);
            String teacher = getText(binding.etTeacher);
            String time = getText(binding.etTime);
            String location = getText(binding.etLocation);

            if (name.isEmpty()) {
                ToastUtils.showShort(getContext(), "课程名称不能为空");
                return;
            }

            editViewModel.setName(name);
            editViewModel.setTeacher(teacher);
            editViewModel.setTime(time);
            editViewModel.setLocation(location);
            
            Course c = editViewModel.buildCourse();
            studyViewModel.addCourse(c);
            ToastUtils.showShort(getContext(), "添加成功");
            requireParentFragment().getChildFragmentManager().popBackStack();
        });

        picker = registerForActivityResult(new ActivityResultContracts.OpenDocument(), this::onPicked);
        binding.btnPickPdf.setOnClickListener(v -> {
            picker.launch(new String[]{"application/pdf", "text/plain"});
        });
    }

    private void onPicked(Uri uri) {
        if (uri == null) return;
        try {
            requireContext().getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } catch (Exception e) {
            // 忽略权限错误，部分系统/URI可能不需要持久权限
        }
        editViewModel.parseFromUri(requireContext(), uri);
        ToastUtils.showShort(getContext(), "解析完成，请核对信息");
    }

    private String getText(android.widget.EditText et) {
        CharSequence cs = et.getText();
        return cs == null ? "" : cs.toString().trim();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
