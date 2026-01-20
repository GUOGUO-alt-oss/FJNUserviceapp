package com.example.fjnuserviceapp.auth.ui.adapter;

import org.junit.Test;
import static org.junit.Assert.*;

public class InputValidatorTest {

    @Test
    public void testIsValidPhone() {
        assertTrue(InputValidator.isValidPhone("13800138000"));
        assertFalse(InputValidator.isValidPhone("123456"));
        assertFalse(InputValidator.isValidPhone(null));
        assertFalse(InputValidator.isValidPhone(""));
    }

    @Test
    public void testIsValidEmail() {
        assertTrue(InputValidator.isValidEmail("test@example.com"));
        assertFalse(InputValidator.isValidEmail("invalid-email"));
        assertFalse(InputValidator.isValidEmail(null));
    }

    @Test
    public void testIsValidPassword() {
        assertTrue(InputValidator.isValidPassword("Password123"));
        assertFalse(InputValidator.isValidPassword("weak")); // Too short
        assertFalse(InputValidator.isValidPassword("password123")); // No upper
        assertFalse(InputValidator.isValidPassword("PASSWORD123")); // No lower
        assertFalse(InputValidator.isValidPassword("Password")); // No digit
    }
}
