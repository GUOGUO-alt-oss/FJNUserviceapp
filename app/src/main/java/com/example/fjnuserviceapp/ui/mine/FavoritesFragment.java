package com.example.fjnuserviceapp.ui.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.databinding.FragmentFavoritesBinding;

public class FavoritesFragment extends Fragment {
    private FragmentFavoritesBinding binding;
    private FavoritesAdapter adapter;
    private FavoritesViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. 设置标题栏透明
        binding.titleBar.setTransparent();
        binding.titleBar.setLeftOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        // 2. 列表设置
        adapter = new FavoritesAdapter();
        binding.recyclerFavorites.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerFavorites.setAdapter(adapter);

        // 3. 视差滚动
        binding.recyclerFavorites.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (binding.particleBg != null) {
                    // 传递累积滚动距离？Recycler 不太好获取 absolute scroll Y。
                    // 简单的视差：只要在动就让粒子飘动。
                    // 或者我们只需让 ParticleView 知道有滚动事件，它自己会根据时间飘动。
                    // 如果要视差，可以使用 computeVerticalScrollOffset。
                    int scrollY = recyclerView.computeVerticalScrollOffset();
                    binding.particleBg.setScrollOffset(scrollY);
                }
            }
        });

        // 4. 数据
        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        viewModel.getFavorites().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            boolean empty = list == null || list.isEmpty();
            binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);

            // 简单入场动画
            if (!empty) {
                binding.recyclerFavorites.setAlpha(0f);
                binding.recyclerFavorites.animate().alpha(1f).setDuration(500).start();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
