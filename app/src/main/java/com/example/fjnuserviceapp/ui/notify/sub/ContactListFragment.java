package com.example.fjnuserviceapp.ui.notify.sub;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.FragmentContactListBinding;
import com.example.fjnuserviceapp.ui.notify.NotificationAdapter;
import com.example.fjnuserviceapp.utils.NotificationMockData;

public class ContactListFragment extends Fragment {
    private FragmentContactListBinding binding;
    private NotificationAdapter adapter;
    private ItemTouchHelper itemTouchHelper; // TODO: 新增

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            binding = FragmentContactListBinding.inflate(inflater, container, false);
            return binding.getRoot();
        } catch (Exception e) {
            // Fallback
            View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
            RecyclerView rvContact = view.findViewById(R.id.rvContact);
            rvContact.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new NotificationAdapter(NotificationMockData.getChatMessages());
            rvContact.setAdapter(adapter);
            // TODO: 新增：Fallback分支也加左滑删除
            setupSwipeToDelete(rvContact);
            return view;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (binding != null) {
            binding.rvContact.setLayoutManager(new LinearLayoutManager(getContext()));

            // 使用自定义的 NotificationAdapter 来展示联系人列表
            // 点击联系人时跳转到 ChatActivity
            adapter = new NotificationAdapter(NotificationMockData.getChatMessages());

            // TODO: 新增：左滑删除配置
            setupSwipeToDelete(binding.rvContact);

            binding.rvContact.setAdapter(adapter);

            // 新增：悬浮按钮，快速发起私信
            // (如果布局里有 fab 的话，这里可以添加逻辑)
        }
    }

    // TODO: 新增：左滑删除逻辑
    private void setupSwipeToDelete(RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            private final ColorDrawable deleteBg = new ColorDrawable(Color.parseColor("#FF4444"));
            private final Drawable deleteIcon = ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_menu_delete);

            @Override
            public boolean onMove(@NonNull RecyclerView rv,
                                  @NonNull RecyclerView.ViewHolder vh,
                                  @NonNull RecyclerView.ViewHolder t) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                adapter.removeItem(position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView rv,
                                    @NonNull RecyclerView.ViewHolder vh, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, rv, vh, dX, dY, actionState, isCurrentlyActive);

                View itemView = vh.itemView;
                int iconSize = deleteIcon != null ? deleteIcon.getIntrinsicWidth() : 48;
                int iconTop = itemView.getTop() + (itemView.getHeight() - iconSize) / 2;
                int iconBottom = iconTop + iconSize;

                if (dX < 0) {
                    deleteBg.setBounds(itemView.getRight() + (int) dX, itemView.getTop(),
                            itemView.getRight(), itemView.getBottom());
                    deleteBg.draw(c);

                    if (deleteIcon != null) {
                        int iconRight = itemView.getRight() - 20;
                        int iconLeft = iconRight - iconSize;
                        deleteIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        deleteIcon.setTint(Color.WHITE);
                        deleteIcon.draw(c);
                    }
                }
            }
        };

        itemTouchHelper = new ItemTouchHelper(swipeCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}