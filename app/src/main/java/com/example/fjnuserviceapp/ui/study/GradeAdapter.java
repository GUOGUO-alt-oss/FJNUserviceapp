package com.example.fjnuserviceapp.ui.study;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.databinding.ItemGradeBinding;
import com.example.fjnuserviceapp.model.Grade;
import java.util.ArrayList;
import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradeViewHolder> {

    private List<Grade> gradeList;

    public GradeAdapter(List<Grade> gradeList) {
        this.gradeList = gradeList == null ? new ArrayList<>() : gradeList;
    }

    public void updateGrades(List<Grade> newGrades) {
        this.gradeList = newGrades == null ? new ArrayList<>() : newGrades;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public GradeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGradeBinding binding = ItemGradeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new GradeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GradeViewHolder holder, int position) {
        Grade grade = gradeList.get(position);
        holder.bind(grade);
    }

    @Override
    public int getItemCount() {
        return gradeList == null ? 0 : gradeList.size();
    }

    static class GradeViewHolder extends RecyclerView.ViewHolder {
        private final ItemGradeBinding binding;

        public GradeViewHolder(ItemGradeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Grade grade) {
            binding.tvCourseName.setText(grade.getCourseName());
            binding.tvScore.setText(String.valueOf(grade.getScore()));
            binding.tvTerm.setText(grade.getTerm());
            binding.tvCredit.setText("学分: " + grade.getCredit());
            binding.tvGpa.setText("绩点: " + grade.getGradePoint());
        }
    }
}
