package com.example.fjnuserviceapp.ui.life.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fjnuserviceapp.base.entity.IdleItem;
import com.example.fjnuserviceapp.databinding.ItemIdleTradeBinding;

public class IdleTradeAdapter extends ListAdapter<IdleItem, IdleTradeAdapter.VH> {

    private static final DiffUtil.ItemCallback<IdleItem> CB =
            new DiffUtil.ItemCallback<IdleItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull IdleItem old, @NonNull IdleItem now) {
                    return old.getId().equals(now.getId());
                }
                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(@NonNull IdleItem old, @NonNull IdleItem now) {
                    return old.equals(now);
                }
            };

    public interface OnItemClickListener { void onClick(IdleItem item); }

    private final Context context;
    private OnItemClickListener listener;

    public IdleTradeAdapter(Context context) {
        super(CB);
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener l) { this.listener = l; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(ItemIdleTradeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        IdleItem item = getItem(position);
        holder.bind(item);
        holder.itemView.setOnClickListener(v -> { if (listener != null) listener.onClick(item); });
    }

    static class VH extends RecyclerView.ViewHolder {
        private final ItemIdleTradeBinding b;
        VH(ItemIdleTradeBinding b) { super(b.getRoot()); this.b = b; }
        void bind(IdleItem item) {
            b.tvTitle.setText(item.getTitle().isEmpty() ? "无标题" : item.getTitle());
            b.tvDesc.setText(item.getDesc().isEmpty() ? "暂无描述" : item.getDesc());
            b.tvPrice.setText("¥ " + item.getPrice());
            b.tvTime.setText(String.valueOf(item.getTime()));
        }
    }
}