package com.example.fjnuserviceapp.ui.life.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull; import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.base.entity.ChatMessage; import com.example.fjnuserviceapp.databinding.ItemChatBinding;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.VH> {
    private final List<ChatMessage> list;
    public ChatAdapter(List<ChatMessage> list) { this.list = list; }
    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int viewType) {
        return new VH(ItemChatBinding.inflate(LayoutInflater.from(p.getContext()), p, false));
    }
    @Override public void onBindViewHolder(@NonNull VH h, int position) {
        ChatMessage msg = list.get(position);
        if (msg.isMe()) { h.b.leftRoot.setVisibility(View.GONE); h.b.rightRoot.setVisibility(View.VISIBLE); h.b.tvRight.setText(msg.getContent()); }
        else { h.b.leftRoot.setVisibility(View.VISIBLE); h.b.rightRoot.setVisibility(View.GONE); h.b.tvLeft.setText(msg.getContent()); }
    }
    @Override public int getItemCount() { return list.size(); }
    static class VH extends RecyclerView.ViewHolder { final ItemChatBinding b; VH(ItemChatBinding b) { super(b.getRoot()); this.b = b; } }
}