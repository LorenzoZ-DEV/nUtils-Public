package me.nutilsv3.commands.staff;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import me.nutilsv3.Main;
import me.nutilsv3.utils.CS;
import me.nutilsv3.utils.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

import java.time.Duration;

public class ReportCommand implements SimpleCommand {

    @Override
    public void execute(Invocation invocation) {
        CommandSource sender = invocation.source();
        String[] args = invocation.arguments();

        if (!(sender instanceof Player reporter)) {
            sender.sendMessage(Component.text(CS.translate("&cOnly players can use this command!")));
            return;
        }

        if (args.length < 2) {
            reporter.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_usage", "&cUsage: /report <player> <reason>"))));
            return;
        }

        String reportedName = args[0];
        String reason = String.join(" ", args).replaceFirst(reportedName, "").trim();

        String serverName = reporter.getCurrentServer().map(server -> server.getServerInfo().getName()).orElse("Unknown");

        String titleText = CS.translate(ConfigManager.getMessage("report_title", "⚠️ New Report!"));
        String subtitleText = CS.translate(ConfigManager.getMessage("report_subtitle",
                        "&d%reporter% &7reported &c%reported% &7for &e%reason%"))
                .replace("%reporter%", reporter.getUsername())
                .replace("%reported%", reportedName)
                .replace("%reason%", reason)
                .replace("%server%", serverName);

        Title title = Title.title(
                Component.text(titleText),
                Component.text(subtitleText),
                Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500))
        );

        // Send title to all staff members
        Main.getInstance().getProxy().getAllPlayers().stream()
                .filter(p -> p.hasPermission("nutils.staff"))
                .forEach(staff -> staff.showTitle(title));

        // Confirmation message to the reporter
        reporter.sendMessage(Component.text(CS.translate(ConfigManager.getMessage("report_confirmation",
                        "&aYou reported &c%reported% &afor &e%reason%&a!"))
                .replace("%reported%", reportedName)
                .replace("%reason%", reason)));

        // Log in console
        Main.getInstance().getLogger().info(ConfigManager.getMessage("report_console",
                        "[Report] %reporter% reported %reported% for %reason%")
                .replace("%reporter%", reporter.getUsername())
                .replace("%reported%", reportedName)
                .replace("%reason%", reason));
    }
}
