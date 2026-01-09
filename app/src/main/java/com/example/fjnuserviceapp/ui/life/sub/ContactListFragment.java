package com.example.fjnuserviceapp.ui.life.sub;

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
import com.example.fjnuserviceapp.ui.life.ChatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ContactListFragment extends Fragment {

    private static final List<Contact> contactList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        initContactData();
        initView(view);
        return view;
    }

    private void initContactData() {
        Contact teacher = new Contact();
        teacher.setId("1001");
        teacher.setName("李老师");
        teacher.setLastMsg("你的作业我批改好了");
        teacher.setLastTime(System.currentTimeMillis() - 3600000);

        Contact classmate = new Contact();
        classmate.setId("1002");
        classmate.setName("小张");
        classmate.setLastMsg("周末去图书馆吗？");
        classmate.setLastTime(System.currentTimeMillis() - 7200000);

        contactList.add(teacher);
        contactList.add(classmate);
    }

    private void initView(View root) {
        RecyclerView rv = root.findViewById(R.id.rvContact);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        ContactAdapter adapter = new ContactAdapter();
        rv.setAdapter(adapter);

        adapter.setOnItemClickListener(contact -> {
            if (getContext() == null || contact == null) return;
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("contact_name", contact.getName());
            getActivity().startActivity(intent);
        });
    }

    /* ---------------- 内部 Adapter ---------------- */
    private static class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.VH> {

        public interface OnItemClickListener { void onClick(Contact contact); }
        private OnItemClickListener listener;
        void setOnItemClickListener(OnItemClickListener l) { listener = l; }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            Contact c = contactList.get(position);
            holder.tvName.setText(c.getName());
            holder.tvLastMsg.setText(c.getLastMsg());
            holder.tvTime.setText(new SimpleDateFormat("HH:mm", Locale.CHINA).format(c.getLastTime()));
            holder.itemView.setOnClickListener(v -> { if (listener != null) listener.onClick(c); });
        }

        @Override
        public int getItemCount() { return contactList.size(); }

        class VH extends RecyclerView.ViewHolder {      // ← 已去掉 static
            TextView tvName, tvLastMsg, tvTime;
            VH(View itemView) {
                super(itemView);
                tvName    = itemView.findViewById(R.id.tvContactName);
                tvLastMsg = itemView.findViewById(R.id.tvLastMsg);
                tvTime    = itemView.findViewById(R.id.tvLastTime);
            }
        }
    }
}