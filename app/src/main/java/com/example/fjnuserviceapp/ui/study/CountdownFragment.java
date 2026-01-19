package com.example.fjnuserviceapp.ui.study;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fjnuserviceapp.databinding.FragmentCountdownBinding;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import java.util.List;
import com.example.fjnuserviceapp.model.CountdownEvent;

public class CountdownFragment extends Fragment {

    private FragmentCountdownBinding binding;
    private CountdownViewModel viewModel;
    private CountdownAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentCountdownBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CountdownViewModel.class);

        setupRecyclerView();
        setupObservers();
        setupListeners();

        // Ensure mocks are there if needed
        viewModel.checkAndInitMocks();
    }

    private void setupRecyclerView() {
        adapter = new CountdownAdapter(item -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("删除倒计时")
                    .setMessage("确定要删除 " + item.getTitle() + " 吗？")
                    .setPositiveButton("删除", (d, w) -> viewModel.deleteCountdown(item))
                    .setNegativeButton("取消", null)
                    .show();
        });
        binding.recyclerCountdowns.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerCountdowns.setAdapter(adapter);
    }

    private void setupObservers() {
        viewModel.getAllCountdowns().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            updateTopPreview(list);
        });
    }

    private void updateTopPreview(List<CountdownEvent> list) {
        if (list == null || list.isEmpty()) {
            binding.tvPreview1.setText("暂无倒计时，快去添加吧");
            binding.tvPreview2.setVisibility(View.GONE);
            return;
        }

        binding.tvPreview2.setVisibility(View.VISIBLE);

        // Preview 1 (Top 1)
        CountdownEvent e1 = list.get(0);
        String text1 = "🎯 今天离 <font color='#FF9100'><b>" + e1.getTitle() + "</b></font> 还有 <b>" + e1.getDaysRemaining()
                + "</b> 天";
        binding.tvPreview1.setText(Html.fromHtml(text1, Html.FROM_HTML_MODE_LEGACY));

        // Preview 2 (Top 2)
        if (list.size() > 1) {
            CountdownEvent e2 = list.get(1);
            String text2 = "📚 离 <font color='#00F2FE'><b>" + e2.getTitle() + "</b></font> 还有 <b>"
                    + e2.getDaysRemaining() + "</b> 天";
            binding.tvPreview2.setText(Html.fromHtml(text2, Html.FROM_HTML_MODE_LEGACY));
            binding.tvPreview2.setVisibility(View.VISIBLE);
        } else {
            binding.tvPreview2.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        binding.fabAdd.setOnClickListener(v -> {
            new AddCountdownDialog().show(getChildFragmentManager(), "AddCountdownDialog");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
