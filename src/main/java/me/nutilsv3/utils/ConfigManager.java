package me.nutilsv3.utils;

import me.nutilsv3.Main;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

public class ConfigManager {

    private static YamlConfigurationLoader loader;
    private static ConfigurationNode config;
    private static final String CONFIG_FILE = "plugins/nutilsv3/config.yml";

    // Colori ANSI per la console
    private static final String PREFIX = "\033[1;34m[nUtils] \033[0m"; // Blu
    private static final String SUCCESS = "\033[1;32m"; // Verde
    private static final String WARNING = "\033[1;33m"; // Giallo
    private static final String ERROR = "\033[1;31m"; // Rosso
    private static final String RESET = "\033[0m"; // Reset colore

    public static void loadConfig(Main plugin) {
        File file = new File(CONFIG_FILE);
        Path configPath = file.toPath();

        if (!file.exists()) {
            try {
                Files.createDirectories(file.getParentFile().toPath());
                InputStream resource = plugin.getClass().getClassLoader().getResourceAsStream("config.yml");

                if (resource == null) {
                    plugin.getLogger().warn("⚠️ The file config.yml not found creating another one ...");
                    file.createNewFile();
                } else {
                    Files.copy(resource, configPath, StandardCopyOption.REPLACE_EXISTING);
                    plugin.getLogger().info("\033[1;32m✅ Configuration created!\033[0m");
                }
            } catch (IOException e) {
                plugin.getLogger().error("❌ Error when creating Configuration file!", e);
                return;
            }
        }

        loader = YamlConfigurationLoader.builder().path(configPath).build();

        try {
            config = loader.load();
            plugin.getLogger().info("\033[1;32m✅ Configuration Created!\033[0m");
        } catch (IOException e) {
            plugin.getLogger().error("❌ Error when creating Configuration file!", e);
        }
    }


    /**
     * Restituisce un messaggio dal config, con valore di default se non esiste.
     */
    public static String getMessage(String path, String defaultValue) {
        return config.node("messages", path).getString(defaultValue);
    }

    /**
     * Restituisce il cooldown di un comando dal config.
     */
    public static int getCooldown(String command) {
        return config.node(command, "cooldown").getInt(15);
    }

    public static List<String> getSuggestions(String request) throws SerializationException {
        return config.node("blocked-commands").getList(String.class);
    }

    /**
     * Ricarica il file di configurazione.
     */
    public static void reloadConfig() {
        try {
            config = loader.load();
            logSuccess("✅ Configurazione ricaricata con successo!");
        } catch (IOException e) {
            logError("❌ Errore nel ricaricamento della configurazione!");
            e.printStackTrace();
        }
    }

    // Funzioni di logging migliorate con colori ANSI
    private static void logSuccess(String message) {
        Main.getInstance().getLogger().info(PREFIX + SUCCESS + message + RESET);
    }

    private static void logWarning(String message) {
        Main.getInstance().getLogger().warn(PREFIX + WARNING + message + RESET);
    }

    private static void logError(String message) {
        Main.getInstance().getLogger().error(PREFIX + ERROR + message + RESET);
    }
}
