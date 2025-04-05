package me.nutils.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import me.nutils.storage.report.ReportStorage;
import me.nutils.utils.configs.ConfigManager;
import me.nutils.utils.strings.CS;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.List;
import java.util.Optional;

public class StaffJoinListener {

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) throws SerializationException {
        Player player = event.getPlayer();

        if (!player.hasPermission("nutils.viewreports")) {
            return;
        }

        Optional<String> serverOpt = player.getCurrentServer().map(server -> server.getServerInfo().getName());
        if (serverOpt.isPresent()) {
            List<String> ignoredServers = ConfigManager.getStringList("reports.ignored_servers");
            if (ignoredServers.contains(serverOpt.get())) {
                return;
            }
        }

        int openReports = ReportStorage.getOpenReportsCount();
        if (openReports > 0) {
            String message = ConfigManager.getMessage("report_join_notification", "&6[Report] &aThere are &e%reports% &aopen reports. Use &e/report list")
                    .replace("%reports%", String.valueOf(openReports))
                    .replace("\\n", "\n");

            player.sendMessage(Component.text( CS.translate ( message)));
        }
    }
}

