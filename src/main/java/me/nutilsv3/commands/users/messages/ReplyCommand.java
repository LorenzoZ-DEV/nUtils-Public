package me.nutilsv3.commands.users.messages;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.nutilsv3.utils.strings.CS;
import me.nutilsv3.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

import java.util.Optional;

public class ReplyCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("player_only", "Devi essere un giocatore per eseguire questo comando!"))));
            return;
        }

        Optional<Player> targetOpt = MessageCommand.getLastMessaged(player);
        if (targetOpt.isEmpty()) {
            player.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("no_reply_target", "Non hai nessuno a cui rispondere!"))));
            return;
        }

        Player target = targetOpt.get();
        if (args.length < 1) {
            player.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("reply_usage", "Uso corretto: /reply <messaggio>"))));
            return;
        }

        String message = String.join(" ", args);
        String formattedMessage = ConfigManager.getMessage("msg_format", "&d[MSG] {sender} &8-> &d{receiver} &8» &7{message}")
                .replace("{sender}", player.getUsername())
                .replace("{receiver}", target.getUsername())
                .replace("{message}", message);

        target.sendMessage(Component.text(CS.translate(formattedMessage)));
        player.sendMessage(Component.text(CS.translate(formattedMessage)));
    }
}
