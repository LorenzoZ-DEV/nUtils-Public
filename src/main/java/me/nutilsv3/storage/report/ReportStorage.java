package me.nutilsv3.storage.report;

import me.nutilsv3.Main;
import me.nutilsv3.storage.DatabaseManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReportStorage {

    public static List<String> getOpenReports() {
        List<String> reports = new ArrayList<>();
        String sql = "SELECT id, reporter, reported, reason, server FROM reports WHERE status = 'OPEN'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String reporter = rs.getString("reporter");
                String reported = rs.getString("reported");
                String reason = rs.getString("reason");
                String server = rs.getString("server");

                reports.add("ID: " + id + " | Reporter: " + reporter + " | Reported: " + reported + " | Reason: " + reason + " | Server: " + server);
            }

        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Failed to retrieve reports!", e);
        }
        return reports;
    }

    public static boolean updateReportStatus(int id, String status) {
        String sql = "UPDATE reports SET status = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Failed to update report status!", e);
            return false;
        }
    }
    public static boolean closeReport(int id, String staffName) {
        String sql = "UPDATE reports SET status = 'CLOSED', handled_by = ? WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staffName);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Failed to close report!", e);
            return false;
        }
    }

    public static int getOpenReportsCount() {
        String sql = "SELECT COUNT(*) FROM reports WHERE status = 'OPEN'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1); // Restituisce il numero totale di report aperti
            }

        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Failed to count open reports!", e);
        }

        return 0; // Restituisce 0 se non ci sono report o in caso di errore
    }
    public static boolean assignReport(int id, String staffName) {
        String sql = "UPDATE reports SET assigned_to = ? WHERE id = ? AND status = 'OPEN'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staffName);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Failed to assign report!", e);
            return false;
        }
    }

    public static int getHandledReportsCount(String staffName) {
        String sql = "SELECT COUNT(*) FROM reports WHERE handled_by = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staffName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Failed to count handled reports!", e);
        }
        return 0;
    }

    public static Optional<String> getReportedPlayer(int id) {
        String sql = "SELECT reported FROM reports WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(rs.getString("reported"));
            }
        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Failed to retrieve reported player!", e);
        }
        return Optional.empty();
    }

    public static boolean reopenReport(int id, String staffName) {
        String sql = "UPDATE reports SET status = 'OPEN', handled_by = ? WHERE id = ? AND status = 'CLOSED'";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, staffName);
            stmt.setInt(2, id);
            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Failed to reopen report!", e);
            return false;
        }
    }
    public static Optional<String> getReportedServer(int id) {
        String sql = "SELECT server FROM reports WHERE id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(rs.getString("server"));
            }
        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Failed to retrieve reported server!", e);
        }
        return Optional.empty();
    }




}
