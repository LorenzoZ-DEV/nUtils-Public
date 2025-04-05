package me.nutils.commands.staff;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import me.nutils.Main;
import me.nutils.utils.strings.CS;
import me.nutils.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class TpToCommand implements SimpleCommand {

    private static final String PREFIX = CS.translate("&r");
    private static final MinecraftChannelIdentifier TELEPORT_CHANNEL = MinecraftChannelIdentifier.from("nutils:teleport");

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player executor)) {
            source.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("console_error", "&cThis command can only be executed by players!"))));
            return;
        }

        if (!executor.hasPermission("nutils.tpto")) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("no_permission", "&cYou do not have permission to use this command."))));
            return;
        }

        if (args.length != 1) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("tpto_usage", "&cUsage: /tpto <player>"))));
            return;
        }

        String targetName = args[0];
        Optional<Player> optionalTarget = Main.getInstance().getProxy().getPlayer(targetName);

        if (optionalTarget.isEmpty()) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("player_not_found", "&cPlayer not found."))));
            return;
        }

        Player target = optionalTarget.get();

        if (executor.getUsername().equalsIgnoreCase(target.getUsername())) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("tpto_self", "&cYou cannot teleport to yourself."))));
            return;
        }

        Optional<ServerConnection> targetServerOpt = target.getCurrentServer();
        if (targetServerOpt.isEmpty()) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("target_no_server", "&cThe target is not connected to any server."))));
            return;
        }

        ServerConnection targetServer = targetServerOpt.get();
        RegisteredServer registeredServer = targetServer.getServer();

        // First, connect the player to the target's server
        executor.createConnectionRequest(registeredServer).connect().thenAccept(result -> {
            if (result.isSuccessful()) {
                // After connection, send teleport plugin message
                String teleportCommand = String.format("teleport %s %s", executor.getUsername(), target.getUsername());
                byte[] dataBytes = teleportCommand.getBytes(StandardCharsets.UTF_8);

                executor.getCurrentServer().ifPresent(conn -> {
                    conn.sendPluginMessage(TELEPORT_CHANNEL, dataBytes);
                });

                executor.sendMessage(Component.text(CS.translate(PREFIX +
                        ConfigManager.getMessage("tpto_success", "&aTeleporting to &e{player}&a's position.")
                                .replace("{player}", target.getUsername()))));
            } else {
                executor.sendMessage(Component.text(CS.translate(PREFIX +
                        ConfigManager.getMessage("tpto_failed", "&cFailed to teleport to {player}'s server.")
                                .replace("{player}", target.getUsername()))));
            }
        });
    }
}