package me.nutilsv3.commands.admin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.nutilsv3.Main;
import me.nutilsv3.utils.strings.CS;
import me.nutilsv3.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

public class ReloadCoreCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();

        ConfigManager.reloadConfig(Main.getInstance());

        sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("core_reload", "Configurazione ricaricata con successo!"))));
    }
}
