package me.nutils.commands.users.info;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.command.CommandSource;
import net.kyori.adventure.text.Component;
import me.nutils.utils.strings.CS;

public class AntistealCommand implements SimpleCommand {

    public AntistealCommand() {
    }

    @Override
    public void execute(Invocation invocation) {
        CommandSource p = invocation.source();

        p.sendMessage(Component.text(CS.translate("&#555555&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")));
        p.sendMessage(Component.text(CS.translate("&#d633ffThis server Use &#ff66ccNUtils for Velocity&#aaaaaa.")));
        p.sendMessage(Component.text(CS.translate("&#555555Spigot Link: &#d633ffhttps://www.spigotmc.org/resources/nutils.119755/")));
        p.sendMessage(Component.text(CS.translate("&#555555&m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")));
    }
}
