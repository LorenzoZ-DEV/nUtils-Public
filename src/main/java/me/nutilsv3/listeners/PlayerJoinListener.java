package me.nutilsv3.listeners;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import me.nutilsv3.Main;
import me.nutilsv3.utils.checker.UpdateChecker;
import net.kyori.adventure.text.Component;

public class PlayerJoinListener
{
    @Subscribe
    public void onPlayerJoin(PostLoginEvent event){
        Player player = event.getPlayer();

        if(!player.hasPermission("nutils.update")){
            return;
        }

        String latestVersion = UpdateChecker.getLatestVersion ();
        String currentVersion = "2.12.1 ";

        if(Main.getInstance ( ).getDescription ().containsKey ( "version" )){
            currentVersion = Main.getInstance ( ).getDescription ().get ( "version" );
        }

        if (!currentVersion.equalsIgnoreCase(latestVersion)) {
            player.sendMessage( Component.text("🔔 A new version of NUtils is available: " + latestVersion));
            player.sendMessage(Component.text("➡ Download it here: https://www.spigotmc.org/resources/nutils.119755/"));
        }
    }
}
