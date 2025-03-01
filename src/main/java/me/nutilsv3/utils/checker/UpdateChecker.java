package me.nutilsv3.utils.checker;

import me.nutilsv3.Main;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker {

    private static final int RESOURCE_ID = 119755; // ID del plugin su Spigot

    public static void checkForUpdates() {
        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=" + RESOURCE_ID);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = reader.readLine();
                reader.close();

                if (response == null || response.isEmpty()) {
                    Main.getInstance().getLogger().warn("§cUnable to fetch update information from Spigot.");
                    return;
                }

                String latestVersion = response.split("\"current_version\":\"")[1].split("\"")[0];
                String currentVersion = Main.getInstance().getDescription().get("version");

                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    Main.getInstance().getLogger().info("§eA new version of NUtils is available: " + latestVersion);
                    Main.getInstance().getLogger().info("§eDownload it here: §bhttps://www.spigotmc.org/resources/nutils.119755/");
                } else {
                    Main.getInstance().getLogger().info("§aNUtils is up to date!");
                }

            } catch (Exception e) {
                Main.getInstance().getLogger().error("§cFailed to check for updates on Spigot!", e);
            }
        });
    }
}
