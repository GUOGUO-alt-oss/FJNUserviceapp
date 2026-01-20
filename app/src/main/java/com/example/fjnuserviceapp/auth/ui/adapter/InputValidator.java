package com.example.fjnuserviceapp.auth.ui.adapter;

import java.util.regex.Pattern;

public class InputValidator {

    private static final String EMAIL_PATTERN = 
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
        "\\@" +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
        "(" +
        "\\." +
        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
        ")+";

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) return false;
        return Pattern.matches("^1[3-9]\\d{9}$", phone);
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    public static boolean isValidStudentId(String studentId) {
        if (studentId == null || studentId.isEmpty()) return false;
        // 假设学号是10位数字
        return Pattern.matches("^\\d{10}$", studentId);
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) return false;
        // 至少8位，包含大小写字母和数字
        if (password.length() < 8) return false;
        boolean hasUpper = Pattern.compile("[A-Z]").matcher(password).find();
        boolean hasLower = Pattern.compile("[a-z]").matcher(password).find();
        boolean hasDigit = Pattern.compile("\\d").matcher(password).find();
        return hasUpper && hasLower && hasDigit;
    }

    public static boolean isValidCode(String code) {
        if (code == null || code.isEmpty()) return false;
        return Pattern.matches("^\\d{6}$", code);
    }

    public static boolean isValidNickname(String nickname) {
        if (nickname == null || nickname.isEmpty()) return false;
        return nickname.length() >= 2 && nickname.length() <= 20;
    }
}
