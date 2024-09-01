package fr.redloto.loto;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;

public class RedLoto extends JavaPlugin {

    private static final Logger logger = Logger.getLogger("RedLoto");;
    private static RedLoto instance;
    private static VaultAPI vaultAPI;
    public static LotoScheduler lotoScheduler;
    public static LotoStartingDelay lotoStartingDelay;
    public static LotoMessageScheduler lotoMessageScheduler;
    public static boolean gameRunning;
    private ArrayList<LotoCard> inGameCards;
    private ArrayList<LotoPlayer> lotoPlayers;
    private CommandMap bukkitCommandMap;
    private LotoConfig lotoConfig;

    @Override
    public void onLoad() {
        RedLoto.instance = this;
        this.inGameCards = new ArrayList<>();
        this.lotoPlayers = new ArrayList<>();
    }

    @Override
    public void onEnable() {
        RedLoto.vaultAPI = new VaultAPI(getServer());
        if (RedLoto.vaultAPI.getEconomy() != null)
            RedLoto.logger.info("["+logger.getName()+"] Economy is now setup !");
        else
            return;
        this.lotoConfig = new LotoConfig();
        Bukkit.getPluginManager().registerEvents(new LotoInventoryEvent(), this);
        this.initServerCommandMap();
        this.registerCommands(new LotoCommand("loto"), "loto");
        RedLoto.lotoScheduler = new LotoScheduler();
        RedLoto.lotoStartingDelay = new LotoStartingDelay();
        RedLoto.lotoMessageScheduler = new LotoMessageScheduler();
        long delay = RedLoto.getInstance().getLotoConfig().get().getInt("loto.delay");
        RedLoto.lotoStartingDelay.runTaskLaterAsynchronously(this, delay*20L);
    }

    @Override
    public void onDisable() {
        for (LotoPlayer lotoPlayer : this.lotoPlayers) {
            OfflinePlayer offlinePlayer = Bukkit.getPlayer(lotoPlayer.getOwnerName());
            if (offlinePlayer != null && offlinePlayer.isOnline()) {
                Player player = (Player) offlinePlayer;
                InventoryView inventory = player.getOpenInventory();
                if (inventory != null) {
                    if (inventory.getTitle().contains("Loto"))
                        player.closeInventory();
                }
            }
            int refundValue = 0;
            for (LotoCard card : lotoPlayer.getCards())
                refundValue+=card.getCardValue();
            if (refundValue>=100)
                refundValue-=100;
            this.getVaultAPI().getEconomy().depositPlayer(offlinePlayer, refundValue);
        }
        clearSession();
        if (RedLoto.gameRunning) {
            RedLoto.lotoScheduler.cancel();
            RedLoto.lotoMessageScheduler.cancel();
        } else
            RedLoto.lotoStartingDelay.cancel();

    }

    public boolean isPlayerInPlaying(Player player) {
        for (LotoPlayer lotoPlayer : this.lotoPlayers) {
            if (player.getName().equals(lotoPlayer.getOwnerName()))
                return true;
        }
        return false;
    }

    public LotoPlayer getLotoPlayer(String ownerName) {
        for (LotoPlayer lotoPlayer : this.lotoPlayers) {
            if (ownerName.equals(lotoPlayer.getOwnerName())) {
                return lotoPlayer;
            }
        }
        return null;
    }

    public void registerCommands(Command command, String label) {
        if (this.bukkitCommandMap != null) {
            if (!this.bukkitCommandMap.register(label, command))
                RedLoto.logger.severe("["+logger.getName()+"] Command "+label+" cannot be register in command map !");
            else
                RedLoto.logger.info("["+logger.getName()+"] Command "+label+ " is now register !");
        } else {
            RedLoto.getLotoLogger().severe("["+logger.getName()+"] Command cannot be register ! Command Map is not initiate..");
        }
    }

    public void initServerCommandMap() {
        try {
            RedLoto.logger.info("["+logger.getName()+"] Register bukkit command map...");
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            this.bukkitCommandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            RedLoto.logger.info("["+logger.getName()+"] Map is now set accessible and can be use !");
        } catch (NoSuchFieldException | IllegalAccessException exception) {
            RedLoto.logger.severe("["+logger.getName()+"] Bukkit command map cannot be accessible !");
            exception.printStackTrace();
        }
    }

    public boolean haveAWinner() {
        int nbCardsInGame = this.inGameCards.size();
        double winRate = this.lotoConfig.get().getDouble("loto.winRate")*100;
        RedLoto.getLotoLogger().info("WinRate: "+winRate);
        if (nbCardsInGame <= 0)
            return false;
        if (nbCardsInGame < winRate) {
            return new Random().nextInt(100) <= winRate;
        } else {
            return true;
        }
    }

    public LotoCard selectWinner() {
        int nbCardInGame = this.inGameCards.size();
        int randomisedCard = new Random().nextInt(nbCardInGame);
        return this.inGameCards.get(randomisedCard);
    }

    public void closeLotoInventory() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryView inventory = player.getOpenInventory();
            if (inventory != null) {
                if (inventory.getTitle().equals("Loto")) {
                    inventory.close();
                } else if (inventory.getTitle().contains("Loto Carte :")) {
                    inventory.close();
                }
            }
        }
    }

    public final int newCardID() {
        return this.inGameCards.size()+1;
    }

    public LotoConfig getLotoConfig() {
        return lotoConfig;
    }

    public void addInGameCard(LotoCard card) {
        this.inGameCards.add(card);
    }

    public LotoScheduler getLotoScheduler() {
        return lotoScheduler;
    }

    public LotoStartingDelay getLotoStartingDelay() {
        return lotoStartingDelay;
    }

    public LotoMessageScheduler getLotoMessageScheduler() {
        return lotoMessageScheduler;
    }

    public ArrayList<LotoPlayer> getLotoPlayersArrays() {
        return this.lotoPlayers;
    }

    public static final Logger getLotoLogger() {
        return logger;
    }

    public static final RedLoto getInstance() {
        return RedLoto.instance;
    }

    public final VaultAPI getVaultAPI() { return RedLoto.vaultAPI; }

    public void clearSession() {
        this.inGameCards.clear();
        this.lotoPlayers.clear();
    }
}
