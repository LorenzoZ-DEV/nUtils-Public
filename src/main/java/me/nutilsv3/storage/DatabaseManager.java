/*
package me.nutilsv3.storage;

import me.nutilsv3.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static Connection connection;

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/nutilsv3/database.db");
            Main.getInstance().getLogger().info("✅ Connected to SQLite database successfully!");

            createTable();
        } catch (ClassNotFoundException e) {
            Main.getInstance().getLogger().error("❌ SQLite JDBC driver not found!", e);
        } catch (SQLException e) {
            Main.getInstance().getLogger().error("❌ Failed to connect to SQLite database!", e);
        }
    }


    private static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS reports (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "reporter TEXT, " +
                "reported TEXT, " +
                "reported_uuid TEXT, " +
                "reason TEXT, " +
                "server TEXT, " +
                "status TEXT DEFAULT 'OPEN', " +
                "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            Main.getInstance().getLogger().info("✅ Reports table checked/created successfully!");
        } catch (SQLException e) {
            Main.getInstance().getLogger().error("❌ Failed to create reports table!", e);
        }
    }


    public static Connection getConnection() {
        return connection;
    }


    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                Main.getInstance().getLogger().info("✅ SQLite connection closed successfully.");
            }
        } catch (SQLException e) {
            Main.getInstance().getLogger().error("❌ Error closing SQLite connection", e);
        }
    }
}
*/