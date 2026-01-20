package com.example.fjnuserviceapp.auth.manager;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import android.text.TextUtils;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenManager {

    private static final String KEY_ACCESS_TOKEN = "key_access_token";
    private static final String KEY_REFRESH_TOKEN = "key_refresh_token";
    private static final String KEY_TOKEN_EXPIRES = "key_token_expires";

    private SharedPreferences securePrefs;

    public TokenManager(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            this.securePrefs = EncryptedSharedPreferences.create(
                    "token_prefs",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            // Fallback or handle error (e.g., clear data)
            // In a real app, might want to delete the file and recreate it
        }
    }

    public void saveToken(String token) {
        if (securePrefs != null) {
            securePrefs.edit().putString(KEY_ACCESS_TOKEN, token).apply();
        }
    }

    public String getToken() {
        return securePrefs != null ? securePrefs.getString(KEY_ACCESS_TOKEN, null) : null;
    }

    public void saveRefreshToken(String refreshToken) {
        if (securePrefs != null) {
            securePrefs.edit().putString(KEY_REFRESH_TOKEN, refreshToken).apply();
        }
    }

    public String getRefreshToken() {
        return securePrefs != null ? securePrefs.getString(KEY_REFRESH_TOKEN, null) : null;
    }

    public void saveTokenExpiresAt(long expiresIn) {
        if (securePrefs != null) {
            long expiresAt = System.currentTimeMillis() + expiresIn * 1000;
            securePrefs.edit().putLong(KEY_TOKEN_EXPIRES, expiresAt).apply();
        }
    }

    public long getTokenExpiresAt() {
        return securePrefs != null ? securePrefs.getLong(KEY_TOKEN_EXPIRES, 0) : 0;
    }

    public boolean hasValidToken() {
        String token = getToken();
        long expiresAt = getTokenExpiresAt();
        return !TextUtils.isEmpty(token) && expiresAt > System.currentTimeMillis();
    }

    public boolean isTokenExpiringSoon() {
        long expiresAt = getTokenExpiresAt();
        long fiveMinutesLater = System.currentTimeMillis() + 5 * 60 * 1000;
        return expiresAt > 0 && expiresAt < fiveMinutesLater;
    }

    public void clearTokens() {
        if (securePrefs != null) {
            securePrefs.edit()
                    .remove(KEY_ACCESS_TOKEN)
                    .remove(KEY_REFRESH_TOKEN)
                    .remove(KEY_TOKEN_EXPIRES)
                    .apply();
        }
    }
}
