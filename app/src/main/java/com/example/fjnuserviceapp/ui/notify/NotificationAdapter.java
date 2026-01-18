package com.example.fjnuserviceapp.ui.notify;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.databinding.ItemNotificationCardBinding;
import com.example.fjnuserviceapp.model.Notification;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNotificationCardBinding binding = ItemNotificationCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications == null ? 0 : notifications.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        private final ItemNotificationCardBinding binding;

        public NotificationViewHolder(ItemNotificationCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Notification notification) {
            binding.tvTitle.setText(notification.getTitle());
            binding.tvContent.setText(notification.getContent());
            binding.tvTime.setText(notification.getTime());
            binding.tvSender.setText("From: " + notification.getSender());

            // 点击事件跳转
            binding.getRoot().setOnClickListener(v -> {
                android.content.Context context = v.getContext();
                if (notification.getType() == Notification.TYPE_COLLEGE) {
                    // 学院通知 -> 详情页
                    android.content.Intent intent = new android.content.Intent(context,
                            com.example.fjnuserviceapp.ui.notify.CollegeNotifyDetailActivity.class);
                    intent.putExtra("title", notification.getTitle());
                    intent.putExtra("content", notification.getContent());
                    intent.putExtra("sender", notification.getSender());
                    intent.putExtra("time", notification.getTime());
                    context.startActivity(intent);
                } else if (notification.getType() == Notification.TYPE_CHAT) {
                    // 私信 -> 聊天页面 (ChatActivity)
                    android.content.Intent intent = new android.content.Intent(context,
                            com.example.fjnuserviceapp.ui.chat.ChatActivity.class);
                    // 传递联系人名字 (辅导员、张三、李四)
                    intent.putExtra("contact_name", notification.getSender());
                    context.startActivity(intent);
                } else {
                    // 默认 -> 详情页
                    android.content.Intent intent = new android.content.Intent(context,
                            com.example.fjnuserviceapp.ui.notify.NoticeDetailActivity.class);
                    intent.putExtra("title", notification.getTitle());
                    context.startActivity(intent);
                }
            });
        }
    }
}