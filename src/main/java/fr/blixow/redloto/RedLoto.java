package fr.blixow.redloto;

import fr.blixow.redloto.card.LotoCard;
import fr.blixow.redloto.economy.VaultAPI;
import fr.blixow.redloto.event.LotoInventoryEvent;
import fr.blixow.redloto.manager.LotoMessageScheduler;
import fr.blixow.redloto.manager.LotoPlayer;
import fr.blixow.redloto.manager.LotoScheduler;
import fr.blixow.redloto.manager.LotoStartingDelay;
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

public final class RedLoto extends JavaPlugin {
    private static final Logger logger = Logger.getLogger("RedLoto");
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

    public void onLoad() {
        instance = this;
        this.inGameCards = new ArrayList<>();
        this.lotoPlayers = new ArrayList<>();
    }

    public void onEnable() {
        vaultAPI = new VaultAPI(this.getServer());
        if (vaultAPI.getEconomy() != null) {
            logger.info("[" + logger.getName() + "] Economy is now setup !");
            this.lotoConfig = new LotoConfig();
            Bukkit.getPluginManager().registerEvents(new LotoInventoryEvent(), this);
            this.initServerCommandMap();
            this.registerCommands(new LotoCommand("loto"), "loto");
            lotoScheduler = new LotoScheduler();
            lotoStartingDelay = new LotoStartingDelay();
            lotoMessageScheduler = new LotoMessageScheduler();
            long delay = (long) getInstance().getLotoConfig().get().getInt("lotto.delay");
            lotoStartingDelay.runTaskLaterAsynchronously(this, delay * 20L);
        }
    }

    public void onDisable() {
        for (LotoPlayer lottoPlayer : this.lotoPlayers) {
            OfflinePlayer offlinePlayer = Bukkit.getPlayer(lottoPlayer.getOwnerName());
            if (offlinePlayer != null && offlinePlayer.isOnline()) {
                Player player = (Player) offlinePlayer;
                InventoryView inventory = player.getOpenInventory();
                if (inventory != null && inventory.getTitle().contains("Lotto")) {
                    player.closeInventory();
                }
            }

            int refundValue = 0;

            for (LotoCard card : lottoPlayer.getCards()) {
                refundValue += card.getCardValue();
            }

            if (refundValue >= 100) {
                refundValue -= 100;
            }

            this.getVaultAPI().getEconomy().depositPlayer(offlinePlayer, (double) refundValue);
        }

        this.clearSession();
        lotoScheduler.cancel();
        lotoStartingDelay.cancel();
        lotoMessageScheduler.cancel();
    }

    public boolean isPlayerInPlaying(Player player) {
        for (LotoPlayer lottoPlayer : this.lotoPlayers) {
            if (player.getName().equals(lottoPlayer.getOwnerName())) {
                return true;
            }
        }

        return false;
    }

    public LotoPlayer getLottoPlayer(String ownerName) {
        for (LotoPlayer lottoPlayer : this.lotoPlayers) {
            if (ownerName.equals(lottoPlayer.getOwnerName())) {
                return lottoPlayer;
            }
        }

        return null;
    }

    public void registerCommands(Command command, String label) {
        if (this.bukkitCommandMap != null) {
            if (!this.bukkitCommandMap.register(label, command)) {
                logger.severe("[" + logger.getName() + "] Command " + label + " cannot be register in command map !");
            } else {
                logger.info("[" + logger.getName() + "] Command " + label + " is now register !");
            }
        } else {
            getLotoLogger().severe("[" + logger.getName() + "] Command cannot be register ! Command Map is not initiate..");
        }

    }

    public void initServerCommandMap() {
        try {
            logger.info("[" + logger.getName() + "] Register bukkit command map...");
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            this.bukkitCommandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            logger.info("[" + logger.getName() + "] Map is now set accessible and can be use !");
        } catch (IllegalAccessException | NoSuchFieldException var2) {
            logger.severe("[" + logger.getName() + "] Bukkit command map cannot be accessible !");
            var2.printStackTrace();
        }

    }

    public boolean haveAWinner() {
        int nbCardsInGame = this.inGameCards.size();
        double winRate = this.lotoConfig.get().getDouble("lotto.winRate") * 100.0D;
        getLotoLogger().info("WinRate: " + winRate);
        if (nbCardsInGame <= 0) {
            return false;
        } else if ((double) nbCardsInGame < winRate) {
            return (double) (new Random()).nextInt(100) <= winRate;
        } else {
            return true;
        }
    }

    public LotoCard selectWinner() {
        int nbCardInGame = this.inGameCards.size();
        int randomisedCard = (new Random()).nextInt(nbCardInGame);
        return (LotoCard) this.inGameCards.get(randomisedCard);
    }

    public void closeLottoInventory() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryView inventory = player.getOpenInventory();
            if (inventory != null) {
                if (inventory.getTitle().equals("Lotto")) {
                    inventory.close();
                } else if (inventory.getTitle().contains("Lotto Carte :")) {
                    inventory.close();
                }
            }
        }

    }

    public final int newCardID() {
        return this.inGameCards.size() + 1;
    }

    public LotoConfig getLotoConfig() {
        return this.lotoConfig;
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

    public static Logger getLotoLogger() {
        return logger;
    }

    public static RedLoto getInstance() {
        return instance;
    }

    public final VaultAPI getVaultAPI() {
        return vaultAPI;
    }

    public void clearSession() {
        this.inGameCards.clear();
        this.lotoPlayers.clear();
    }
}