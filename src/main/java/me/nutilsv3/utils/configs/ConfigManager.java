package me.nutilsv3.utils.configs;

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
import java.util.List;

public class ConfigManager {

    private static YamlConfigurationLoader configLoader;
    private static ConfigurationNode config;
    private static final String CONFIG_FILE = "plugins/nutilsv3/config.yml";

    private static YamlConfigurationLoader messagesLoader;
    private static ConfigurationNode messagesConfig;
    private static final String MESSAGES_FILE = "plugins/nutilsv3/messages.yml";

    private static final String PREFIX = "\033[1;34m[nUtils] \033[0m";
    private static final String SUCCESS = "\033[1;32m";
    private static final String WARNING = "\033[1;33m";
    private static final String ERROR = "\033[1;31m";
    private static final String RESET = "\033[0m";

    public static void loadConfig(Main plugin) {
        config = loadYamlConfig(plugin, CONFIG_FILE);
        messagesConfig = loadYamlConfig(plugin, MESSAGES_FILE);
    }


    private static ConfigurationNode loadYamlConfig(Main plugin, String filePath) {
        File file = new File(filePath);
        Path configPath = file.toPath();

        if (!file.exists()) {
            try {
                Files.createDirectories(file.getParentFile().toPath());
                InputStream resource = plugin.getClass().getClassLoader().getResourceAsStream(file.getName());

                if (resource == null) {
                    plugin.getLogger().warn("⚠️ The file " + file.getName() + " was not found, creating a new one...");
                    file.createNewFile();
                } else {
                    Files.copy(resource, configPath, StandardCopyOption.REPLACE_EXISTING);
                    plugin.getLogger().info("\033[1;32m✅ " + file.getName() + " has been created successfully!\033[0m");
                }
            } catch (IOException e) {
                plugin.getLogger().error("❌ Error while creating " + file.getName(), e);
                return null;
            }
        }

        try {
            YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(configPath).build();
            return loader.load();
        } catch (IOException e) {
            plugin.getLogger().error("❌ Error while loading " + file.getName(), e);
            return null;
        }
    }


    public static String getMessage(String key, String defaultValue) {
        if (messagesConfig == null) {
            return defaultValue;
        }
        return messagesConfig.node("messages", key).getString(defaultValue);
    }


    public static int getCooldown(String command) {
        if (config == null) return 15;
        return config.node(command, "cooldown").getInt(15);
    }


    public static List<String> getSuggestions(String request) throws SerializationException {
        if (config == null) return List.of();
        return config.node("blocked-commands").getList(String.class);
    }


    public static void reloadConfig(Main plugin) {
        config = loadYamlConfig(plugin, CONFIG_FILE);
        messagesConfig = loadYamlConfig(plugin, MESSAGES_FILE);
        logSuccess("✅ Configurations reloaded successfully!");
    }

    private static void logSuccess(String message) {
        Main.getInstance().getLogger().info(PREFIX + SUCCESS + message + RESET);
    }

    private static void logWarning(String message) {
        Main.getInstance().getLogger().warn(PREFIX + WARNING + message + RESET);
    }

    private static void logError(String message) {
        Main.getInstance().getLogger().error(PREFIX + ERROR + message + RESET);
    }

    public static String getString(String path, String defaultValue) {
        return config.node(path).getString(defaultValue);
    }

}
