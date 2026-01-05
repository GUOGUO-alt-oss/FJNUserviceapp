package com.example.fjnuserviceapp.utils;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * 网络请求工具类，封装Retrofit+OkHttp
 */
public class RetrofitManager {
    private static final String BASE_URL = "https://api.example.com/"; // 模拟API地址
    private static final long TIMEOUT = 10L; // 超时时间（秒）

    // 创建OkHttpClient实例
    private static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
            // 可以添加拦截器、日志等
            .build();

    // 创建Retrofit实例
    private static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    /**
     * 获取API服务实例
     * @param serviceClass API服务接口类
     * @param <T> 泛型参数
     * @return API服务实例
     */
    public static <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}