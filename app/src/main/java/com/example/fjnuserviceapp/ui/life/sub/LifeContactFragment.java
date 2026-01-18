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
import com.example.fjnuserviceapp.ui.life.detail.ChatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LifeContactFragment extends Fragment {

    private static final List<Contact> contactList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_life_contact, container, false);
        initContactData();
        initView(view);
        return view;
    }

    private void initContactData() {
        Contact c1 = new Contact();
        c1.setId("2001");
        c1.setName("生活小明");
        c1.setLastMsg("出闲置教材吗？");
        c1.setLastTime(System.currentTimeMillis() - 3600000);

        Contact c2 = new Contact();
        c2.setId("2002");
        c2.setName("失物招领处");
        c2.setLastMsg("校园卡已找到");
        c2.setLastTime(System.currentTimeMillis() - 7200000);

        contactList.add(c1);
        contactList.add(c2);
    }

    private void initView(View root) {
        RecyclerView rv = root.findViewById(R.id.rvLifeContact);
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
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_life_contact, parent, false);
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

        class VH extends RecyclerView.ViewHolder {
            TextView tvName, tvLastMsg, tvTime;
            VH(View itemView) {
                super(itemView);
                tvName    = itemView.findViewById(R.id.tvLifeContactName);
                tvLastMsg = itemView.findViewById(R.id.tvLifeContactLastMsg);
                tvTime    = itemView.findViewById(R.id.tvLifeContactTime);
            }
        }
    }
}