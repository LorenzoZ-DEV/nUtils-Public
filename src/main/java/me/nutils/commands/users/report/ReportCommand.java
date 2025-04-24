package me.nutils.commands.users.report;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.nutils.Main;
import me.nutils.storage.report.ReportStorage;
import me.nutils.utils.strings.CS;
import me.nutils.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

import java.util.Optional;

public class ReportCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!(sender instanceof Player reporter)) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("player_only", "&cYou must be a player to execute this command!"))));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 2) {
            reporter.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_usage", "&cUsage: /report <player> <reason>"))));
            return;
        }

        String targetName = args[0];
        String reason = String.join(" ", args).substring(targetName.length()).trim();

        Optional<Player> reportedPlayerOpt = Main.getInstance ().getProxy().getPlayer(targetName);
        if (reportedPlayerOpt.isEmpty()) {
            reporter.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("player_not_found", "&cPlayer not found."))));
            return;
        }

        Player reportedPlayer = reportedPlayerOpt.get();
        String serverName = reportedPlayer.getCurrentServer().map(sc -> sc.getServerInfo().getName()).orElse("Unknown");

        int reportId = ReportStorage.saveReport(reporter.getUsername(), reportedPlayer.getUsername(), reason, serverName);

        Component message = Component.text("🔔 New report! Click here to teleport to " + serverName)
                .clickEvent(ClickEvent.runCommand("/report tp " + reportId));

        reporter.sendMessage(message);
    }
}
