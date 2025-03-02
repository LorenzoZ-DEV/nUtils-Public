package me.nutilsv3.commands.staff.reportsystem;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.nutilsv3.storage.report.ReportStorage;
import me.nutilsv3.utils.strings.CS;
import me.nutilsv3.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

public class ReportCloseCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (args.length < 1) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("usage", "&cUsage: /report close <id>"))));
            return;
        }

        int reportId;
        try {
            reportId = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text(CS.translate("&cInvalid report ID!")));
            return;
        }

        if (!(sender instanceof com.velocitypowered.api.proxy.Player player)) {
            sender.sendMessage(Component.text(CS.translate("&cOnly players can use this command!")));
            return;
        }

        boolean success = ReportStorage.closeReport(reportId, player.getUsername());

        if (success) {
            sender.sendMessage(Component.text(CS.translate("&aSuccessfully closed report ID: " + reportId)));
        } else {
            sender.sendMessage(Component.text(CS.translate("&cFailed to close report! Report not found.")));
        }
    }
}
