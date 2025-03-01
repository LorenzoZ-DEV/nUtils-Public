package me.nutilsv3.utils;

import com.velocitypowered.api.proxy.Player;
import me.nutilsv3.storage.report.ReportStorage;
import me.nutilsv3.utils.configs.ConfigManager;
import me.nutilsv3.utils.strings.CS;
import net.kyori.adventure.text.Component;

import java.util.List;

public class GUIManager {

    public static void openReportGUI(Player player) {
        List<String> reports = ReportStorage.getOpenReports();

        if (reports.isEmpty()) {
            player.sendMessage(Component.text( CS.translate(ConfigManager.getMessage("report_no_open_reports", "&aNo open reports."))));
            return;
        }

        player.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_gui_opening", "&aOpening report management GUI..."))));

        // Simulazione GUI
        player.sendMessage(Component.text("§6=== Report Management GUI ==="));
        for (String report : reports) {
            player.sendMessage(Component.text("§7- " + report));
        }
        player.sendMessage(Component.text("§aClick a report to manage it!"));
    }
}
