package me.nutilsv3.commands.staff.reportsystem;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.nutilsv3.storage.report.ReportStorage;
import me.nutilsv3.utils.strings.CS;
import me.nutilsv3.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

public class ReportReopenCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (args.length < 1) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("usage", "&cUsage: /report reopen <id>"))));
            return;
        }

        int reportId;
        try {
            reportId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text(CS.translate("&cInvalid report ID!")));
            return;
        }

        boolean success = ReportStorage.reopenReport(reportId);

        if (success) {
            sender.sendMessage(Component.text(CS.translate("&aSuccessfully reopened report ID: " + reportId)));
        } else {
            sender.sendMessage(Component.text(CS.translate("&cFailed to reopen report! Report not found.")));
        }
    }
}
