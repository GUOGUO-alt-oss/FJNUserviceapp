package com.example.fjnuserviceapp.ui.study;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.databinding.ItemCourseBinding;
import com.example.fjnuserviceapp.model.Course;
import java.util.ArrayList;
import java.util.List;

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.VH> {
    private final List<Course> data = new ArrayList<>();

    public void submitList(List<Course> list) {
        data.clear();
        if (list != null) {
            data.addAll(list);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCourseBinding binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Course c = data.get(position);
        holder.binding.tvName.setText(c.getName());
        holder.binding.tvTeacher.setText(c.getTeacher());
        holder.binding.tvTime.setText(c.getDayOfWeek() + "-" + c.getStartSection() + "-" + c.getEndSection());
        holder.binding.tvLocation.setText(c.getLocation());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final ItemCourseBinding binding;

        VH(ItemCourseBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
