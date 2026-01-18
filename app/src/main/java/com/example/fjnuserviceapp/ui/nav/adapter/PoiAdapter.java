package com.example.fjnuserviceapp.ui.nav.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.ui.nav.model.PoiInfo;

import java.util.List;

/**
 * POI（兴趣点）列表适配器
 */
public class PoiAdapter extends RecyclerView.Adapter<PoiAdapter.PoiViewHolder> {

    private List<PoiInfo> poiList;
    private OnPoiClickListener onPoiClickListener;

    public PoiAdapter() {
    }

    public PoiAdapter(List<PoiInfo> poiList) {
        this.poiList = poiList;
    }

    @NonNull
    @Override
    public PoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poi, parent, false);
        return new PoiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PoiViewHolder holder, int position) {
        if (poiList != null && position < poiList.size()) {
            PoiInfo poiInfo = poiList.get(position);
            holder.bind(poiInfo);
        }
    }

    @Override
    public int getItemCount() {
        return poiList != null ? poiList.size() : 0;
    }

    public void setPoiList(List<PoiInfo> poiList) {
        this.poiList = poiList;
        notifyDataSetChanged();
    }

    public void setOnPoiClickListener(OnPoiClickListener onPoiClickListener) {
        this.onPoiClickListener = onPoiClickListener;
    }

    /**
     * POI点击监听器接口
     */
    public interface OnPoiClickListener {
        void onPoiClick(PoiInfo poiInfo);
    }

    /**
     * POI列表项ViewHolder
     */
    class PoiViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvPoiName;
        private final TextView tvPoiAddress;
        private final TextView tvPoiType;

        public PoiViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPoiName = itemView.findViewById(R.id.tv_poi_name);
            tvPoiAddress = itemView.findViewById(R.id.tv_poi_address);
            tvPoiType = itemView.findViewById(R.id.tv_poi_type);

            // 设置点击事件
            itemView.setOnClickListener(v -> {
                if (onPoiClickListener != null && poiList != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onPoiClickListener.onPoiClick(poiList.get(position));
                    }
                }
            });
        }

        public void bind(PoiInfo poiInfo) {
            if (poiInfo != null) {
                tvPoiName.setText(poiInfo.getName());
                tvPoiAddress.setText(poiInfo.getAddress());
                tvPoiType.setText(poiInfo.getType());
            }
        }
    }
}