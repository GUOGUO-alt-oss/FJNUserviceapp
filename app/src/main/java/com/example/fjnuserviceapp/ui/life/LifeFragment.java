package com.example.fjnuserviceapp.ui.life;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.fjnuserviceapp.databinding.FragmentLifeBinding;

/**
 * 生活模块Fragment（失物招领/闲置）
 */
public class LifeFragment extends Fragment {
    private FragmentLifeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLifeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}