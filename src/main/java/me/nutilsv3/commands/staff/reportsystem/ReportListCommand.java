package me.nutilsv3.commands.staff.reportsystem;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.nutilsv3.storage.report.ReportStorage;
import me.nutilsv3.utils.strings.CS;
import me.nutilsv3.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

import java.util.List;

public class ReportListCommand implements SimpleCommand {

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!sender.hasPermission("nutils.viewreports")) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("no_permission", "&cYou do not have permission to use this command."))));
            return;
        }

        List<String> reports = ReportStorage.getOpenReports();

        if (reports.isEmpty()) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_no_open_reports", "&aThere are no open reports."))));
            return;
        }

        sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_list_header", "&6Open Reports:"))));
        for (String report : reports) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_list_format", "&7- %id% | Reporter: %reporter% | Reported: %reported% | Reason: %reason%"))
                    .replace("%id%", report.split(" \\| ")[0])
                    .replace("%reporter%", report.split(" \\| ")[1])
                    .replace("%reported%", report.split(" \\| ")[2])
                    .replace("%reason%", report.split(" \\| ")[3])));
        }
    }
}
