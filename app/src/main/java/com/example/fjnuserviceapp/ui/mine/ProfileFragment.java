package com.example.fjnuserviceapp.ui.mine;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.fjnuserviceapp.databinding.FragmentProfileBinding;
import com.example.fjnuserviceapp.model.UserProfile;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel viewModel;
    private UserProfile currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        
        // 标题栏返回
        binding.btnBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        // 保存按钮
        binding.btnSave.setOnClickListener(v -> saveProfile());

        // 头像编辑 (模拟)
        binding.btnEditAvatar.setOnClickListener(v -> {
            Toast.makeText(getContext(), "模拟打开相册选择图片", Toast.LENGTH_SHORT).show();
        });

        // 数据绑定
        viewModel.getUserProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                currentUser = profile;
                // 仅在首次加载或数据确实变更时更新，避免输入时被覆盖
                // 这里简单处理：如果输入框为空，则填充
                if (binding.etName.getText().length() == 0) binding.etName.setText(profile.getName());
                if (binding.etSignature.getText().length() == 0) binding.etSignature.setText(profile.getSignature());
                if (binding.etPhone.getText().length() == 0) binding.etPhone.setText(profile.getPhone());
                
                binding.tvStudentId.setText(profile.getStudentId());
                binding.tvDeptMajor.setText(profile.getDepartment() + " | " + profile.getMajor());
            }
        });
    }

    private void saveProfile() {
        if (currentUser == null) return;

        String name = binding.etName.getText().toString().trim();
        String signature = binding.etSignature.getText().toString().trim();
        String phone = binding.etPhone.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setName(name);
        currentUser.setSignature(signature);
        currentUser.setPhone(phone);

        viewModel.updateUser(currentUser);
        Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
        getParentFragmentManager().popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}