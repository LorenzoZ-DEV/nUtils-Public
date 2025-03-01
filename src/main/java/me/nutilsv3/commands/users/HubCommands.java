package me.nutilsv3.commands.users;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.nutilsv3.Main;
import me.nutilsv3.utils.CS;
import me.nutilsv3.utils.ConfigManager;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Optional;

public class HubCommands implements SimpleCommand {

    public HubCommands() {
        // Costruttore vuoto
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("player_only", "Devi essere un giocatore per eseguire questo comando!"))));
            return;
        }

        connectToLobby(player);
    }

    public static void connectToLobby(Player player) {
        // Ottieni la lista dei server lobby dal config.yml
        List<String> lobbyServers = ConfigManager.getLobbyServers();
        if (lobbyServers.isEmpty()) {
            player.sendMessage(Component.text(CS.translate("&cNessun server lobby è stato configurato!")));
            return;
        }

        // Se il giocatore è già in una lobby, non deve essere teletrasportato
        Optional<String> currentServer = player.getCurrentServer().map(s -> s.getServerInfo().getName());
        if (currentServer.isPresent() && lobbyServers.contains(currentServer.get())) {
            player.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("already_in_lobby", "Sei già in un server lobby!"))));
            return;
        }

        // Trova il primo server disponibile dalla lista
        for (String serverName : lobbyServers) {
            Optional<RegisteredServer> targetServer = Main.getInstance().getProxy().getServer(serverName);
            if (targetServer.isPresent()) {
                player.createConnectionRequest(targetServer.get()).fireAndForget();
                player.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("hub_teleport", "Ti stai teletrasportando all'hub..."))));
                return;
            }
        }

        // Se nessun server lobby è disponibile
        player.sendMessage(Component.text(CS.translate("&cErrore! Nessun server hub disponibile al momento.")));
    }
}
