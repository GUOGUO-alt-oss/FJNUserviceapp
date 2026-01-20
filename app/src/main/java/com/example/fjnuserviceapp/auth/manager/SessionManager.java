package com.example.fjnuserviceapp.auth.manager;

import com.example.fjnuserviceapp.auth.data.model.User;

public class SessionManager {
    private static SessionManager instance;
    private User currentUser;
    private final TokenManager tokenManager;

    private SessionManager(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    public static synchronized SessionManager getInstance(TokenManager tokenManager) {
        if (instance == null) {
            instance = new SessionManager(tokenManager);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return tokenManager.hasValidToken();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void saveSession(User user) {
        this.currentUser = user;
    }

    public void clearSession() {
        this.currentUser = null;
        tokenManager.clearTokens();
    }

    public void updateUser(User user) {
        this.currentUser = user;
    }
}
