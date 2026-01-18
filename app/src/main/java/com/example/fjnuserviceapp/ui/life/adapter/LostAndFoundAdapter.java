package com.example.fjnuserviceapp.ui.life.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fjnuserviceapp.base.entity.LostAndFound;
import com.example.fjnuserviceapp.databinding.ItemLostAndFoundBinding;

public class LostAndFoundAdapter extends ListAdapter<LostAndFound, LostAndFoundAdapter.VH> {

    private static final DiffUtil.ItemCallback<LostAndFound> CB =
            new DiffUtil.ItemCallback<LostAndFound>() {
                @Override
                public boolean areItemsTheSame(@NonNull LostAndFound old,
                                               @NonNull LostAndFound now) {
                    return old.getId().equals(now.getId());
                }
                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull LostAndFound old,
                                                  @NonNull LostAndFound now) {
                    return old.equals(now);
                }
            };

    public interface OnItemClickListener {
        void onClick(LostAndFound item);
    }

    private final Context context;
    private OnItemClickListener listener;

    public LostAndFoundAdapter(Context context) {
        super(CB);
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener l) { this.listener = l; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemLostAndFoundBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        LostAndFound item = getItem(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(item);
        });
    }

    static class VH extends RecyclerView.ViewHolder {
        private final ItemLostAndFoundBinding b;
        VH(ItemLostAndFoundBinding b) { super(b.getRoot()); this.b = b; }
        void bind(LostAndFound item) {
            b.tvTitle.setText(item.getTitle().isEmpty() ? "无标题" : item.getTitle());
            b.tvDesc.setText(item.getDesc().isEmpty() ? "暂无描述" : item.getDesc());
            b.tvTime.setText(String.valueOf(item.getTime()));
        }
    }
}