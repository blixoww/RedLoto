package fr.blixow.redloto.manager;

import fr.blixow.redloto.RedLoto;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LotoStartingDelay extends BukkitRunnable {
    public void run() {
        int delay = RedLoto.getInstance().getLotoConfig().get().getInt("lotto.timer");
        double delayInMin = (double)delay / 60.0D;

        for(Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(" ");
            player.sendMessage("§8§m-------------------------------------------------");
            player.sendMessage(" ");
            player.sendMessage("§7[§RedLoto§7] Lancement du prochain Lotto, résultat dans : " + delayInMin + " mins ! Faites vos jeux... (/lotto)");
            player.sendMessage(" ");
            player.sendMessage("§8§m-------------------------------------------------");
        }

        RedLoto.gameRunning = true;
        RedLoto.lotoScheduler = new LotoScheduler();
        RedLoto.lotoMessageScheduler = new LotoMessageScheduler();
        RedLoto.getInstance().getLotoScheduler().runTaskLaterAsynchronously(RedLoto.getInstance(), (long)delay * 20L);
        int messageDelay = RedLoto.getInstance().getLotoConfig().get().getInt("lotto.messageTimer");
        RedLoto.getInstance().getLotoMessageScheduler().runTaskTimerAsynchronously(RedLoto.getInstance(), (long)messageDelay * 20L, (long)messageDelay * 20L);
    }
}
