package com.example.fjnuserviceapp.ui.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fjnuserviceapp.databinding.FragmentTodoBinding;
import com.example.fjnuserviceapp.model.TodoItem;

public class TodoFragment extends Fragment {

    private FragmentTodoBinding binding;
    private TodoViewModel viewModel;
    private TodoAdapter pendingAdapter;
    private TodoAdapter doneAdapter;
    private boolean isDoneExpanded = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentTodoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TodoViewModel.class);

        setupAdapters();
        setupObservers();
        setupListeners();
    }

    private void setupAdapters() {
        // Pending List
        pendingAdapter = new TodoAdapter(false, todo -> {
            // Click Pending -> Start (Active)
            viewModel.startTodo(todo);
        });
        binding.recyclerPending.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerPending.setAdapter(pendingAdapter);

        // Done List
        doneAdapter = new TodoAdapter(true, null); // Done items no click action
        binding.recyclerDone.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerDone.setAdapter(doneAdapter);
    }

    private void setupObservers() {
        // 1. Pending List
        viewModel.getPendingTodos().observe(getViewLifecycleOwner(), list -> {
            pendingAdapter.submitList(list);
        });

        // 2. Done List
        viewModel.getDoneTodos().observe(getViewLifecycleOwner(), list -> {
            doneAdapter.submitList(list);
            int count = list == null ? 0 : list.size();
            binding.tvDoneCount.setText("已完成 (" + count + ")");
        });

        // 3. Active Todo
        viewModel.getActiveTodo().observe(getViewLifecycleOwner(), todo -> {
            if (todo != null) {
                binding.cardActiveTodo.setVisibility(View.VISIBLE);
                binding.tvActiveTitle.setText(todo.getTitle());
                binding.tvActiveSubtitle.setText(todo.getSubtitle());

                // Progress Logic (Mock)
                int progress = (int) ((float) todo.getCompletedDuration() / todo.getTotalDuration() * 100);
                binding.progressActive.setProgress(progress);
                binding.tvActiveTime.setText(todo.getCompletedDuration() + " / " + todo.getTotalDuration() + " min");

                // Actions
                binding.btnPause.setOnClickListener(v -> viewModel.pauseTodo(todo));
                binding.btnFinish.setOnClickListener(v -> viewModel.completeTodo(todo));
            } else {
                binding.cardActiveTodo.setVisibility(View.GONE);
            }
        });

        // 4. Status Stats
        viewModel.getTodayFocusMinutes().observe(getViewLifecycleOwner(),
                min -> binding.tvFocusMinutes.setText(String.valueOf(min)));

        viewModel.getConsecutiveDays().observe(getViewLifecycleOwner(),
                days -> binding.tvConsecutiveDays.setText(String.valueOf(days)));
    }

    private void setupListeners() {
        // Expand/Collapse Done List
        binding.layoutDoneHeader.setOnClickListener(v -> {
            isDoneExpanded = !isDoneExpanded;
            binding.recyclerDone.setVisibility(isDoneExpanded ? View.VISIBLE : View.GONE);
            binding.ivDoneArrow.setRotation(isDoneExpanded ? 90 : 0);
        });

        // FAB -> Add Todo
        binding.fabAddTodo.setOnClickListener(v -> {
            new AddTodoDialog().show(getChildFragmentManager(), "AddTodoDialog");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
