package com.example.fjnuserviceapp.ui.weather.utils;

import com.example.fjnuserviceapp.ui.weather.WeatherData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 模拟天气数据生成工具
 * 提供天气数据的创建和管理
 */
public class WeatherDataUtil {
    private static final Random random = new Random();
    
    // 中文星期名称
    private static final String[] WEEK_DAYS = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    
    // 保存上一次的温度数据，用于生成连续的天气变化
    private static int lastMinTemp = -1;
    private static int lastMaxTemp = -1;
    private static int lastWeatherCondition = -1;
    
    // 温度变化的最大幅度（度）
    private static final int MAX_TEMP_CHANGE = 2;
    
    // 天气状况变化的概率权重（值越大，变化的可能性越小）
    private static final int SAME_WEATHER_WEIGHT = 3;
    
    /**
     * 生成指定天数和地区的天气数据
     * 
     * @param days 要生成的天数
     * @param location 地区名称
     * @return 天气数据列表
     */
    public static List<WeatherData> generateWeatherData(int days, String location) {
        List<WeatherData> weatherList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        
        // 确保location不为空
        if (location == null) {
            location = "默认地区";
        }
        
        for (int i = 0; i < days; i++) {
            // 计算日期
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_YEAR, i);
            Date date = calendar.getTime();
            
            // 获取星期几
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            String dateText = WEEK_DAYS[dayOfWeek];
            
            // 生成天气状况
            int weatherCondition = generateRandomWeatherCondition();
            
            // 生成温度范围
            int[] temperatureRange = generateRandomTemperatureRange(location);
            int minTemp = temperatureRange[0];
            int maxTemp = temperatureRange[1];
            
            // 生成随机实时温度
            int currentTemp = generateCurrentTemperature(minTemp, maxTemp);
            
            // 创建天气数据对象
            WeatherData weatherData = new WeatherData(
                    date,
                    weatherCondition,
                    dateText,
                    minTemp,
                    maxTemp,
                    currentTemp
            );
            
            weatherList.add(weatherData);
        }
        
        return weatherList;
    }
    
    /**
     * 生成天气状况
     * 考虑上一次的天气状况，增加天气状况的持续性
     * 
     * @return 天气状况常量
     */
    private static int generateRandomWeatherCondition() {
        int[] conditions = {WeatherData.WEATHER_SUNNY, WeatherData.WEATHER_CLOUDY, 
                          WeatherData.WEATHER_OVERCAST, WeatherData.WEATHER_RAINY};
        
        // 如果是第一次生成或随机到需要变化，则生成新的天气状况
        if (lastWeatherCondition == -1 || random.nextInt(SAME_WEATHER_WEIGHT + 1) == 0) {
            lastWeatherCondition = conditions[random.nextInt(conditions.length)];
        }
        
        return lastWeatherCondition;
    }
    
    /**
     * 生成温度范围
     * 考虑不同地区的气候特点和上一次的温度数据，实现更平滑的温度变化
     * 
     * @param location 地区名称
     * @return 温度范围数组 [最低温度, 最高温度]
     */
    private static int[] generateRandomTemperatureRange(String location) {
        // 根据不同地区设置不同的温度范围
        int baseMinTemp = 10;
        int baseMaxTemp = 25;
        
        // 确保location不为空
        if (location == null) {
            location = "默认地区";
        }
        
        // 基于中国不同城市的典型冬季温度范围调整
        if (location.equals("北京")) {
            baseMinTemp = -5;
            baseMaxTemp = 10;
        } else if (location.equals("上海")) {
            baseMinTemp = 5;
            baseMaxTemp = 15;
        } else if (location.equals("广州") || location.equals("深圳")) {
            baseMinTemp = 15;
            baseMaxTemp = 25;
        } else if (location.equals("成都")) {
            baseMinTemp = 5;
            baseMaxTemp = 15;
        } else if (location.equals("杭州")) {
            baseMinTemp = 3;
            baseMaxTemp = 12;
        } else if (location.equals("武汉")) {
            baseMinTemp = 3;
            baseMaxTemp = 13;
        } else {
            // 默认是福建师范大学旗山校区（福州）的温度范围
            baseMinTemp = 10;
            baseMaxTemp = 25;
        }
        
        int minTemp;
        int maxTemp;
        
        if (lastMinTemp == -1 || lastMaxTemp == -1) {
            // 第一次生成温度，随机生成
            minTemp = baseMinTemp + random.nextInt(5);
            maxTemp = minTemp + 5 + random.nextInt(10);
        } else {
            // 基于上一次的温度生成，实现平滑变化
            // 最低温度变化
            int minTempChange = random.nextInt(2 * MAX_TEMP_CHANGE + 1) - MAX_TEMP_CHANGE;
            minTemp = lastMinTemp + minTempChange;
            
            // 确保最低温度在合理范围内
            if (minTemp < baseMinTemp) {
                minTemp = baseMinTemp;
            }
            
            // 最高温度变化
            int maxTempChange = random.nextInt(2 * MAX_TEMP_CHANGE + 1) - MAX_TEMP_CHANGE;
            maxTemp = lastMaxTemp + maxTempChange;
            
            // 确保最高温度在合理范围内，并且大于最低温度
            if (maxTemp < minTemp + 3) {
                maxTemp = minTemp + 3;
            } else if (maxTemp > baseMaxTemp) {
                maxTemp = baseMaxTemp;
            }
        }
        
        // 保存当前温度作为下一次的参考
        lastMinTemp = minTemp;
        lastMaxTemp = maxTemp;
        
        return new int[]{minTemp, maxTemp};
    }
    
    /**
     * 生成随机实时温度
     * 在最低温度和最高温度之间随机生成
     * 
     * @param min 最低温度
     * @param max 最高温度
     * @return 当前实时温度
     */
    private static int generateCurrentTemperature(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }
}