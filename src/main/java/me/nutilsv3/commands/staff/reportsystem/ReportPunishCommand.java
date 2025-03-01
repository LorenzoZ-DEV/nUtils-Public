package me.nutilsv3.commands.staff.reportsystem;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.nutilsv3.storage.report.ReportStorage;
import me.nutilsv3.utils.strings.CS;
import me.nutilsv3.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public class ReportPunishCommand implements SimpleCommand {

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!sender.hasPermission("nutils.punish")) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("no_permission", "&cYou do not have permission to use this command."))));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 1) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_punish_usage", "&cUsage: /report punish <id>"))));
            return;
        }

        try {
            int reportId = Integer.parseInt(args[0]);

            // Recupera il giocatore segnalato dal report
            Optional<String> reportedPlayer = ReportStorage.getReportedPlayer(reportId);

            if (reportedPlayer.isEmpty()) {
                sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_not_found", "&cReport not found or already handled."))));
                return;
            }

            String playerName = reportedPlayer.get();

            // Esegui il comando di ban o punizione (puoi personalizzarlo)
            String punishmentCommand = "ban " + playerName + " Report confirmed";
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_punished", "&aPlayer %reported% has been punished for a confirmed report."))
                    .replace("%reported%", playerName)));

            ReportStorage.closeReport(reportId, sender.toString());

        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_invalid_id", "&cInvalid report ID."))));
        }
    }
}
