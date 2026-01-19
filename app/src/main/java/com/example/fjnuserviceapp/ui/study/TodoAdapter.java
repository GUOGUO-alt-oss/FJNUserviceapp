package com.example.fjnuserviceapp.ui.study;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.ItemTodoBinding;
import com.example.fjnuserviceapp.model.TodoItem;
import java.util.ArrayList;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {

    private List<TodoItem> items = new ArrayList<>();
    private final OnTodoClickListener listener;
    private final boolean isDoneList;

    public interface OnTodoClickListener {
        void onTodoClick(TodoItem todo);
    }

    public TodoAdapter(boolean isDoneList, OnTodoClickListener listener) {
        this.isDoneList = isDoneList;
        this.listener = listener;
    }

    public void submitList(List<TodoItem> newItems) {
        this.items = newItems == null ? new ArrayList<>() : newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTodoBinding binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TodoViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {
        private final ItemTodoBinding binding;

        public TodoViewHolder(ItemTodoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TodoItem item) {
            binding.tvTitle.setText(item.getTitle());
            binding.tvSubtitle.setText(item.getSubtitle());
            binding.tvDuration.setText(item.getTotalDuration() + " min");

            // Visual diff for Done items
            if (isDoneList) {
                binding.tvTitle.setAlpha(0.5f);
                binding.tvSubtitle.setAlpha(0.5f);
                binding.viewStatusDot.setBackgroundResource(R.drawable.ic_launcher_background); // Mock checkmark or
                                                                                                // just dim
                // Or better, set checkmark drawable
                // binding.viewStatusDot.setBackgroundResource(R.drawable.ic_check);
            } else {
                binding.tvTitle.setAlpha(1.0f);
                binding.tvSubtitle.setAlpha(0.8f);
                binding.viewStatusDot.setBackgroundResource(R.drawable.bg_circle_glass);
            }

            binding.getRoot().setOnClickListener(v -> {
                if (listener != null)
                    listener.onTodoClick(item);
            });
        }
    }
}
