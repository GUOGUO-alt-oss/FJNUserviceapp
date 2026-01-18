package com.example.fjnuserviceapp.ui.life.sub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fjnuserviceapp.databinding.FragmentLostAndFoundBinding;
import com.example.fjnuserviceapp.ui.life.adapter.LostAndFoundAdapter;
import com.example.fjnuserviceapp.ui.life.detail.LostAndFoundDetailActivity;
import com.example.fjnuserviceapp.ui.life.detail.LostPublishActivity;
import com.example.fjnuserviceapp.ui.life.detail.LostSearchActivity;
import com.example.fjnuserviceapp.ui.life.viewmodel.LifeViewModel;

public class LostAndFoundFragment extends Fragment {

    private FragmentLostAndFoundBinding binding;
    private LifeViewModel viewModel;
    private LostAndFoundAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentLostAndFoundBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initViewModel();

        // 新增：失物发布入口
        binding.btnPublish.setOnClickListener(v ->
                startActivity(new Intent(getContext(), LostPublishActivity.class)));

        // 新增：失物查询入口
        binding.btnSearch.setOnClickListener(v ->
                startActivity(new Intent(getContext(), LostSearchActivity.class)));
    }

    private void initView() {
        binding.rvLost.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LostAndFoundAdapter(getContext());
        binding.rvLost.setAdapter(adapter);
        adapter.setOnItemClickListener(item -> {
            if (getContext() == null || item == null) return;
            Intent intent = new Intent(getContext(), LostAndFoundDetailActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("desc", item.getDesc());
            intent.putExtra("location", item.getLocation());
            intent.putExtra("contact", item.getContact());
            intent.putExtra("time", item.getTime());
            intent.putExtra("category", item.getCategory());
            startActivity(intent);
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LifeViewModel.class);
        viewModel.getLostAndFound().observe(getViewLifecycleOwner(), list -> adapter.submitList(list));
        viewModel.loadLostAndFound();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}