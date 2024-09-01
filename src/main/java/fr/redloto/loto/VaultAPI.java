package fr.redloto.loto;

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
        if (!setupEconomy()) {
            RedLoto.getLotoLogger().severe("Vault not found ! CelestiamLotto is disable...");
            this.pluginManager.disablePlugin(RedLoto.getInstance());
        }
    }

    private boolean setupEconomy() {
        if (this.pluginManager.getPlugin("Vault") == null)
            return false;
        RegisteredServiceProvider<Economy> rsp =
                this.server.getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (rsp != null) {
            this.econ = rsp.getProvider();
            if (this.econ != null)
                return true;
        }
        return false;
    }

    public Economy getEconomy() {
        return this.econ;
    }


}
