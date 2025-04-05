package me.nutils.utils.checker;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.nutils.Main;
import me.nutils.utils.configs.ConfigManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class UpdateChecker {

    private static final String UPDATE_URL = "https://raw.githubusercontent.com/LorenzoZ-DEV/UpdatePlugins/main/Update.json";
    private static String latestVersion = "unknown";
    private static final Gson gson = new Gson();

    public static void checkForUpdates() {
        CompletableFuture.runAsync(() -> {
            try {
                URL url = new URL(UPDATE_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "NUtils-Plugin/" + Main.getInstance().getDescription().get ( "version" ));
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    Main.getInstance().getLogger().warn ("⚠ GitHub API returned an error: HTTP " + responseCode);
                    return;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                JsonObject jsonResponse = gson.fromJson(reader, JsonObject.class);
                reader.close();

                if (jsonResponse.has("plugins")) {
                    JsonObject plugins = jsonResponse.getAsJsonObject("plugins");
                    if (plugins.has("nUtils")) {
                        JsonObject nUtils = plugins.getAsJsonObject("nUtils");
                        if (nUtils.has("latest_version")) {
                            latestVersion = nUtils.get("latest_version").getAsString();
                        }
                    }
                }

                String currentVersion = Main.getInstance().getVersion();


                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    String updateMessage = ConfigManager.getMessage("update_checker.message",
                                    "🔔 A new update is available! Current: %current_version%, New: %new_version%\n➡ Click here: %link%")
                            .replace("%current_version%", currentVersion)
                            .replace("%new_version%", latestVersion)
                            .replace("%link%", "https://www.spigotmc.org/resources/nutils.119755/");

                    Main.getInstance().getLogger().info(updateMessage);
                } else {
                    Main.getInstance().getLogger().info("✅ NUtils is up to date!");
                }

            } catch (Exception e) {
                Main.getInstance().getLogger().error ("❌ Failed to check for updates on GitHub!");
                e.printStackTrace();
            }
        });
    }

    public static String getLatestVersion() {
        return latestVersion;
    }
}
