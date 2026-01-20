package com.example.fjnuserviceapp.ui.notify.sub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fjnuserviceapp.databinding.FragmentCollegeNotifyBinding;
import com.example.fjnuserviceapp.ui.notify.NotificationAdapter;
import com.example.fjnuserviceapp.utils.NotificationMockData;

public class CollegeNotifyFragment extends Fragment {
    private FragmentCollegeNotifyBinding binding;
    private NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCollegeNotifyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        binding.rvCollegeNotify.setLayoutManager(new LinearLayoutManager(getContext()));
        // Use Mock Data
        adapter = new NotificationAdapter(NotificationMockData.getCollegeNotifications());
        binding.rvCollegeNotify.setAdapter(adapter);
        
        // Hide empty/loading views as we have mock data
        binding.llLoading.setVisibility(View.GONE);
        binding.llEmpty.setVisibility(View.GONE);
        binding.rvCollegeNotify.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}