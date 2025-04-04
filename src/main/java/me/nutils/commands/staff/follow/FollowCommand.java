package me.nutils.commands.staff.follow;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import me.nutils.Main;
import me.nutils.utils.strings.CS;
import me.nutils.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public class FollowCommand implements SimpleCommand {

    private static final String PREFIX = CS.translate("&r");

    public FollowCommand() {
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!(sender instanceof Player executor)) {
            sender.sendMessage(Component.text(CS.translate(PREFIX + "&cSolo i giocatori possono eseguire questo comando!")));
            return;
        }

        if (!executor.hasPermission("nutils.follow")) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("no_permission", "Non hai il permesso per eseguire questo comando."))));
            return;
        }

        switch (args.length) {
            case 0:
                executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("follow_usage", "/follow <player>"))));
                break;

            case 1:
                handleFollow(executor, args[0]);
                break;

            default:
                executor.sendMessage(Component.text(CS.translate(PREFIX + "&cTroppi argomenti! Utilizzo corretto: /follow <player>")));
                break;
        }
    }

    private void handleFollow(Player executor, String targetName) {
        Optional<Player> optionalTarget = Main.getInstance().getProxy().getPlayer(targetName);

        Player target = optionalTarget.orElse(null);
        if (target == null) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("player_not_found", "Giocatore non trovato."))));
            return;
        }

        if (executor.getUsername().equalsIgnoreCase(target.getUsername())) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("follow_self", "Non puoi seguire te stesso."))));
            return;
        }

        Optional<ServerConnection> targetServerOptional = target.getCurrentServer();
        if (targetServerOptional.isEmpty()) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("target_no_server", "Il giocatore non è attualmente connesso a un server."))));
            return;
        }

        ServerConnection targetServer = targetServerOptional.get();

        Optional<ServerConnection> executorServerOptional = executor.getCurrentServer();
        if (executorServerOptional.isPresent() &&
                executorServerOptional.get().getServerInfo().getName().equalsIgnoreCase(targetServer.getServer().getServerInfo().getName())) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("already_in_server", "Sei già nello stesso server del giocatore!"))));
            return;
        }

        Optional<RegisteredServer> targetRegisteredServer = Main.getInstance().getProxy().getServer(targetServer.getServer().getServerInfo().getName());
        if (targetRegisteredServer.isEmpty()) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("server_not_found", "Il server di destinazione non è stato trovato."))));
            return;
        }

        executor.createConnectionRequest(targetRegisteredServer.get()).fireAndForget();
        executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("following", "Stai seguendo {player}.").replace("{player}", target.getUsername()))));
    }
}
