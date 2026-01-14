package com.example.fjnuserviceapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.base.entity.BaseMessage;
import com.example.fjnuserviceapp.ui.notify.CollegeNotifyDetailActivity;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CollegeNotifyAdapter extends RecyclerView.Adapter<CollegeNotifyAdapter.ViewHolder> {
    private Context context;
    private List<BaseMessage> notifyList;
    // 新增：点击监听
    private OnItemClickListener onItemClickListener;

    // 构造方法
    public CollegeNotifyAdapter(Context context, List<BaseMessage> notifyList) {
        this.context = context;
        this.notifyList = notifyList;
    }

    // 新增：设置点击监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_college_notify, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaseMessage message = notifyList.get(position);
        holder.tvTitle.setText(message.getTitle());
        holder.tvContent.setText(message.getContent());
        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        holder.tvTime.setText(sdf.format(message.getTime()));

        // 新增：条目点击事件（跳转到详情页）
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(message);
            }
            // 跳转到详情页


        });
    }

    @Override
    public int getItemCount() {
        return notifyList == null ? 0 : notifyList.size();
    }

    // 更新数据
    public void updateData(List<BaseMessage> newList) {
        this.notifyList = newList;
        notifyDataSetChanged();
    }

    // 新增：点击监听接口
    public interface OnItemClickListener {
        void onItemClick(BaseMessage message);
    }

    // ViewHolder类
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
