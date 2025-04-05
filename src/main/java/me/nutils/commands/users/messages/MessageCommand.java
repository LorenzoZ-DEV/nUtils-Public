package me.nutils.commands.users.messages;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.nutils.Main;
import me.nutils.utils.strings.CS;
import me.nutils.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MessageCommand implements SimpleCommand {
    private static final Map<Player, Player> lastMessaged = new HashMap<>();

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("player_only", "Devi essere un giocatore per eseguire questo comando!"))));
            return;
        }

        if (args.length < 2) {
            player.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("msg_usage", "Uso corretto: /msg <player> <messaggio>"))));
            return;
        }

        Optional<Player> targetOpt = Main.getInstance().getProxy().getPlayer(args[0]);
        if (targetOpt.isEmpty()) {
            player.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("player_not_found", "Giocatore non trovato."))));
            return;
        }

        Player target = targetOpt.get();
        String message = String.join(" ", args).substring(args[0].length()).trim();

        String formattedMessage = ConfigManager.getMessage("msg_format", "&d[MSG] {sender} &8-> &d{receiver} &8» &7{message}")
                .replace("{sender}", player.getUsername())
                .replace("{receiver}", target.getUsername())
                .replace("{message}", message);

        target.sendMessage(Component.text(CS.translate(formattedMessage)));
        player.sendMessage(Component.text(CS.translate(formattedMessage)));

        lastMessaged.put(player, target);
        lastMessaged.put(target, player);
    }

    public static Optional<Player> getLastMessaged(Player player) {
        return Optional.ofNullable(lastMessaged.get(player));
    }
}