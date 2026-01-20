package com.example.fjnuserviceapp.auth.viewmodel;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class PasswordStrength {
    public enum Level {
        WEAK("弱", Color.parseColor("#FF4D4F")),
        MEDIUM("中", Color.parseColor("#FAAD14")),
        STRONG("强", Color.parseColor("#52C41A")),
        VERY_STRONG("很强", Color.parseColor("#0066FF"));

        public final String label;
        public final int color;

        Level(String label, int color) {
            this.label = label;
            this.color = color;
        }
    }

    private Level level;
    private String message;
    private int score;
    private List<String> feedback;

    public PasswordStrength(Level level, int score, List<String> feedback) {
        this.level = level;
        this.score = score;
        this.feedback = feedback;
    }

    public Level getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }
    
    public List<String> getFeedback() {
        return feedback;
    }

    public static PasswordStrength check(String password) {
        int score = 0;
        List<String> feedback = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            return new PasswordStrength(Level.WEAK, 0, feedback);
        }

        // 长度检测
        if (password.length() >= 8) score += 1;
        if (password.length() >= 12) score += 1;

        // 复杂度检测
        if (password.matches(".*[A-Z].*")) score += 1;  // 大写字母
        if (password.matches(".*[a-z].*")) score += 1;  // 小写字母
        if (password.matches(".*\\d.*")) score += 1;    // 数字
        if (password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) score += 1;  // 特殊字符

        Level level;
        if (score <= 2) level = Level.WEAK;
        else if (score <= 4) level = Level.MEDIUM;
        else if (score <= 5) level = Level.STRONG;
        else level = Level.VERY_STRONG;

        return new PasswordStrength(level, score, feedback);
    }
}
