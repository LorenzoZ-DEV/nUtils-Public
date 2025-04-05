package me.nutils.commands.users.services;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.nutils.Main;
import me.nutils.utils.strings.CS;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

import java.util.Optional;

public class PingCommand implements SimpleCommand {

    public PingCommand() {
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(CS.translate("&cSolo i giocatori possono eseguire questo comando!")));
            return;
        }

        switch (args.length) {
            case 0:
                sendPingMessage(player, player);
                break;

            case 1:
                Optional<Player> optionalTarget = Main.getInstance().getProxy().getPlayer(args[0]);

                if (optionalTarget.isEmpty()) {
                    player.sendMessage(Component.text(CS.translate("&5&lNINFEA&8 » &cGiocatore non trovato.")));
                } else {
                    sendPingMessage(player, optionalTarget.get());
                }
                break;

            default:
                player.sendMessage(Component.text(CS.translate("&5&lNINFEA&8 » &7Utilizzo: &d/ping [giocatore]")));
        }
    }

    private void sendPingMessage(Player sender, Player target) {
        long ping = target.getPing();
        String targetName = target.getUsername();

        if (ping <= 0) {
            sender.sendMessage(Component.text(CS.translate("&5&lNINFEA&8 » &7Stiamo calcolando la latenza, attendi altri secondi.")));
            return;
        }

        String message = (target.equals(sender))
                ? "La tua latenza è di " + ping + "ms."
                : "La latenza di " + targetName + " è di " + ping + "ms.";

        sender.sendMessage(CS.gradientText(message, TextColor.color(0x8A2BE2), TextColor.color(0xD633FF)));
    }
}
