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
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.FragmentCollegeNotifyBinding;
import com.example.fjnuserviceapp.ui.notify.NotificationAdapter;
import com.example.fjnuserviceapp.utils.NotificationMockData;

public class CollegeNotifyFragment extends Fragment {
    private FragmentCollegeNotifyBinding binding;
    private NotificationAdapter adapter;
    private ItemTouchHelper itemTouchHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCollegeNotifyBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.rvCollegeNotify.setLayoutManager(new LinearLayoutManager(getContext()));
        // 初始化Adapter
        adapter = new NotificationAdapter(NotificationMockData.getCollegeNotifications());

        // 新增：设置已读状态回调（解决静态方法调用错误）
        adapter.setOnNotificationStatusChangeListener(position -> {
            adapter.markAsRead(position); // Adapter实例调用，合法
        });

        binding.rvCollegeNotify.setAdapter(adapter);

        // 修改：左滑删除+右滑已读 配置
        setupSwipeToDeleteAndRead(binding.rvCollegeNotify);

        // 隐藏空/加载视图
        binding.llLoading.setVisibility(View.GONE);
        binding.llEmpty.setVisibility(View.GONE);
        binding.rvCollegeNotify.setVisibility(View.VISIBLE);
    }

    // 核心修改：左滑删除+右滑标记已读逻辑
    private void setupSwipeToDeleteAndRead(androidx.recyclerview.widget.RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) { // 支持左滑+右滑
            // 左滑删除：红色背景+删除图标
            private final ColorDrawable deleteBg = new ColorDrawable(Color.parseColor("#FF4444"));
            private final Drawable deleteIcon = ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_menu_delete);
            // 右滑已读：绿色背景+对勾图标
            private final ColorDrawable readBg = new ColorDrawable(Color.parseColor("#4CAF50"));
            private final Drawable readIcon = ContextCompat.getDrawable(requireContext(), android.R.drawable.checkbox_on_background);

            @Override
            public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView rv,
                                  @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder vh,
                                  @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder t) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    // 左滑：删除项
                    adapter.removeItem(position);
                } else if (direction == ItemTouchHelper.RIGHT) {
                    // 右滑：标记为已读
                    adapter.markAsRead(position);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull androidx.recyclerview.widget.RecyclerView rv,
                                    @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder vh, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, rv, vh, dX, dY, actionState, isCurrentlyActive);

                View itemView = vh.itemView;
                int iconSize = 48; // 统一图标大小
                int iconTop = itemView.getTop() + (itemView.getHeight() - iconSize) / 2;
                int iconBottom = iconTop + iconSize;

                // 右滑：绿色背景+对勾（标记已读）
                if (dX > 0) {
                    readBg.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + (int) dX, itemView.getBottom());
                    readBg.draw(c);

                    if (readIcon != null) {
                        int iconLeft = itemView.getLeft() + 20;
                        int iconRight = iconLeft + iconSize;
                        readIcon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        readIcon.setTint(Color.WHITE);
                        readIcon.draw(c);
                    }
                }
                // 左滑：红色背景+删除图标
                else if (dX < 0) {
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