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
    private ItemTouchHelper itemTouchHelper; // TODO: 新增

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
        // Use Mock Data
        adapter = new NotificationAdapter(NotificationMockData.getCollegeNotifications());
        binding.rvCollegeNotify.setAdapter(adapter);

        // TODO: 新增：左滑删除配置
        setupSwipeToDelete(binding.rvCollegeNotify);

        // Hide empty/loading views as we have mock data
        binding.llLoading.setVisibility(View.GONE);
        binding.llEmpty.setVisibility(View.GONE);
        binding.rvCollegeNotify.setVisibility(View.VISIBLE);
    }

    // TODO: 新增：左滑删除逻辑（和MessageCenter一致）
    private void setupSwipeToDelete(androidx.recyclerview.widget.RecyclerView recyclerView) {
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            private final ColorDrawable deleteBg = new ColorDrawable(Color.parseColor("#FF4444"));
            private final Drawable deleteIcon = ContextCompat.getDrawable(requireContext(), android.R.drawable.ic_menu_delete);

            @Override
            public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView rv,
                                  @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder vh,
                                  @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder t) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                adapter.removeItem(position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull androidx.recyclerview.widget.RecyclerView rv,
                                    @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder vh, float dX, float dY,
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