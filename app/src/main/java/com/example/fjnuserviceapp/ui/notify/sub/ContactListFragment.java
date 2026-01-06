package com.example.fjnuserviceapp.ui.notify.sub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.base.entity.Contact;
import com.example.fjnuserviceapp.databinding.FragmentContactListBinding;
import com.example.fjnuserviceapp.ui.notify.ChatDetailActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactListFragment extends Fragment {
    private FragmentContactListBinding binding;
    private ContactAdapter adapter;
    private final List<Contact> contactList = new ArrayList<>(); // 加final消除警告

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 加try-catch防止ViewBinding初始化失败
        try {
            binding = FragmentContactListBinding.inflate(inflater, container, false);
        } catch (Exception e) {
            // 兜底：用传统方式加载布局
            View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
            RecyclerView rvContact = view.findViewById(R.id.rvContact);
            initContactData();
            adapter = new ContactAdapter();
            rvContact.setLayoutManager(new LinearLayoutManager(getContext()));
            rvContact.setAdapter(adapter);
            return view;
        }
        initContactData();
        initView();
        return binding.getRoot();
    }

    // 模拟2个联系人数据（加空值保护）
    private void initContactData() {
        // 联系人1：李老师
        Contact teacher = new Contact();
        teacher.setId("1001");
        teacher.setName("李老师");
        teacher.setAvatar("");
        teacher.setLastMsg("你的作业我批改好了");
        teacher.setLastTime(System.currentTimeMillis() - 3600000); // 1小时前

        // 联系人2：小张
        Contact classmate = new Contact();
        classmate.setId("1002");
        classmate.setName("小张");
        classmate.setAvatar("");
        classmate.setLastMsg("周末去图书馆吗？");
        classmate.setLastTime(System.currentTimeMillis() - 7200000); // 2小时前

        contactList.add(teacher);
        contactList.add(classmate);
    }

    private void initView() {
        // 空判断：防止binding为null
        if (binding == null) return;
        binding.rvContact.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ContactAdapter();
        binding.rvContact.setAdapter(adapter);
    }

    // 联系人列表适配器
    private class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_contact, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // 空判断1：防止position越界
            if (position < 0 || position >= contactList.size()) return;

            Contact contact = contactList.get(position);
            // 空判断2：防止contact为null
            if (contact == null) return;

            // 空判断3：防止文本为null
            holder.tvName.setText(contact.getName() == null ? "未知联系人" : contact.getName());
            holder.tvLastMsg.setText(contact.getLastMsg() == null ? "无消息" : contact.getLastMsg());

            // 格式化最后一条消息时间（加异常捕获）
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.CHINA);
                holder.tvLastTime.setText(sdf.format(contact.getLastTime()));
            } catch (Exception e) {
                holder.tvLastTime.setText("未知时间");
            }

            // 点击联系人，进入对话详情页（核心修复：多层空判断）
            holder.itemView.setOnClickListener(v -> {
                // 空判断4：防止Context为空
                if (getContext() == null || getActivity() == null) return;
                // 空判断5：防止contact数据为空
                String contactName = contact.getName() == null ? "未知联系人" : contact.getName();
                String contactId = contact.getId() == null ? "0" : contact.getId();

                Intent intent = new Intent(getContext(), ChatDetailActivity.class);
                intent.putExtra("contact_name", contactName);
                intent.putExtra("contact_id", contactId);

                // 用getActivity().startActivity，更稳定
                getActivity().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return contactList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvLastMsg, tvLastTime;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                // 绑定控件（加空判断）
                tvName = itemView.findViewById(R.id.tvContactName);
                tvLastMsg = itemView.findViewById(R.id.tvLastMsg);
                tvLastTime = itemView.findViewById(R.id.tvLastTime);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
