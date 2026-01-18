package com.example.fjnuserviceapp.ui.study;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fjnuserviceapp.databinding.FragmentGradesBinding;
import com.example.fjnuserviceapp.model.Grade;
import com.example.fjnuserviceapp.utils.MockDataGenerator;

import java.util.ArrayList;
import java.util.List;

public class GradesFragment extends Fragment {

    private FragmentGradesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGradesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Setup RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<Grade> grades = MockDataGenerator.getMockGrades();
        GradeAdapter adapter = new GradeAdapter(grades);
        binding.recyclerView.setAdapter(adapter);

        // Calculate and Set Header Data
        calculateAndSetStats(grades);
    }

    private void calculateAndSetStats(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) return;

        float totalPoints = 0;
        float totalCredits = 0;
        List<Float> gpaTrend = new ArrayList<>();

        // Logic for total GPA and Trend (simplified)
        for (Grade g : grades) {
            totalPoints += g.getGradePoint() * g.getCredit();
            totalCredits += g.getCredit();
            gpaTrend.add(g.getGradePoint());
        }

        float avgGpa = totalCredits > 0 ? totalPoints / totalCredits : 0;
        binding.tvTotalGpa.setText(String.format("%.2f", avgGpa));

        // Set Chart Data
        binding.chartView.setData(gpaTrend);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}