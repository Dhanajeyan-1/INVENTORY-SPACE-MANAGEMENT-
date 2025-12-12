package com.inventory.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Password Utility Class
 * Handles password hashing and verification using BCrypt
 */
public class PasswordUtils {

    // BCrypt workload factor (higher = more secure but slower)
    private static final int WORKLOAD = 12;

    /**
     * Hash a password using BCrypt
     * 
     * @param plainPassword The plain text password
     * @return The hashed password
     */
    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(WORKLOAD);
        return BCrypt.hashpw(plainPassword, salt);
    }

    /**
     * Verify a password against a hashed password
     * 
     * @param plainPassword  The plain text password to verify
     * @param hashedPassword The stored hashed password
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Check if a password needs rehashing (if workload has changed)
     * 
     * @param hashedPassword The hashed password to check
     * @return true if password needs rehashing
     */
    public static boolean needsRehash(String hashedPassword) {
        try {
            return !hashedPassword.startsWith("$2a$" + WORKLOAD + "$");
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Generate a temporary password
     * 
     * @return A random 8-character password
     */
    public static String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        StringBuilder password = new StringBuilder();
        java.util.Random random = new java.util.Random();

        for (int i = 0; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }

        return password.toString();
    }
}
