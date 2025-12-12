package com.inventory.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Connection Manager
 * Handles MySQL database connections
 */
public class DatabaseConnection {
    // Database credentials - CHANGE THESE ACCORDING TO YOUR SETUP
    private static final String DB_URL = "jdbc:mysql://localhost:3306/inventory_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root@123"; 
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    // Static block to load the JDBC driver
    static {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("MySQL JDBC Driver loaded successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to load MySQL JDBC Driver!");
            e.printStackTrace();
        }
    }

    /**
     * Get a new database connection
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection established successfully!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection!");
            System.err.println("URL: " + DB_URL);
            System.err.println("User: " + DB_USER);
            throw e;
        }
    }

    /**
     * Close database connection
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Database connection closed successfully!");
            } catch (SQLException e) {
                System.err.println("Error closing database connection!");
                e.printStackTrace();
            }
        }
    }

    /**
     * Test the database connection
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed!");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Main method for testing database connection
     */
    public static void main(String[] args) {
        System.out.println("Testing database connection...");
        if (testConnection()) {
            System.out.println("✓ Database connection test PASSED!");
        } else {
            System.out.println("✗ Database connection test FAILED!");
            System.out.println("\nPlease check:");
            System.out.println("1. MySQL is running");
            System.out.println("2. Database 'inventory_management' exists");
            System.out.println("3. Username and password are correct");
            System.out.println("4. MySQL Connector JAR is in classpath");
        }
    }
}
