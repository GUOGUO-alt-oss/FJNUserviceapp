package com.example.fjnuserviceapp.auth.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class MockAuthInterceptor implements Interceptor {

    private final Gson gson = new Gson();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String path = request.url().encodedPath();

        if (path.endsWith("/auth/login")) {
            return mockLogin(request);
        } else if (path.endsWith("/auth/register")) {
            return mockRegister(request);
        } else if (path.endsWith("/auth/send-code")) {
            return mockSendCode(request);
        } else if (path.endsWith("/auth/verify-code")) {
            return mockVerifyCode(request);
        } else if (path.endsWith("/auth/reset-password")) {
            return mockResetPassword(request);
        } else if (path.endsWith("/auth/profile")) {
            return mockUserProfile(request);
        }

        return chain.proceed(request);
    }

    private Response mockLogin(Request request) {
        // Read body to check credentials if needed, for now accept any valid format
        // Simulating success
        String json = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"登录成功\",\n" +
                "    \"data\": {\n" +
                "        \"user\": {\n" +
                "            \"id\": 1,\n" +
                "            \"phone\": \"13800138000\",\n" +
                "            \"nickname\": \"测试用户\",\n" +
                "            \"avatarUrl\": null\n" +
                "        },\n" +
                "        \"token\": \"mock_access_token_" + UUID.randomUUID().toString() + "\",\n" +
                "        \"refreshToken\": \"mock_refresh_token_" + UUID.randomUUID().toString() + "\",\n" +
                "        \"expiresIn\": 86400\n" +
                "    }\n" +
                "}";
        
        return createResponse(request, json);
    }

    private Response mockRegister(Request request) {
        String json = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"注册成功\",\n" +
                "    \"data\": {\n" +
                "        \"user\": {\n" +
                "            \"id\": 2,\n" +
                "            \"phone\": \"13900139000\",\n" +
                "            \"nickname\": \"新用户\",\n" +
                "            \"avatarUrl\": null\n" +
                "        },\n" +
                "        \"token\": \"mock_access_token_" + UUID.randomUUID().toString() + "\",\n" +
                "        \"refreshToken\": \"mock_refresh_token_" + UUID.randomUUID().toString() + "\",\n" +
                "        \"expiresIn\": 86400\n" +
                "    }\n" +
                "}";
        return createResponse(request, json);
    }

    private Response mockSendCode(Request request) {
        String json = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"验证码发送成功\"\n" +
                "}";
        return createResponse(request, json);
    }

    private Response mockVerifyCode(Request request) {
        String json = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"验证成功\"\n" +
                "}";
        return createResponse(request, json);
    }

    private Response mockResetPassword(Request request) {
        String json = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"密码重置成功\"\n" +
                "}";
        return createResponse(request, json);
    }
    
    private Response mockUserProfile(Request request) {
        String json = "{\n" +
                "    \"code\": 200,\n" +
                "    \"message\": \"success\",\n" +
                "    \"data\": {\n" +
                "        \"id\": 1,\n" +
                "        \"phone\": \"13800138000\",\n" +
                "        \"nickname\": \"测试用户\",\n" +
                "        \"studentId\": \"20210001\",\n" +
                "        \"department\": \"计算机学院\",\n" +
                "        \"major\": \"软件工程\"\n" +
                "    }\n" +
                "}";
        return createResponse(request, json);
    }

    private Response createResponse(Request request, String json) {
        return new Response.Builder()
                .code(200)
                .message("OK")
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(MediaType.parse("application/json"), json))
                .addHeader("content-type", "application/json")
                .build();
    }
}
