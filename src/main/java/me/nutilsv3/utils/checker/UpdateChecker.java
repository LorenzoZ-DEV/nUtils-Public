package me.nutilsv3.utils.checker;

import me.nutilsv3.Main;
import me.nutilsv3.utils.configs.ConfigManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker {

    private static final int RESOURCE_ID = 119755;
    private static String latestVersion = "unknown";
    public static void checkForUpdates() {
        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=" + RESOURCE_ID);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "NUtils-Plugin/" + Main.getInstance().getDescription().get("version"));

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    Main.getInstance().getLogger().warn("⚠ SpigotMC API returned an error: HTTP " + responseCode);
                    return;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();

                if (response == null || response.isEmpty()) {
                    Main.getInstance().getLogger().warn("⚠ Unable to fetch update information from Spigot.");
                    return;
                }

                latestVersion = response.split("\"current_version\":\"")[1].split("\"")[0];
                String currentVersion = Main.getInstance().getDescription().get("version");

                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    String updateMessage = ConfigManager.getMessage("update_checker.message",
                                    "🔔 A new update is available! Current: %current_version%, New: %new_version%\n➡ Click here: %link%")
                            .replace("%current_version%", currentVersion)
                            .replace("%new_version%", latestVersion)
                            .replace("%link%", "https://www.spigotmc.org/resources/nutils.119755/")
                            .replace("\\n", "\n");

                    Main.getInstance().getLogger().info(updateMessage);
                } else {
                    Main.getInstance().getLogger().info("✅ NUtils is up to date!");
                }

            } catch (Exception e) {
                Main.getInstance().getLogger().error("❌ Failed to check for updates on Spigot!", e);
            }
        });
    }

    public static String getLatestVersion() {
        return latestVersion;
    }
}
