package fr.blixow.redloto.manager;

import fr.blixow.redloto.RedLoto;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LotoStartingDelay extends BukkitRunnable {
    public void run() {
        int delay = RedLoto.getInstance().getLotoConfig().get().getInt("loto.timer");
        double delayInMin = (double) delay / 60.0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(" ");
            player.sendMessage("§8§m-------------------------------------------------");
            player.sendMessage(" ");
            player.sendMessage("§7[§RedLoto§7] Lancement du prochain Loto, résultat dans : " + delayInMin + " mins ! Faites vos jeux... (/loto)");
            player.sendMessage(" ");
            player.sendMessage("§8§m-------------------------------------------------");
        }
        RedLoto.gameRunning = true;
        RedLoto.lotoScheduler = new LotoScheduler();
        RedLoto.lotoMessageScheduler = new LotoMessageScheduler();
        RedLoto.getInstance().getLotoScheduler().runTaskLaterAsynchronously(RedLoto.getInstance(), (long) delay * 20L);
        int messageDelay = RedLoto.getInstance().getLotoConfig().get().getInt("loto.messageTimer");
        RedLoto.getInstance().getLotoMessageScheduler().runTaskTimerAsynchronously(RedLoto.getInstance(), (long) messageDelay * 20L, (long) messageDelay * 20L);
    }
}