package com.example.fjnuserviceapp.ui.life;

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
import com.example.fjnuserviceapp.base.entity.IdleItem;
import com.example.fjnuserviceapp.databinding.FragmentIdleTradeBinding;
import com.example.fjnuserviceapp.ui.life.adapter.IdleTradeAdapter;
import com.example.fjnuserviceapp.ui.life.detail.IdleTradeDetailActivity;
import com.example.fjnuserviceapp.ui.life.viewmodel.LifeViewModel;

public class IdleTradeFragment extends Fragment {

    private FragmentIdleTradeBinding binding;
    private LifeViewModel viewModel;
    private IdleTradeAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentIdleTradeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initViewModel();

        // 新增：闲置发布入口
        binding.btnPublish.setOnClickListener(v ->
                startActivity(new Intent(getContext(), com.example.fjnuserviceapp.ui.life.IdlePublishActivity.class)));
    }

    private void initView() {
        binding.rvIdle.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new IdleTradeAdapter(getContext());
        binding.rvIdle.setAdapter(adapter);
        adapter.setOnItemClickListener(item -> {
            if (getContext() == null || item == null) return;
            Intent intent = new Intent(getContext(), IdleTradeDetailActivity.class);
            intent.putExtra("id", item.getId());
            intent.putExtra("title", item.getTitle());
            intent.putExtra("desc", item.getDesc());
            intent.putExtra("price", item.getPrice());
            intent.putExtra("contact", item.getContact());
            intent.putExtra("time", item.getTime());
            startActivity(intent);
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LifeViewModel.class);
        viewModel.getIdleItems().observe(getViewLifecycleOwner(), list -> adapter.submitList(list));
        viewModel.loadIdleItems();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}