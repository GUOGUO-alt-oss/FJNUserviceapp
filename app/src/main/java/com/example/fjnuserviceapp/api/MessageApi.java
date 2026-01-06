package com.example.fjnuserviceapp.api;



import com.example.fjnuserviceapp.base.entity.BaseMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MessageApi {
    // 获取学院通知列表
    @GET("api/college/notify")
    Call<List<BaseMessage>> getCollegeNotify(@Query("userId") String userId);

    // 获取私信列表
    @GET("api/private/chat")
    Call<List<BaseMessage>> getPrivateChat(@Query("userId") String userId);

    // 获取消息中心列表
    @GET("api/message/center")
    Call<List<BaseMessage>> getMessageCenter(@Query("userId") String userId);
}
