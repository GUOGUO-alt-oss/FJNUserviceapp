package com.example.fjnuserviceapp.api;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * API服务接口，定义所有网络请求方法
 */
public interface ApiService {
    // 模拟获取课程列表
    @GET("courses")
    Call<List<Object>> getCourses(@Query("week") int week);

    // 模拟获取失物招领列表
    @GET("lost-found")
    Call<List<Object>> getLostFoundList(@Query("type") int type);

    // 模拟获取校园通知
    @GET("notifications")
    Call<List<Object>> getNotifications();

    // 模拟提交数据
    @POST("submit")
    Call<Object> submitData(@Query("data") String data);
}