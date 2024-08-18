package fr.blixow.redloto;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LotoConfig {
    private final String path = RedLoto.getInstance().getDataFolder().getPath();
    private File folder;
    private File configFile;
    private FileConfiguration configuration;

    public LotoConfig() {
        this.checkFolder();
        this.checkFile();
    }

    private void checkFolder() {
        if (this.folder == null) {
            this.folder = new File(this.path);
            if (this.folder.exists()) {
                RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Config folder is ready !");
            } else {
                this.folder.mkdir();
                RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Config folder is create...");
            }
        }

    }

    private void checkFile() {
        RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Check config file !");
        this.configFile = new File(this.path, "LotoConfig.yml");
        if (this.configFile.exists()) {
            RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Config file exist ! Loading...");
            this.configuration = YamlConfiguration.loadConfiguration(this.configFile);
        } else {
            RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Config file not exist ! Create...");

            try {
                this.configFile.createNewFile();
                RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Config file is create !");
                this.configuration = YamlConfiguration.loadConfiguration(this.configFile);
                RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Config file is load ! Writing...");
                this.writeConfig();
                RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Config is write !");
                this.saveConfig();
            } catch (IOException var2) {
                RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Error with config file !");
                var2.printStackTrace();
            }
        }

    }

    private void writeConfig() {
        this.configuration.set("loto.multiplier", 8);
        this.configuration.set("loto.cardValue", 50);
        this.configuration.set("loto.maxCard", 5);
        this.configuration.set("loto.winRate", 0.2D);
        this.configuration.set("loto.timer", 600);
        this.configuration.set("loto.delay", 3600);
        this.configuration.set("loto.messageTimer", 300);
    }

    public void saveConfig() {
        RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Save running config into file !");

        try {
            this.configuration.save(this.configFile);
            RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Running config is save !");
        } catch (IOException var2) {
            RedLoto.getLotoLogger().info("[" + RedLoto.getLotoLogger().getName() + "] Error when config save !");
        }

    }

    public FileConfiguration get() {
        return this.configuration;
    }
}
