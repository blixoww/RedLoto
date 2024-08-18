package fr.blixow.redloto.economy;

import fr.blixow.redloto.RedLoto;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultAPI {
    private Economy econ;
    private final PluginManager pluginManager;
    private final Server server;

    public VaultAPI(Server server) {
        this.server = server;
        this.pluginManager = server.getPluginManager();
        if (!this.setupEconomy()) {
            RedLoto.getLotoLogger().severe("Vault not found ! RedLoto is disable...");
            this.pluginManager.disablePlugin(RedLoto.getInstance());
        }

    }

    private boolean setupEconomy() {
        if (this.pluginManager.getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider<Economy> rsp = this.server.getServicesManager().getRegistration(Economy.class);
            if (rsp != null) {
                this.econ = (Economy)rsp.getProvider();
                return this.econ != null;
            }

            return false;
        }
    }

    public Economy getEconomy() {
        return this.econ;
    }
}
