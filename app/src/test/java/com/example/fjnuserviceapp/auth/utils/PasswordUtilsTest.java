package com.example.fjnuserviceapp.auth.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class PasswordUtilsTest {

    @Test
    public void testGenerateSalt() {
        String salt = PasswordUtils.generateSalt();
        assertNotNull(salt);
        assertTrue(salt.length() > 0);
    }

    @Test
    public void testEncryptAndVerify() {
        String password = "TestPassword123!";
        String salt = PasswordUtils.generateSalt();
        String encrypted = PasswordUtils.encryptPassword(password, salt);
        
        assertNotNull(encrypted);
        assertTrue(encrypted.contains(":"));
        
        assertTrue(PasswordUtils.verifyPassword(password, encrypted));
        assertFalse(PasswordUtils.verifyPassword("WrongPassword", encrypted));
    }
}
