package me.nutils.commands.staff.reportsystem;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.nutils.utils.strings.CS;
import me.nutils.utils.GUIManager;
import me.nutils.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

public class ReportGUICommand implements SimpleCommand {

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("player_only", "&cYou must be a player to use this command!"))));
            return;
        }

        if (!player.hasPermission("nutils.gui")) {
            player.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("no_permission", "&cYou do not have permission to use this command."))));
            return;
        }

        player.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_gui_opening", "&aOpening report management GUI..."))));
        GUIManager.openReportGUI(player);
    }
}
