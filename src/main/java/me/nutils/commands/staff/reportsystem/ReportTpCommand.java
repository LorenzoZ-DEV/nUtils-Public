package me.nutils.commands.staff.reportsystem;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.nutils.storage.report.ReportStorage;
import me.nutils.Main;
import me.nutils.utils.strings.CS;
import me.nutils.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public class ReportTpCommand implements SimpleCommand {

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!(sender instanceof Player staff)) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("player_only", "&cYou must be a player to use this command!"))));
            return;
        }

        if (!staff.hasPermission("nutils.managereports")) {
            staff.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("no_permission", "&cYou do not have permission to use this command."))));
            return;
        }

        String[] args = invocation.arguments();
        if (args.length < 1) {
            staff.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_tp_usage", "&cUsage: /report tp <id>"))));
            return;
        }

        try {
            int reportId = Integer.parseInt(args[0]);

            Optional<String> serverName = ReportStorage.getReportedServer(reportId).describeConstable ( );

            if (serverName.isEmpty()) {
                staff.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_not_found", "&cReport not found or player is offline."))));
                return;
            }

            staff.createConnectionRequest(Main.getInstance().getProxy().getServer(serverName.get()).get()).fireAndForget();
            staff.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_teleporting", "&aTeleporting you to the server of report ID %id%."))
                    .replace("%id%", String.valueOf(reportId))));

        } catch (NumberFormatException e) {
            staff.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_invalid_id", "&cInvalid report ID."))));
        }
    }
}
