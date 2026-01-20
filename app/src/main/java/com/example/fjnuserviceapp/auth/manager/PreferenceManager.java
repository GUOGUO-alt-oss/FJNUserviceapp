package com.example.fjnuserviceapp.auth.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "auth_prefs";
    private static final String KEY_REMEMBER_PASSWORD = "remember_password";
    private static final String KEY_AUTO_LOGIN = "auto_login";
    private static final String KEY_ACCOUNT = "account";
    private static final String KEY_PASSWORD = "password"; // Encrypted

    private SharedPreferences prefs;

    public PreferenceManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveRememberPassword(boolean remember) {
        prefs.edit().putBoolean(KEY_REMEMBER_PASSWORD, remember).apply();
    }

    public boolean isRememberPassword() {
        return prefs.getBoolean(KEY_REMEMBER_PASSWORD, false);
    }

    public void saveAutoLogin(boolean autoLogin) {
        prefs.edit().putBoolean(KEY_AUTO_LOGIN, autoLogin).apply();
    }

    public boolean isAutoLogin() {
        return prefs.getBoolean(KEY_AUTO_LOGIN, false);
    }

    public void saveAccount(String account, String password) {
        prefs.edit()
                .putString(KEY_ACCOUNT, account)
                .putString(KEY_PASSWORD, password) // Should be encrypted in real usage
                .apply();
    }

    public void clearPassword() {
        prefs.edit().remove(KEY_PASSWORD).apply();
    }

    public String getRememberedAccount() {
        return prefs.getString(KEY_ACCOUNT, "");
    }

    public String getRememberedPassword() {
        return prefs.getString(KEY_PASSWORD, "");
    }

    public void clearAuthState() {
        prefs.edit()
                .remove(KEY_AUTO_LOGIN)
                .remove(KEY_PASSWORD)
                .apply();
        // Keep account and remember password setting if desired, or clear all
    }
}
