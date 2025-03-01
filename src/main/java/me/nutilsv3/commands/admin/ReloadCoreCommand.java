package me.nutilsv3.commands.admin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.nutilsv3.utils.CS;
import me.nutilsv3.utils.ConfigManager;
import net.kyori.adventure.text.Component;

public class ReloadCoreCommand implements SimpleCommand {

    public ReloadCoreCommand() {
        // Costruttore vuoto
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        ConfigManager.reloadConfig();

        // Usa Component.text() per inviare il messaggio correttamente
        sender.sendMessage(Component.text( CS.translate ( ConfigManager.getMessage ( "core_reload", "Configurazione ricaricata con successo!" ))));
    }
}
