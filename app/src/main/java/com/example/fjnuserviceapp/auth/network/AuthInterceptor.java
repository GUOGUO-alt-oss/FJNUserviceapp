package com.example.fjnuserviceapp.auth.network;

import android.content.Intent;

import com.example.fjnuserviceapp.auth.data.model.ApiResponse;
import com.example.fjnuserviceapp.auth.data.model.AuthResponse;
import com.example.fjnuserviceapp.auth.data.model.RefreshTokenRequest;
import com.example.fjnuserviceapp.auth.manager.TokenManager;
import com.example.fjnuserviceapp.utils.RetrofitManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;

public class AuthInterceptor implements Interceptor {

    private final TokenManager tokenManager;
    private final ContextProvider contextProvider;

    public interface ContextProvider {
        void sendBroadcast(Intent intent);
        String getPackageName();
    }

    public AuthInterceptor(TokenManager tokenManager, ContextProvider contextProvider) {
        this.tokenManager = tokenManager;
        this.contextProvider = contextProvider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        if (isPublicApi(originalRequest)) {
            return chain.proceed(originalRequest);
        }

        String token = tokenManager.getToken();
        if (token == null || token.isEmpty()) {
            // handleNoToken
            return chain.proceed(originalRequest); // Let server return 401 or handle locally
        }

        // Check if token expiring soon (optional optimization)
        if (tokenManager.isTokenExpiringSoon()) {
             refreshTokenSync();
             token = tokenManager.getToken(); // Update token
        }

        Request authenticatedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        Response response = chain.proceed(authenticatedRequest);

        if (response.code() == 401) {
            response.close(); // Close previous response
            // Token expired, try refresh
            if (refreshTokenSync()) {
                // Refresh success, retry with new token
                Request newRequest = originalRequest.newBuilder()
                        .header("Authorization", "Bearer " + tokenManager.getToken())
                        .build();
                return chain.proceed(newRequest);
            } else {
                // Refresh failed, logout
                tokenManager.clearTokens();
                // Broadcast logout
                Intent intent = new Intent("ACTION_TOKEN_EXPIRED");
                if (contextProvider != null) {
                    intent.setPackage(contextProvider.getPackageName());
                    contextProvider.sendBroadcast(intent);
                }
            }
        }

        return response;
    }

    private boolean isPublicApi(Request request) {
        String path = request.url().encodedPath();
        return path.contains("/auth/login") ||
               path.contains("/auth/register") ||
               path.contains("/auth/send-code") ||
               path.contains("/auth/refresh-token");
    }

    private synchronized boolean refreshTokenSync() {
        String refreshToken = tokenManager.getRefreshToken();
        if (refreshToken == null || refreshToken.isEmpty()) {
            return false;
        }

        AuthApi api = RetrofitManager.createService(AuthApi.class);
        Call<ApiResponse<AuthResponse>> call = api.refreshToken(new RefreshTokenRequest(refreshToken));
        try {
            retrofit2.Response<ApiResponse<AuthResponse>> response = call.execute();
            if (response.isSuccessful() && response.body() != null && response.body().getCode() == 200) {
                AuthResponse data = response.body().getData();
                tokenManager.saveToken(data.getToken());
                tokenManager.saveRefreshToken(data.getRefreshToken());
                tokenManager.saveTokenExpiresAt(data.getExpiresIn());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
