package com.example.fjnuserviceapp.auth.network;

import android.content.Context;
import android.content.Intent;

import com.example.fjnuserviceapp.auth.manager.TokenManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {
    private static final String BASE_URL = "https://api.example.com/"; // Should match RetrofitManager or be config
    private static NetworkClient instance;
    private Retrofit retrofit;

    private NetworkClient(Context context) {
        TokenManager tokenManager = new TokenManager(context);
        AuthInterceptor authInterceptor = new AuthInterceptor(tokenManager, new AuthInterceptor.ContextProvider() {
            @Override
            public void sendBroadcast(Intent intent) {
                context.sendBroadcast(intent);
            }

            @Override
            public String getPackageName() {
                return context.getPackageName();
            }
        });

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(authInterceptor)
                .addInterceptor(new MockAuthInterceptor()) // Add Mock Interceptor
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized NetworkClient getInstance(Context context) {
        if (instance == null) {
            instance = new NetworkClient(context.getApplicationContext());
        }
        return instance;
    }

    public <T> T createService(Class<T> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
