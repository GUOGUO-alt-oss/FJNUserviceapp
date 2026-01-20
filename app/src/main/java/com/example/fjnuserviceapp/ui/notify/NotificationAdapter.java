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
    // 已读状态回调接口
    public interface OnNotificationStatusChangeListener {
        void onMarkAsRead(int position); // 标记已读的回调
    }

    // 回调变量
    private OnNotificationStatusChangeListener statusChangeListener;

    // 设置回调的方法
    public void setOnNotificationStatusChangeListener(OnNotificationStatusChangeListener listener) {
        this.statusChangeListener = listener;
    }

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
        holder.bind(notifications.get(position), statusChangeListener); // 传递回调
    }

    // 删除项方法
    public void removeItem(int position) {
        if (position >= 0 && position < notifications.size()) {
            notifications.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notifications.size());
        }
    }

    // 标记已读方法（外部调用）
    public void markAsRead(int position) {
        if (position >= 0 && position < notifications.size()) {
            notifications.get(position).setRead(true);
            notifyItemChanged(position); // Adapter实例调用，合法
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

        // 关键修改：添加回调参数，不再直接调用notifyItemChanged
        public void bind(Notification notification, OnNotificationStatusChangeListener listener) {
            binding.tvTitle.setText(notification.getTitle());
            binding.tvContent.setText(notification.getContent());
            binding.tvTime.setText(notification.getTime());
            binding.tvSender.setText("From: " + notification.getSender());

            // 已读未读样式区分
            if (notification.isRead()) {
                binding.tvTitle.setTextColor(Color.parseColor("#9E9E9E"));
                binding.tvTitle.setTypeface(null, android.graphics.Typeface.NORMAL);
            } else {
                binding.tvTitle.setTextColor(Color.parseColor("#FFFFFF"));
                binding.tvTitle.setTypeface(null, android.graphics.Typeface.BOLD);
            }

            // 星标逻辑
            updateStarStyle(binding.ivStarBadge, notification.getPriority());
            binding.ivStarBadge.setOnClickListener(v -> {
                int currentPriority = notification.getPriority();
                int newPriority = (currentPriority + 1) % 3;
                notification.setPriority(newPriority);
                updateStarStyle(binding.ivStarBadge, newPriority);
            });

            // 点击跳转逻辑（核心修复：删除错误的notifyItemChanged，改用回调）
            binding.getRoot().setOnClickListener(v -> {
                android.content.Context context = v.getContext();
                // 点击后标记为已读
                if (!notification.isRead()) {
                    notification.setRead(true);
                    // 关键：通过回调让Adapter处理刷新，而非直接调用
                    if (listener != null) {
                        listener.onMarkAsRead(getBindingAdapterPosition());
                    }
                }

                // 原有跳转逻辑（完全保留）
                if (notification.getType() == Notification.TYPE_COLLEGE) {
                    android.content.Intent intent = new android.content.Intent(context,
                            com.example.fjnuserviceapp.ui.notify.CollegeNotifyDetailActivity.class);
                    intent.putExtra("title", notification.getTitle());
                    intent.putExtra("content", notification.getContent());
                    intent.putExtra("sender", notification.getSender());
                    intent.putExtra("time", notification.getTime());
                    context.startActivity(intent);
                } else if (notification.getType() == Notification.TYPE_CHAT) {
                    android.content.Intent intent = new android.content.Intent(context,
                            com.example.fjnuserviceapp.ui.chat.ChatActivity.class);
                    intent.putExtra("contact_name", notification.getSender());
                    context.startActivity(intent);
                } else {
                    android.content.Intent intent = new android.content.Intent(context,
                            com.example.fjnuserviceapp.ui.notify.NoticeDetailActivity.class);
                    intent.putExtra("title", notification.getTitle());
                    context.startActivity(intent);
                }
            });
        }

        private void updateStarStyle(ImageView starView, int priority) {
            ColorFilter colorFilter;
            switch (priority) {
                case 0:
                    starView.setImageResource(android.R.drawable.btn_star_big_off);
                    colorFilter = new PorterDuffColorFilter(Color.parseColor("#9E9E9E"), PorterDuff.Mode.SRC_IN);
                    starView.setColorFilter(colorFilter);
                    break;
                case 1:
                    starView.setImageResource(android.R.drawable.btn_star_big_on);
                    colorFilter = new PorterDuffColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_IN);
                    starView.setColorFilter(colorFilter);
                    break;
                case 2:
                    starView.setImageResource(android.R.drawable.btn_star_big_on);
                    colorFilter = new PorterDuffColorFilter(Color.parseColor("#FF4444"), PorterDuff.Mode.SRC_IN);
                    starView.setColorFilter(colorFilter);
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