package me.nutilsv3.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import me.nutilsv3.Main;
import me.nutilsv3.utils.checker.UpdateChecker;
import me.nutilsv3.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;

public class PlayerJoinListener {

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("nutils.update")) {
            return;
        }

        String latestVersion = UpdateChecker.getLatestVersion();
        String currentVersion = "unknown";

        if (Main.getInstance().getDescription().containsKey("version")) {
            currentVersion = Main.getInstance().getDescription().get("version");
        }

        if (!currentVersion.equalsIgnoreCase(latestVersion)) {
            // ✅ Prendiamo il messaggio dal config.yml
            String updateMessage = ConfigManager.getMessage("update_notification",
                            "%prefix% 🔔 A new update is available! Current: %current_version%, New: %new_version%\n➡ Click here: %link%")
                    .replace("%prefix%", ConfigManager.getMessage("prefix", "[nUtils]"))
                    .replace("%current_version%", currentVersion)
                    .replace("%new_version%", latestVersion)
                    .replace("%link%", "https://www.spigotmc.org/resources/nutils.119755/")
                    .replace("\\n", "\n");

            // ✅ Messaggio cliccabile con link alla pagina di download
            Component message = Component.text(updateMessage)
                    .clickEvent(ClickEvent.openUrl("https://www.spigotmc.org/resources/nutils.119755/"));

            player.sendMessage(message);
        }
    }
}
