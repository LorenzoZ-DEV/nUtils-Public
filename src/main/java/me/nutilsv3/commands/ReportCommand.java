package me.nutilsv3.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import me.nutilsv3.Main;
import me.nutilsv3.utils.CS;
import me.nutilsv3.utils.ConfigManager;
import net.kyori.adventure.text.Component;

import java.util.*;

public class ReportCommand implements SimpleCommand {

    private final HashMap<String, Long> cooldowns = new HashMap<>();
    private static final String PREFIX = CS.translate("&r");

    @Override
    public void execute(SimpleCommand.Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        // Controllo se il comando è stato eseguito da un player
        if (!(sender instanceof Player executor)) {
            sender.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("player_only", "Devi essere un giocatore per eseguire questo comando!"))));
            return;
        }

        // Controllo permessi
        if (!executor.hasPermission("nutils.report")) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("no_permission", "Non hai il permesso per eseguire questo comando."))));
            return;
        }

        // Switch per la gestione degli argomenti
        switch (args.length) {
            case 0, 1 -> executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("usage", "/report <player> <motivo>"))));

            case 2 -> handleReport(executor, args[0], String.join(" ", Arrays.copyOfRange(args, 1, args.length)));

            default -> executor.sendMessage(Component.text(CS.translate(PREFIX + "&cTroppi argomenti! Utilizzo corretto: /report <player> <motivo>")));
        }
    }

    private void handleReport(Player executor, String targetName, String reason) {
        Optional<Player> optionalTarget = Main.getInstance().getProxy().getPlayer(targetName);
        Player targetPlayer = optionalTarget.orElse(null);

        // Se il giocatore non è online
        if (targetPlayer == null) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("player_not_found", "Giocatore non trovato."))));
            return;
        }

        // Se il giocatore sta cercando di segnalare sé stesso
        if (executor.getUsername().equalsIgnoreCase(targetPlayer.getUsername())) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("self_report", "Non puoi reportare te stesso."))));
            return;
        }

        // Controllo cooldown
        int cooldown = ConfigManager.getCooldown("report");
        if (cooldowns.containsKey(executor.getUsername())) {
            long secondsLeft = (cooldowns.get(executor.getUsername()) / 1000L + cooldown) - (System.currentTimeMillis() / 1000L);
            if (secondsLeft > 0) {
                executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("cooldown_wait", "Attendi ancora {time} secondi.")
                        .replace("{time}", String.valueOf(secondsLeft)))));
                return;
            }
        }

        // Controllo se il giocatore target è in un server
        Optional<ServerConnection> targetServer = targetPlayer.getCurrentServer();
        if (targetServer.isEmpty()) {
            executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("server_not_found", "Il giocatore non è collegato a un server."))));
            return;
        }

        // Formattazione del messaggio di segnalazione
        String finalMessage = CS.translate(ConfigManager.getMessage("report_format", "{executor} ha segnalato {target} per {reason}")
                .replace("{executor}", executor.getUsername())
                .replace("{target}", targetPlayer.getUsername())
                .replace("{reason}", reason));

        // Invio della segnalazione agli staff con permesso "nutils.receivereport"
        Main.getInstance().getProxy().getAllPlayers().stream()
                .filter(p -> p.hasPermission("nutils.receivereport"))
                .forEach(staff -> staff.sendMessage(Component.text(finalMessage)));

        // Impostiamo il cooldown
        cooldowns.put(executor.getUsername(), System.currentTimeMillis());

        // Messaggio di conferma al giocatore
        executor.sendMessage(Component.text(CS.translate(PREFIX + ConfigManager.getMessage("report_sent", "Segnalazione inviata."))));
    }
}
