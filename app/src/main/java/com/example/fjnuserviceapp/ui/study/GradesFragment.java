package com.example.fjnuserviceapp.ui.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fjnuserviceapp.databinding.FragmentGradesBinding;
import com.example.fjnuserviceapp.model.Grade;
import com.example.fjnuserviceapp.utils.MockDataGenerator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class GradesFragment extends Fragment {

    private GradeAdapter adapter;
    private List<Grade> allGrades = new ArrayList<>();
    private String selectedTerm = ALL_TERMS;

    private static final String ALL_TERMS = "全部学期";
    private static final String KEY_SELECTED_TERM = "selected_term";
    private FragmentGradesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        binding = FragmentGradesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            selectedTerm = savedInstanceState.getString(KEY_SELECTED_TERM, ALL_TERMS);
        }

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GradeAdapter(new ArrayList<>());
        binding.recyclerView.setAdapter(adapter);
        allGrades = MockDataGenerator.getMockGrades();
        setupTermSpinner(allGrades);
        applyTermAndRender(selectedTerm);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SELECTED_TERM, selectedTerm);
    }

    private void setupTermSpinner(List<Grade> grades) {
        List<String> terms = buildTerms(grades);
        ArrayAdapter<String> termAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,
                terms);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTerm.setAdapter(termAdapter);

        int initialIndex = terms.indexOf(selectedTerm);
        if (initialIndex < 0) {
            selectedTerm = ALL_TERMS;
            initialIndex = 0;
        }
        binding.spinnerTerm.setSelection(initialIndex, false);

        binding.spinnerTerm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String term = (String) parent.getItemAtPosition(position);
                if (term == null)
                    term = ALL_TERMS;
                if (!term.equals(selectedTerm)) {
                    selectedTerm = term;
                    applyTermAndRender(selectedTerm);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private List<String> buildTerms(List<Grade> grades) {
        HashSet<String> termSet = new HashSet<>();
        if (grades != null) {
            for (Grade g : grades) {
                if (g != null && g.getTerm() != null && !g.getTerm().trim().isEmpty()) {
                    termSet.add(g.getTerm());
                }
            }
        }
        List<String> terms = new ArrayList<>(termSet);
        Collections.sort(terms, Collections.reverseOrder());
        terms.add(0, ALL_TERMS);
        return terms;
    }

    private void applyTermAndRender(String term) {
        List<Grade> filtered = new ArrayList<>();
        if (term == null || ALL_TERMS.equals(term)) {
            filtered.addAll(allGrades);
            binding.tvGpaTitle.setText("总平均绩点 (GPA)");
        } else {
            for (Grade g : allGrades) {
                if (g != null && term.equals(g.getTerm())) {
                    filtered.add(g);
                }
            }
            binding.tvGpaTitle.setText(term + " 平均绩点 (GPA)");
        }

        adapter.updateGrades(filtered);
        calculateAndSetStats(filtered);
    }

    private void calculateAndSetStats(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) {
            binding.tvTotalGpa.setText(String.format("%.2f", 0f));
            binding.chartView.setData(new ArrayList<>());
            return;
        }

        float totalPoints = 0;
        float totalCredits = 0;
        List<Float> gpaTrend = new ArrayList<>();
        for (Grade g : grades) {
            totalPoints += g.getGradePoint() * g.getCredit();
            totalCredits += g.getCredit();
            gpaTrend.add(g.getGradePoint());
        }

        float avgGpa = totalCredits > 0 ? totalPoints / totalCredits : 0;
        binding.tvTotalGpa.setText(String.format("%.2f", avgGpa));
        binding.chartView.setData(gpaTrend);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
