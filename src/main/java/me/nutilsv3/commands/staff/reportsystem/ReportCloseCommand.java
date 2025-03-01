package me.nutilsv3.commands.staff.reportsystem;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.nutilsv3.storage.report.ReportStorage;
import me.nutilsv3.utils.strings.CS;
import me.nutilsv3.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

public class ReportCloseCommand implements SimpleCommand {

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!sender.hasPermission("nutils.managereports")) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("no_permission", "&cYou do not have permission to use this command."))));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 1) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_invalid_id", "&cInvalid report ID. Please provide a valid number."))));
            return;
        }

        try {
            int reportId = Integer.parseInt(args[0]);

            boolean success = ReportStorage.closeReport(reportId, sender.toString());
            if (success) {
                sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_closed", "&aReport ID %id% has been closed by %staff%."))
                        .replace("%id%", String.valueOf(reportId))
                        .replace("%staff%", sender.toString())));
            } else {
                sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_not_found", "&cReport not found or already closed."))));
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_invalid_id", "&cInvalid report ID."))));
        }
    }
}
