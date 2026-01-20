package com.example.fjnuserviceapp.ui.notify;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
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

    // TODO: 新增：删除项方法（供左滑调用）
    public void removeItem(int position) {
        if (position >= 0 && position < notifications.size()) {
            notifications.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notifications.size());
        }
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

            // TODO: 新增1：星标初始化（根据优先级显示样式）
            updateStarStyle(binding.ivStarBadge, notification.getPriority());

            // TODO: 新增2：星标点击切换优先级
            binding.ivStarBadge.setOnClickListener(v -> {
                int currentPriority = notification.getPriority();
                int newPriority = (currentPriority + 1) % 3; // 0→1→2→0循环
                notification.setPriority(newPriority);
                updateStarStyle(binding.ivStarBadge, newPriority);
            });

            // 原有点击事件：完全保留（确保私聊/详情能打开）
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

        // TODO: 新增3：星标样式切换（普通/重要/紧急）
        private void updateStarStyle(ImageView starView, int priority) {
            ColorFilter colorFilter;
            switch (priority) {
                case 0: // 普通（灰色空心）
                    starView.setImageResource(android.R.drawable.btn_star_big_off);
                    colorFilter = new PorterDuffColorFilter(Color.parseColor("#9E9E9E"), PorterDuff.Mode.SRC_IN);
                    starView.setColorFilter(colorFilter);
                    break;
                case 1: // 重要（黄色实心）
                    starView.setImageResource(android.R.drawable.btn_star_big_on);
                    colorFilter = new PorterDuffColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_IN);
                    starView.setColorFilter(colorFilter);
                    break;
                case 2: // 紧急（红色实心+放大）
                    starView.setImageResource(android.R.drawable.btn_star_big_on);
                    colorFilter = new PorterDuffColorFilter(Color.parseColor("#FF4444"), PorterDuff.Mode.SRC_IN);
                    starView.setColorFilter(colorFilter);
                    // 放大动画
                    ValueAnimator anim = ValueAnimator.ofFloat(1.0f, 1.2f, 1.0f);
                    anim.setDuration(300);
                    anim.setInterpolator(new AccelerateDecelerateInterpolator());
                    anim.addUpdateListener(animation -> {
                        float value = (float) animation.getAnimatedValue();
                        starView.setScaleX(value);
                        starView.setScaleY(value);
                    });
                    anim.start();
                    break;
            }
        }
    }
}