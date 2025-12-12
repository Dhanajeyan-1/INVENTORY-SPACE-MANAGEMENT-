package com.inventory.util;

import java.util.regex.Pattern;

/**
 * Validation Utility Class
 * Handles validation for various input types
 */
public class ValidationUtils {

    // Email regex pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    // Phone regex pattern (supports various formats)
    private static final Pattern PHONE_PATTERN = Pattern
            .compile("^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{1,9}$");

    // Username pattern (alphanumeric, underscore, 3-20 characters)
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,20}$");

    // SKU pattern (letters, numbers, hyphens)
    private static final Pattern SKU_PATTERN = Pattern.compile("^[A-Z]{4}-[0-9]{3}$");

    /**
     * Validate email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validate phone number format
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validate username format
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    /**
     * Validate SKU format
     */
    public static boolean isValidSKU(String sku) {
        if (sku == null || sku.trim().isEmpty()) {
            return false;
        }
        return SKU_PATTERN.matcher(sku.trim()).matches();
    }

    /**
     * Validate password strength
     * At least 6 characters, contains letter and number
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isLetter(c))
                hasLetter = true;
            if (Character.isDigit(c))
                hasDigit = true;
        }

        return hasLetter && hasDigit;
    }

    /**
     * Validate string is not null or empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Validate numeric value is positive
     */
    public static boolean isPositive(double value) {
        return value > 0;
    }

    /**
     * Validate integer is within range
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Sanitize string input (remove special characters)
     */
    public static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        // Remove potential SQL injection characters
        return input.replaceAll("[;'\"\\\\]", "").trim();
    }

    /**
     * Validate price format (positive decimal with max 2 decimal places)
     */
    public static boolean isValidPrice(String price) {
        if (price == null || price.trim().isEmpty()) {
            return false;
        }

        try {
            double value = Double.parseDouble(price);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate quantity (positive integer)
     */
    public static boolean isValidQuantity(String quantity) {
        if (quantity == null || quantity.trim().isEmpty()) {
            return false;
        }

        try {
            int value = Integer.parseInt(quantity);
            return value >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Get validation error message
     */
    public static String getValidationError(String field, String type) {
        switch (type) {
            case "required":
                return field + " is required";
            case "email":
                return "Invalid email format";
            case "phone":
                return "Invalid phone number format";
            case "username":
                return "Username must be 3-20 alphanumeric characters";
            case "password":
                return "Password must be at least 6 characters with letters and numbers";
            case "price":
                return "Price must be a positive number";
            case "quantity":
                return "Quantity must be a non-negative integer";
            case "sku":
                return "SKU format must be XXXX-###";
            default:
                return "Invalid " + field;
        }
    }
}
