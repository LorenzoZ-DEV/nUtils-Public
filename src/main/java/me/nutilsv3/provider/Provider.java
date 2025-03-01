package me.nutilsv3.provider;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandManager;
import me.nutilsv3.Main;
import me.nutilsv3.commands.*;
import me.nutilsv3.commands.admin.ReloadCoreCommand;
import me.nutilsv3.commands.staff.FollowCommand;
import me.nutilsv3.commands.users.PingCommand;
import me.nutilsv3.commands.users.messages.MessageCommand;
import me.nutilsv3.utils.ConfigManager;
import me.nutilsv3.utils.CS;

public class Provider {

    private final Main plugin;

    public Provider(Main plugin) {
        this.plugin = plugin;
    }

    public void inizializza() {
        plugin.getLogger().info(CS.translate("\033[1;36m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\033[0m"));
        plugin.getLogger().info("\033[1;34m[INFO] Inizializzazione del plugin...\033[0m");

        caricaconfigurazione();
        registracommandi();
        registralisteners();

        plugin.getLogger().info("\033[1;32m[INFO] ✅ Plugin completamente avviato!\033[0m");
        plugin.getLogger().info(CS.translate("\033[1;36m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\033[0m"));
    }

    /**
     * 📌 Registra i comandi
     */
    public void registracommandi() {
        CommandManager commandManager = plugin.getProxy().getCommandManager();

        commandManager.register("report", new ReportCommand());
        commandManager.register("request", new RequestCommand());
        commandManager.register("follow", new FollowCommand());
        commandManager.register("ping", new PingCommand());
        commandManager.register("nutilsreload", new ReloadCoreCommand());
        commandManager.register("nutilscore", new AntistealCommand());
        commandManager.register("msg", new MessageCommand ());

        // ✅ Aggiunta alias per hub e staffchat

        plugin.getLogger().info("\033[1;32m[INFO] ✅ Comandi registrati con successo!\033[0m");
    }

    /**
     * 🔊 Registra gli eventi
     */
    public void registralisteners() {
// Aggiunto il listener per AdminChat

    }

    /**
     * 📄 Carica il file di configurazione
     */
    public void caricaconfigurazione() {
        plugin.getLogger().info("\033[1;34m[INFO] Caricamento configurazione...\033[0m");
        ConfigManager.loadConfig(plugin);
        plugin.getLogger().info("\033[1;32m[INFO] ✅ Configurazione caricata con successo!\033[0m");
    }
}
