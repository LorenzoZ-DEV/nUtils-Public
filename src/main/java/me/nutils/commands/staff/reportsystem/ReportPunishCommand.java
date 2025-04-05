package me.nutils.commands.staff.reportsystem;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.nutils.Main;
import me.nutils.storage.report.ReportStorage;
import me.nutils.utils.strings.CS;
import me.nutils.utils.configs.ConfigManager;
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

            Optional<String> reportedPlayer = Optional.ofNullable(ReportStorage.getReportedPlayer(reportId));

            if (reportedPlayer.isEmpty()) {
                sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_not_found", "&cReport not found or already handled."))));
                return;
            }

            String playerName = reportedPlayer.get();

            // ✅ Leggiamo il comando di punizione dal config
            String punishmentCommand = ConfigManager.getString("punishments.command", "ban %player% Report confirmed")
                    .replace("%player%", playerName);

            // ✅ Eseguiamo il comando di punizione
            Main.getInstance ( ).getProxy().getCommandManager().executeAsync(sender, punishmentCommand);

            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_punished", "&aPlayer %reported% has been punished for a confirmed report."))
                    .replace("%reported%", playerName)));

            ReportStorage.closeReport(reportId, sender.toString());

        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_invalid_id", "&cInvalid report ID."))));
        }
    }
}
