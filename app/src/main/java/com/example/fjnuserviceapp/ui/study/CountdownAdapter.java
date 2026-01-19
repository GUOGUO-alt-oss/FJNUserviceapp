package com.example.fjnuserviceapp.ui.study;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.databinding.ItemCountdownBinding;
import com.example.fjnuserviceapp.model.CountdownEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CountdownAdapter extends RecyclerView.Adapter<CountdownAdapter.ViewHolder> {

    private List<CountdownEvent> list = new ArrayList<>();
    private OnItemLongClickListener longClickListener;

    public interface OnItemLongClickListener {
        void onItemLongClick(CountdownEvent event);
    }

    public CountdownAdapter(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void submitList(List<CountdownEvent> newList) {
        this.list = newList == null ? new ArrayList<>() : newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCountdownBinding binding = ItemCountdownBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                false);
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

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemCountdownBinding binding;
        private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        public ViewHolder(ItemCountdownBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(CountdownEvent item) {
            binding.tvTitle.setText(item.getTitle());
            binding.tvMotivation.setText(item.getMotivation() != null && !item.getMotivation().isEmpty()
                    ? item.getMotivation()
                    : "加油，坚持就是胜利！");
            binding.tvDays.setText(String.valueOf(item.getDaysRemaining()));

            binding.getRoot().setOnLongClickListener(v -> {
                if (longClickListener != null)
                    longClickListener.onItemLongClick(item);
                return true;
            });
        }
    }
}
