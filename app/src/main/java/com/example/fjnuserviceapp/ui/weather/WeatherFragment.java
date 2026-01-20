package com.example.fjnuserviceapp.ui.weather;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fjnuserviceapp.MainActivity;
import com.example.fjnuserviceapp.R;
import com.example.fjnuserviceapp.databinding.FragmentWeatherBinding;
import com.example.fjnuserviceapp.ui.nav.NavFragment;
import com.example.fjnuserviceapp.ui.weather.utils.WeatherDataUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 天气预报页面的Fragment
 * 处理用户交互和UI更新
 */
public class WeatherFragment extends Fragment {
    
    private FragmentWeatherBinding binding;
    private WeatherAdapter weatherAdapter;
    private List<WeatherData> weatherList;
    
    // 地区列表
    private String[] locations = {
        "福建师范大学旗山校区",
        "北京",
        "上海",
        "广州",
        "深圳",
        "成都",
        "杭州",
        "武汉"
    };
    
    // 当前选中的地区
    private String currentLocation;
    
    // 地区适配器
    private ArrayAdapter<String> locationAdapter;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        // 初始化视图绑定
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        
        // 设置Fragment可以处理菜单事件
        setHasOptionsMenu(true);
        
        // 初始化视图组件
        initView();
        
        // 加载天气数据
        loadWeatherData();
        
        return binding.getRoot();
    }
    
    /**
     * 初始化视图组件
     */
    private void initView() {
        // 设置Toolbar
        Toolbar toolbar = binding.toolbar;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        
        // 设置Toolbar标题颜色
        toolbar.setTitleTextColor(getResources().getColor(R.color.neon_purple));
        
        // 设置明显的返回按钮点击事件
        Button btnBack = binding.btnBack;
        btnBack.setOnClickListener(v -> {
            if (requireActivity() instanceof MainActivity) {
                MainActivity mainActivity = (MainActivity) requireActivity();
                // 直接切换回NavFragment而不是调用onBackPressed()
                mainActivity.switchFragment(new NavFragment());
            }
        });
        
        // 初始化RecyclerView
        RecyclerView recyclerView = binding.rvWeatherForecast;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        // 初始化适配器
        weatherAdapter = new WeatherAdapter(getContext());
        recyclerView.setAdapter(weatherAdapter);
        
        // 初始化地区选择器
        initLocationSpinner();
        
        // 设置当前日期
        updateCurrentDate();
    }
    
    /**
     * 初始化地区选择器
     */
    private void initLocationSpinner() {
        try {
            // 确保fragment已附加到activity
            if (!isAdded() || getContext() == null) return;
            
            // 确保binding不为空
            if (binding == null) return;
            
            // 创建地区适配器，使用自定义布局
            locationAdapter = new ArrayAdapter<>(getContext(), 
                    R.layout.spinner_item_custom, locations);
            
            // 设置下拉菜单样式，使用自定义下拉布局
            locationAdapter.setDropDownViewResource(R.layout.spinner_dropdown_custom);
            
            // 设置适配器
            Spinner spinnerLocation = binding.spinnerLocation;
            spinnerLocation.setAdapter(locationAdapter);
            
            // 设置默认选中第一个地区
            currentLocation = locations[0];
            spinnerLocation.setSelection(0);
            
            // 设置地区选择监听器
            spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // 更新当前选中的地区
                    currentLocation = locations[position];
                    
                    // 加载选中地区的天气数据
                    loadWeatherData();
                }
                
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // 默认选中第一个地区
                    if (isAdded() && binding != null) {
                        currentLocation = locations[0];
                        binding.spinnerLocation.setSelection(0);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 更新当前日期显示
     */
    private void updateCurrentDate() {
        TextView tvDate = binding.tvDate;
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 EEEE", Locale.CHINA);
        String dateText = dateFormat.format(currentDate);
        tvDate.setText(dateText);
    }
    
    /**
     * 加载天气数据
     */
    private void loadWeatherData() {
        try {
            // 确保fragment已附加到activity
            if (!isAdded()) return;
            
            // 确保binding不为空
            if (binding == null) return;
            
            // 确保currentLocation不为空
            if (currentLocation == null) {
                currentLocation = locations[0];
            }
            
            // 生成5天的模拟天气数据
            weatherList = WeatherDataUtil.generateWeatherData(5, currentLocation);
            
            // 更新当前天气卡片
            if (weatherList != null && !weatherList.isEmpty()) {
                WeatherData currentWeather = weatherList.get(0);
                updateCurrentWeatherCard(currentWeather);
            }
            
            // 更新天气预报列表
            updateWeatherList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 更新当前天气卡片
     * 
     * @param currentWeather 当前天气数据
     */
    private void updateCurrentWeatherCard(WeatherData currentWeather) {
        // 更新当前温度
        TextView tvCurrentTemperature = binding.tvCurrentTemperature;
        tvCurrentTemperature.setText(currentWeather.getCurrentTemperature() + "°");
        
        // 更新天气状况
        TextView tvWeatherCondition = binding.tvWeatherCondition;
        tvWeatherCondition.setText(currentWeather.getWeatherConditionText());
        
        // 更新温度范围
        TextView tvTemperatureRange = binding.tvTemperatureRange;
        String temperatureRange = currentWeather.getTemperatureMin() + "° - " + 
                                 currentWeather.getTemperatureMax() + "°";
        tvTemperatureRange.setText(temperatureRange);
        

    }
    
    /**
     * 更新天气预报列表
     */
    private void updateWeatherList() {
        if (weatherAdapter != null) {
            weatherAdapter.setWeatherList(weatherList);
        }
    }
    

    

    

    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 释放视图绑定资源
        binding = null;
    }
}