package com.example.fjnuserviceapp.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fjnuserviceapp.R;

import java.util.List;

/**
 * 天气预报列表的适配器
 * 负责将天气数据绑定到RecyclerView的列表项
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    
    private List<WeatherData> weatherList;
    private Context context;
    
    /**
     * 构造函数
     * 
     * @param context 上下文
     */
    public WeatherAdapter(Context context) {
        this.context = context;
    }
    
    /**
     * 设置天气数据列表
     * 
     * @param weatherList 天气数据列表
     */
    public void setWeatherList(List<WeatherData> weatherList) {
        this.weatherList = weatherList;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weather, parent, false);
        return new WeatherViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        if (weatherList != null && position < weatherList.size()) {
            WeatherData weatherData = weatherList.get(position);
            
            // 绑定日期
            holder.tvDate.setText(weatherData.getDateText());
            
            // 绑定天气状况
            holder.tvWeatherCondition.setText(weatherData.getWeatherConditionText());
            
            // 绑定温度范围
            String temperatureRange = weatherData.getTemperatureMin() + "° - " + 
                                     weatherData.getTemperatureMax() + "°";
            holder.tvTemperatureRange.setText(temperatureRange);
            
            // 移除图标显示，使用更可靠的文本表示
            holder.ivWeatherIcon.setVisibility(View.GONE);
        }
    }
    
    @Override
    public int getItemCount() {
        return weatherList != null ? weatherList.size() : 0;
    }
    
    /**
     * 根据天气状况获取对应的图标资源
     * 
     * @param weatherCondition 天气状况常量
     * @return 图标资源ID
     */
    private int getWeatherIcon(int weatherCondition) {
        // 使用更通用的系统图标，确保在所有Android版本中都可用
        switch (weatherCondition) {
            case WeatherData.WEATHER_SUNNY:
                return android.R.drawable.ic_input_add;
            case WeatherData.WEATHER_CLOUDY:
                return android.R.drawable.ic_menu_search;
            case WeatherData.WEATHER_OVERCAST:
                return android.R.drawable.ic_menu_recent_history;
            case WeatherData.WEATHER_RAINY:
                return android.R.drawable.ic_menu_close_clear_cancel;
            case WeatherData.WEATHER_SNOWY:
                return android.R.drawable.ic_menu_more;
            default:
                return android.R.drawable.ic_input_add;
        }
    }
    
    /**
     * 天气列表项的ViewHolder
     */
    static class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate;
        ImageView ivWeatherIcon;
        TextView tvWeatherCondition;
        TextView tvTemperatureRange;
        
        WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            ivWeatherIcon = itemView.findViewById(R.id.iv_item_weather_icon);
            tvWeatherCondition = itemView.findViewById(R.id.tv_item_weather_condition);
            tvTemperatureRange = itemView.findViewById(R.id.tv_item_temperature_range);
        }
    }
}