package me.nutilsv3;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import me.nutilsv3.provider.Provider;
import me.nutilsv3.storage.DatabaseManager;
import me.nutilsv3.utils.checker.UpdateChecker;
import me.nutilsv3.utils.checker.UpdateChecker;
import me.nutilsv3.utils.strings.CS;
import org.slf4j.Logger;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Map;

@Plugin(
        id = "nutilsv3",
        name = "nUtilsV3",
        version = BuildConstants.VERSION
)
public class Main {

    private static Main instance;
    private final ProxyServer proxy;
    private final Logger logger;

    private static final String SEPARATOR = CS.translate("\033[1;35m━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━\033[0m");

    @Inject
    public Main(ProxyServer proxy, Logger logger) {
        this.proxy = proxy;
        this.logger = logger;
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public Logger getLogger() {
        return logger;
    }


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        Provider provider = new Provider(this);
        provider.inizializza();
        DatabaseManager.connect();
        UpdateChecker.checkForUpdates();
        logger.info ( SEPARATOR );
        logger.info("\033[1;32m✅ nUtilsV3 has been successfully started!\033[0m");
        logger.info(SEPARATOR);}

    public Map<String, String> getDescription() {
        Map<String, String> description = new HashMap<> ();
        description.put("name", "nUtilsV3");
        description.put("version", BuildConstants.VERSION);
        description.put("author", "Vanixy");
        description.put("description", "A powerful utility plugin for Velocity.");
        return description;
    }
}
