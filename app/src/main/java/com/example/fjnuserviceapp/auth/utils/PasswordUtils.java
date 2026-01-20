package com.example.fjnuserviceapp.auth.utils;

import com.example.fjnuserviceapp.auth.viewmodel.PasswordStrength;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {

    private static final int SALT_LENGTH = 16;
    private static final int HASH_ITERATIONS = 10000;
    private static final int HASH_KEY_LENGTH = 256;

    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String encryptPassword(String password, String salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(),
                    Base64.getDecoder().decode(salt),
                    HASH_ITERATIONS,
                    HASH_KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return salt + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("密码加密失败", e);
        }
    }

    public static boolean verifyPassword(String password, String storedPassword) {
        try {
            String[] parts = storedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            String salt = parts[0];
            String computedHash = encryptPassword(password, salt);
            return computedHash.equals(storedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    public static PasswordStrength checkStrength(String password) {
        return PasswordStrength.check(password);
    }
}
