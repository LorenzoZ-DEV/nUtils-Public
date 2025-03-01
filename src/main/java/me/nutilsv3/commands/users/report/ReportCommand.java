package me.nutilsv3.commands.users.report;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import me.nutilsv3.Main;
import me.nutilsv3.storage.DatabaseManager;
import me.nutilsv3.utils.strings.CS;
import me.nutilsv3.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;

public class ReportCommand implements SimpleCommand {

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!(sender instanceof Player reporter)) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("player_only", "&cYou must be a player to execute this command!"))));
            return;
        }

        if (!reporter.hasPermission("nutils.report")) {
            reporter.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("no_permission", "&cYou do not have permission to execute this command."))));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 2) {
            reporter.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_usage", "&cUsage: /report <player> <reason>"))));
            return;
        }

        String targetName = args[0];
        String reason = String.join(" ", args).substring(targetName.length()).trim();

        Optional<Player> reportedPlayerOpt = Main.getInstance().getProxy().getPlayer(targetName);
        if (reportedPlayerOpt.isEmpty()) {
            reporter.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("player_not_found", "&cPlayer not found."))));
            return;
        }

        Player reportedPlayer = reportedPlayerOpt.get();
        Optional<ServerConnection> serverConnection = reportedPlayer.getCurrentServer();
        String serverName = serverConnection.map(sc -> sc.getServerInfo().getName()).orElse("Unknown");

        // ✅ Salvare il report nel database
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO reports (reporter, reported, reason, server, status) VALUES (?, ?, ?, ?, 'OPEN')"
             )) {
            stmt.setString(1, reporter.getUsername());
            stmt.setString(2, reportedPlayer.getUsername());
            stmt.setString(3, reason);
            stmt.setString(4, serverName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            Main.getInstance().getLogger().error("Failed to save report to database!", e);
        }

        // ✅ Notifiche per staff e titoli
        String reportTitle = ConfigManager.getMessage("report_title", "⚠️ New Report!");
        String reportSubtitle = ConfigManager.getMessage("report_subtitle", "&d%reporter% &7reported &c%reported% &7for &e%reason%")
                .replace("%reporter%", reporter.getUsername())
                .replace("%reported%", reportedPlayer.getUsername())
                .replace("%reason%", reason);

        Main.getInstance().getProxy().getAllPlayers().stream()
                .filter(staff -> staff.hasPermission("nutils.viewreports"))
                .forEach(staff -> {
                    staff.showTitle(Title.title(
                            Component.text(CS.translate(reportTitle)),
                            Component.text(CS.translate(reportSubtitle)),
                            Title.Times.times(Duration.ofSeconds(0), Duration.ofSeconds(3), Duration.ofSeconds(1))
                    ));
                    staff.sendMessage(Component.text(CS.translate(
                            ConfigManager.getMessage("report_console", "[Report] %reporter% reported %reported% for %reason%")
                                    .replace("%reporter%", reporter.getUsername())
                                    .replace("%reported%", reportedPlayer.getUsername())
                                    .replace("%reason%", reason)
                    )));
                });

        reporter.sendMessage(Component.text(CS.translate(
                ConfigManager.getMessage("report_confirmation", "&aYou reported &c%reported% &afor &e%reason%&a!")
                        .replace("%reported%", reportedPlayer.getUsername())
                        .replace("%reason%", reason)
        )));
    }
}
