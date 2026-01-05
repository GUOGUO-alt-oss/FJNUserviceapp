package com.example.fjnuserviceapp.ui.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.fjnuserviceapp.databinding.FragmentStudyBinding;

/**
 * 学习模块Fragment（课表/成绩）
 */
public class StudyFragment extends Fragment {
    private FragmentStudyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStudyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}