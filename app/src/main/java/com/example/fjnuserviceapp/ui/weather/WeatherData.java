package com.example.fjnuserviceapp.ui.weather;

import java.util.Date;

/**
 * 天气数据模型类
 * 存储单天的天气信息
 */
public class WeatherData {
    // 天气状况常量
    public static final int WEATHER_SUNNY = 1;       // 晴天
    public static final int WEATHER_CLOUDY = 2;      // 多云
    public static final int WEATHER_OVERCAST = 3;    // 阴天
    public static final int WEATHER_RAINY = 4;       // 雨天
    public static final int WEATHER_SNOWY = 5;       // 雪天
    
    private Date date;                // 日期
    private int weatherCondition;     // 天气状况
    private String dateText;          // 日期文本（如"周一", "周二"）
    private int temperatureMin;       // 最低温度
    private int temperatureMax;       // 最高温度
    private int currentTemperature;   // 当前实时温度
    
    // 构造函数
    public WeatherData(Date date, int weatherCondition, String dateText, 
                      int temperatureMin, int temperatureMax, int currentTemperature) {
        this.date = date;
        this.weatherCondition = weatherCondition;
        this.dateText = dateText;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.currentTemperature = currentTemperature;
    }
    
    // Getter和Setter方法
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public int getWeatherCondition() {
        return weatherCondition;
    }
    
    public void setWeatherCondition(int weatherCondition) {
        this.weatherCondition = weatherCondition;
    }
    
    public String getDateText() {
        return dateText;
    }
    
    public void setDateText(String dateText) {
        this.dateText = dateText;
    }
    
    public int getTemperatureMin() {
        return temperatureMin;
    }
    
    public void setTemperatureMin(int temperatureMin) {
        this.temperatureMin = temperatureMin;
    }
    
    public int getTemperatureMax() {
        return temperatureMax;
    }
    
    public void setTemperatureMax(int temperatureMax) {
        this.temperatureMax = temperatureMax;
    }
    
    public int getCurrentTemperature() {
        return currentTemperature;
    }
    
    public void setCurrentTemperature(int currentTemperature) {
        this.currentTemperature = currentTemperature;
    }
    
    // 获取天气状况的文本描述
    public String getWeatherConditionText() {
        switch (weatherCondition) {
            case WEATHER_SUNNY:
                return "晴";
            case WEATHER_CLOUDY:
                return "多云";
            case WEATHER_OVERCAST:
                return "阴";
            case WEATHER_RAINY:
                return "雨";
            case WEATHER_SNOWY:
                return "雪";
            default:
                return "未知";
        }
    }
}