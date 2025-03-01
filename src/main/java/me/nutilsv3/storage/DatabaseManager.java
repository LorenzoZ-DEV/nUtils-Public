package me.nutilsv3.storage;

import me.nutilsv3.Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection;

    public static void connect() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:plugins/NUtils/reports.db");
            createTable();
            Main.getInstance().getLogger().info("SQLite connected successfully.");
        } catch (Exception e) {
            Main.getInstance().getLogger().error("SQLite connection failed!", e);
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
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Failed to create reports table!", e);
        }
    }


    public static Connection getConnection() {
        return connection;
    }

    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Error closing SQLite connection", e);
        }
    }
}
