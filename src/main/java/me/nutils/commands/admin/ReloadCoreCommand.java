package me.nutils.commands.admin;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import me.nutils.Main;
import me.nutils.utils.strings.CS;
import me.nutils.utils.configs.ConfigManager;
import net.kyori.adventure.text.Component;

public class ReloadCoreCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();

        ConfigManager.reloadConfig(Main.getInstance());

        sender.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("core_reload", "Configurazione ricaricata con successo!"))));
    }
}
