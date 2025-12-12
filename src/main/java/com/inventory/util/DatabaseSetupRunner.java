package com.inventory.util;

import java.io.File;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Utility to setup the database for users without MySQL Command Line tools.
 * Right-click this file in IntelliJ and select "Run
 * 'DatabaseSetupRunner.main()'"
 */
public class DatabaseSetupRunner {
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("      DATABASE SETUP RUNNER");
        System.out.println("==========================================");

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter your MySQL Root Password: ");
            String password = scanner.nextLine().trim();

            // Connect to MySQL Server (no specific DB yet)
            String url = "jdbc:mysql://localhost:3306/";
            String user = "root";

            System.out.println("\nConnecting to MySQL Server...");
            try (Connection conn = DriverManager.getConnection(url, user, password);
                    Statement stmt = conn.createStatement()) {

                System.out.println("Connected! Reading sql/schema.sql...");

                // Try to find the file
                File sqlFile = new File("sql/schema.sql");
                if (!sqlFile.exists()) {
                    // Start searching from project root
                    // fallback for different working dirs
                    sqlFile = new File("src/main/resources/sql/schema.sql"); // Not there
                    if (!sqlFile.exists()) {
                        sqlFile = new File("c:\\Users\\HP\\Desktop\\inventory management\\sql\\schema.sql");
                    }
                }

                if (!sqlFile.exists()) {
                    System.err.println("❌ Could not find sql/schema.sql!");
                    System.err.println("Working Directory: " + new File(".").getAbsolutePath());
                    return;
                }

                String content = new String(Files.readAllBytes(sqlFile.toPath()));

                // Basic cleanup
                // Remove comments starting with --
                content = content.replaceAll("--.*", "");

                // Split by semicolon
                String[] commands = content.split(";");

                int successCount = 0;
                for (String command : commands) {
                    if (command.trim().isEmpty())
                        continue;
                    try {
                        String cleanCommand = command.trim();
                        // Ignore DELIMITER commands if any left
                        if (cleanCommand.toUpperCase().startsWith("DELIMITER"))
                            continue;

                        System.out.println(
                                "Executing: " + cleanCommand.substring(0, Math.min(cleanCommand.length(), 40)) + "...");
                        stmt.execute(cleanCommand);
                        successCount++;
                    } catch (Exception e) {
                        System.err.println("⚠️ Warning: " + e.getMessage());
                    }
                }

                System.out.println("\n✅ Database setup complete!");
                System.out.println("Executed " + successCount + " commands.");
                System.out.println("You can now proceed to run the application.");

            } catch (Exception e) {
                System.err.println("❌ Connection Failed: " + e.getMessage());
                System.err.println("Please check your password and ensure MySQL is running.");
            }
        }
    }
}
