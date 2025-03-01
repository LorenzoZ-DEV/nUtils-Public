package me.nutilsv3.commands.staff.reportsystem;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.nutilsv3.storage.report.ReportStorage;
import me.nutilsv3.Main;
import me.nutilsv3.utils.strings.CS;
import me.nutilsv3.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public class ReportAssignCommand implements SimpleCommand {

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!sender.hasPermission("nutils.managereports")) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("no_permission", "&cYou do not have permission to use this command."))));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 2) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_assign_usage", "&cUsage: /report assign <id> <staff>"))));
            return;
        }

        try {
            int reportId = Integer.parseInt(args[0]);
            String staffName = args[1];

            Optional<Player> staff = Main.getInstance().getProxy().getPlayer(staffName);
            if (staff.isEmpty()) {
                sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("player_not_found", "&cStaff member not found."))));
                return;
            }

            boolean success = ReportStorage.assignReport(reportId, staffName);
            if (success) {
                sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_assigned", "&aReport ID %id% has been assigned to %staff%."))
                        .replace("%id%", String.valueOf(reportId))
                        .replace("%staff%", staffName)));
            } else {
                sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_not_found", "&cReport not found or already assigned."))));
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_invalid_id", "&cInvalid report ID."))));
        }
    }
}
