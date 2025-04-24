package me.nutils.provider;

import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.EventManager;
import me.nutils.Main;
import me.nutils.commands.admin.ReloadCoreCommand;
import me.nutils.commands.staff.TpToCommand;
import me.nutils.commands.staff.follow.FollowCommand;
import me.nutils.commands.staff.reportsystem.*;
import me.nutils.commands.users.info.AntistealCommand;
import me.nutils.commands.users.messages.ReplyCommand;
import me.nutils.commands.users.report.*;
import me.nutils.commands.users.request.RequestCommand;
import me.nutils.commands.users.services.PingCommand;
import me.nutils.commands.users.messages.MessageCommand;
import me.nutils.listeners.PlayerJoinListener;
import me.nutils.listeners.StaffJoinListener;
import me.nutils.utils.configs.ConfigManager;
import me.nutils.utils.strings.CS;

public class Provider {

    private final Main plugin;

    public Provider(Main plugin) {
        this.plugin = plugin;
    }

    public void inizializza() {
        plugin.getLogger().info(CS.translate("\033[1;36m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\033[0m"));
        plugin.getLogger().info("\033[1;34m[INFO] Starting Plugins...\033[0m");

        caricaconfigurazione();
        registracommandi();
        registralisteners();

        plugin.getLogger().info("\033[1;32m[INFO] ✅ Plugin Enabled!\033[0m");
        plugin.getLogger().info(CS.translate("\033[1;36m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\033[0m"));
    }


    public void registracommandi() {
        CommandManager commandManager = plugin.getProxy().getCommandManager();

        commandManager.register("report", new ReportCommand());
        commandManager.register("request", new RequestCommand());
        commandManager.register("follow", new FollowCommand());
        commandManager.register("ping", new PingCommand());
        commandManager.register("msg", new MessageCommand());
        commandManager.register ( "r", new ReplyCommand () );
        commandManager.register ( "tpto", new TpToCommand () );
        commandManager.register("nutilsreload", new ReloadCoreCommand());
        commandManager.register("nutilscore", new AntistealCommand());

        commandManager.register("reportlist", new ReportListCommand());
        commandManager.register("reportclose", new ReportCloseCommand());
        commandManager.register("reportreopen", new ReportReopenCommand ());
        commandManager.register("reporttp", new ReportTpCommand ());
        commandManager.register("reportpunish", new ReportPunishCommand());
        commandManager.register("reportassign", new ReportAssignCommand ());
        commandManager.register("reportstats", new ReportStatsCommand ());
        commandManager.register("reportgui", new ReportGUICommand ());
        if (ConfigManager.getBoolean("commands.msg", true)) {
            commandManager.register("msg", new MessageCommand());
        }
        if (ConfigManager.getBoolean("commands.requests", true)) {
            commandManager.register("request", new RequestCommand ());
        }
        if (ConfigManager.getBoolean("commands.report", true)) {
            commandManager.register("report", new ReportCommand());
        }
        if (ConfigManager.getBoolean("commands.report_tp", true)) {
            commandManager.register("report_tp", new ReportTpCommand());
        }

        plugin.getLogger().info("\033[1;32m[INFO] ✅ Comands hooks!\033[0m");
    }

    public void registralisteners() {
        EventManager eventManager = plugin.getProxy().getEventManager();
        eventManager.register(plugin, new StaffJoinListener ());
        eventManager.register ( plugin, new PlayerJoinListener () );


    }

    public void caricaconfigurazione() {
        plugin.getLogger().info("\033[1;34m[INFO] Loading Configs...\033[0m");
        ConfigManager.loadConfig(plugin);
        plugin.getLogger().info("\033[1;32m[INFO] ✅ Config Created!\033[0m");
    }
}
