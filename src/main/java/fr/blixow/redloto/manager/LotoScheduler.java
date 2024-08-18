package fr.blixow.redloto.manager;

import fr.blixow.redloto.RedLoto;
import fr.blixow.redloto.card.LotoCard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LotoScheduler extends BukkitRunnable {
    public void run() {
        RedLoto.gameRunning = false;
        RedLoto.getLotoLogger().info(RedLoto.getLotoLogger().getName() + " Starting loto pickup...");
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage("§7[§RedLoto§7] Tirage en cours...");
        }
        RedLoto.getInstance().closeLotoInventory();
        if (RedLoto.getInstance().haveAWinner()) {
            LotoCard card = RedLoto.getInstance().selectWinner();
            int gain = card.getCardValue() * RedLoto.getInstance().getLotoConfig().get().getInt("loto.multiplier");
            Player player = Bukkit.getPlayer(card.getOwnerName());
            String moneyName = RedLoto.getInstance().getVaultAPI().getEconomy().currencyNamePlural();
            player.sendMessage("§7[§RedLoto§7] Vous avez gagné ce tirage ! Gain de " + gain + " " + moneyName + ".");
            RedLoto.getLotoLogger().info("Winner is " + player.getDisplayName() + " with " + gain + moneyName + ".");
            RedLoto.getInstance().getVaultAPI().getEconomy().depositPlayer(player, gain);
            for (Player itPlayer : Bukkit.getOnlinePlayers()) {
                itPlayer.sendMessage("§7[§RedLoto§7] Le tirage a été remporté par " + player.getDisplayName() + " ! §7Gain de §c" + gain + " " + moneyName + "§7.");
            }
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage("§7[§RedLoto§7] Le tirage n'a pas trouvé de gagnant pour cette edition !");
            }
        }
        RedLoto.getInstance().clearSession();
        int delay = RedLoto.getInstance().getLotoConfig().get().getInt("lotto.delay");
        if (RedLoto.getInstance().getLotoStartingDelay() != null) {
            RedLoto.lotoStartingDelay = new LotoStartingDelay();
        }
        if (RedLoto.getInstance().getLotoStartingDelay() != null) {
            RedLoto.getInstance().getLotoStartingDelay().runTaskLaterAsynchronously(RedLoto.getInstance(), (long) delay * 20L);
        }
        this.cancel();
    }
}
