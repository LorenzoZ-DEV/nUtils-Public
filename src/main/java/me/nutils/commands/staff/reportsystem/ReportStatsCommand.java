package me.nutils.commands.staff.reportsystem;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.nutils.storage.report.ReportStorage;
import me.nutils.utils.strings.CS;
import me.nutils.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

public class ReportStatsCommand implements SimpleCommand {

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!sender.hasPermission("nutils.viewstats")) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("no_permission", "&cYou do not have permission to use this command."))));
            return;
        }

        String staffName = sender.toString();
        int handledReports = ReportStorage.getHandledReportsCount(staffName);

        sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_stats", "&aYou have handled %count% reports."))
                .replace("%count%", String.valueOf(handledReports))));
    }
}
