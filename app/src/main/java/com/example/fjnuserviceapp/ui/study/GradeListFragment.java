package com.example.fjnuserviceapp.ui.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.fjnuserviceapp.databinding.FragmentGradeListBinding;
import com.example.fjnuserviceapp.model.Grade;
import java.util.List;

public class GradeListFragment extends Fragment {
    private FragmentGradeListBinding binding;
    private GradeListAdapter adapter;
    private GradeViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGradeListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new GradeListAdapter();
        binding.recyclerGrades.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerGrades.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(GradeViewModel.class);

        setupFilters();

        viewModel.getGrades().observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            boolean empty = list == null || list.isEmpty();
            binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            updateGpa(list);
        });
    }

    private void updateGpa(List<Grade> list) {
        if (list == null || list.isEmpty()) {
            // binding.tvTotalGpa.setText("GPA: 0.0");
            return;
        }
        float totalPoint = 0;
        float totalCredit = 0;
        for (Grade g : list) {
            try {
                // 如果分数是数字且及格
                float s = g.getScore();
                if (s >= 60) {
                    totalPoint += g.getGradePoint() * g.getCredit();
                    totalCredit += g.getCredit();
                }
            } catch (Exception e) {
                // ignore
            }
        }
        float gpa = totalCredit > 0 ? totalPoint / totalCredit : 0;
        // 假设有一个显示 GPA 的 TextView，例如 binding.tvGpa
        // 这里只是为了演示，如果没有对应的 View 可以忽略
    }

    private void setupFilters() {
        String[] terms = { "全部学期", "2024-2025(1)", "2023-2024(2)" };
        ArrayAdapter<String> termAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                terms);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTerm.setAdapter(termAdapter);

        // 排序筛选器 (暂时保留UI但ViewModel未实现排序，可后续添加)
        String[] sorts = { "默认排序", "成绩降序", "成绩升序" };
        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                sorts);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSort.setAdapter(sortAdapter);

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent == binding.spinnerTerm) {
                    String term = (String) binding.spinnerTerm.getSelectedItem();
                    viewModel.setTerm(term);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };

        binding.spinnerTerm.setOnItemSelectedListener(listener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
