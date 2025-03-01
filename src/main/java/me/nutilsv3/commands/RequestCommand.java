package me.nutilsv3.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import me.nutilsv3.Main;
import me.nutilsv3.utils.CS;
import me.nutilsv3.utils.ConfigManager;
import net.kyori.adventure.text.Component;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.*;

public class RequestCommand implements SimpleCommand {

    private final Map<String, Long> cooldowns = new HashMap<>();

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(
                    CS.translate(ConfigManager.getMessage("request_player_only", "Devi essere un giocatore per usare questo comando!"))
            ));
            return;
        }

        if (!player.hasPermission("nutils.request")) {
            player.sendMessage(Component.text(
                    CS.translate(ConfigManager.getMessage("request_no_permission", "Non hai il permesso per eseguire questo comando."))
            ));
            return;
        }

        String[] args = invocation.arguments();

        if (args.length < 1) {
            player.sendMessage(Component.text(
                    CS.translate(ConfigManager.getMessage("request_usage", "Uso corretto: /request <messaggio>"))
            ));
            return;
        }

        // Ottenere il valore del cooldown dal config, con gestione sicura dell'errore
        int cooldownSeconds;
        try {
            cooldownSeconds = ConfigManager.getCooldown("request");
        } catch (Exception e) {
            cooldownSeconds = 15; // Valore di default in caso di errore
            Main.getInstance().getLogger().error("Errore nel caricamento del cooldown dal config.yml", e);
        }

        long currentTime = System.currentTimeMillis() / 1000L;

        if (cooldowns.containsKey(player.getUsername())) {
            long lastRequest = cooldowns.get(player.getUsername());
            long secondsLeft = (lastRequest + cooldownSeconds) - currentTime;

            if (secondsLeft > 0) {
                player.sendMessage(Component.text(CS.translate(
                        ConfigManager.getMessage("request_cooldown", "Devi attendere ancora {time}s per usare di nuovo /request.")
                                .replace("{time}", String.valueOf(secondsLeft))
                )));
                return;
            }
        }

        String message = String.join(" ", args);

        cooldowns.put(player.getUsername(), currentTime);

        Optional<ServerConnection> currentServer = player.getCurrentServer();
        String serverName = currentServer
                .map(sc -> sc.getServerInfo().getName())
                .orElse("Sconosciuto");

        String formattedMessage = ConfigManager.getMessage("request_format", "[REQUEST] {player} ha chiesto aiuto dal server {server}: {message}")
                .replace("{player}", player.getUsername())
                .replace("{server}", serverName)
                .replace("{message}", message);

        Main.getInstance().getProxy().getAllPlayers().stream()
                .filter(staff -> staff.hasPermission("nutils.receiverequest"))
                .forEach(staff -> staff.sendMessage(Component.text(CS.translate(formattedMessage))));

        player.sendMessage(Component.text(CS.translate(
                ConfigManager.getMessage("request_sent", "La tua richiesta è stata inviata allo staff!")
        )));
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!(sender instanceof Player player)) {
            return Collections.emptyList();
        }
        if (!player.hasPermission("nutils.request")) {
            return Collections.emptyList();
        }

        try {
            return ConfigManager.getSuggestions("request");
        } catch (SerializationException e) {
            Main.getInstance().getLogger().error("Errore nel caricamento dei suggerimenti dal config.yml", e);
            return Collections.emptyList();
        }
    }
}
