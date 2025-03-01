package me.nutilsv3.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import me.nutilsv3.storage.report.ReportStorage;
import net.kyori.adventure.text.Component;

public class StaffJoinListener {

    @Subscribe
    public void onStaffJoin(PostLoginEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("nutils.viewreports")) {
            return;
        }

        int openReports = ReportStorage.getOpenReportsCount();

        if (openReports > 0) {
            player.sendMessage(Component.text("§6[Report] §aThere are §e" + openReports + " §aopen reports. Use §e/report list §ato view them."));
        }
    }
}
