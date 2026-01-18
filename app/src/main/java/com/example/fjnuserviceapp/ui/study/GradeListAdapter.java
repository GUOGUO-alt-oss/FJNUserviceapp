package com.example.fjnuserviceapp.ui.study;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.databinding.ItemGradeBinding;
import com.example.fjnuserviceapp.model.Grade;
import java.util.ArrayList;
import java.util.List;

public class GradeListAdapter extends RecyclerView.Adapter<GradeListAdapter.VH> {
    private final List<Grade> data = new ArrayList<>();

    public void submitList(List<Grade> list) {
        data.clear();
        if (list != null) {
            data.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGradeBinding binding = ItemGradeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Grade g = data.get(position);
        holder.binding.tvCourseName.setText(g.getCourseName());
        holder.binding.tvCredit.setText("学分: " + g.getCredit());
        holder.binding.tvScore.setText(String.valueOf(g.getScore()));
        holder.binding.tvGpa.setText(String.format("%.1f", g.getGradePoint()));

        // 成绩颜色逻辑
        try {
            double score = g.getScore();
            if (score >= 90) {
                holder.binding.tvScore.setTextColor(Color.parseColor("#4CAF50")); // 绿色
            } else if (score >= 60) {
                holder.binding.tvScore.setTextColor(Color.parseColor("#2196F3")); // 蓝色
            } else {
                holder.binding.tvScore.setTextColor(Color.parseColor("#F44336")); // 红色
            }
        } catch (NumberFormatException e) {
            holder.binding.tvScore.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemGradeBinding binding;

        VH(ItemGradeBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
