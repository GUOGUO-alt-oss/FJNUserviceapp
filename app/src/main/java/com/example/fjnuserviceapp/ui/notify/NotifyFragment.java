package com.example.fjnuserviceapp.ui.notify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.fjnuserviceapp.databinding.FragmentNotifyBinding;

/**
 * 通知模块Fragment
 */
public class NotifyFragment extends Fragment {
    private FragmentNotifyBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNotifyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}