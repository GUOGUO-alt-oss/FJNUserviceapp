package com.example.fjnuserviceapp.ui.study;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.databinding.ItemMindfulnessBinding;
import com.example.fjnuserviceapp.model.MindfulnessRecord;
import java.util.ArrayList;
import java.util.List;

public class MindfulnessAdapter extends RecyclerView.Adapter<MindfulnessAdapter.ViewHolder> {
    private List<MindfulnessRecord> list = new ArrayList<>();

    public void submitList(List<MindfulnessRecord> newList) {
        this.list = newList == null ? new ArrayList<>() : newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMindfulnessBinding binding = ItemMindfulnessBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemMindfulnessBinding binding;

        public ViewHolder(ItemMindfulnessBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MindfulnessRecord item) {
            binding.tvType.setText(item.getType());
            binding.tvDuration.setText(item.getDurationMinutes() + " min");
        }
    }
}
