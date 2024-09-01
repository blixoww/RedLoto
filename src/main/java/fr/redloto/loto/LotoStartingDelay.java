package fr.redloto.loto;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LotoStartingDelay extends BukkitRunnable {


    @Override
    public void run() {
        int delay = RedLoto.getInstance().getLotoConfig().get().getInt("lotto.timer");
        double delayInMin = (double)delay/60;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(" ");
            player.sendMessage("§8§m-------------------------------------------------");
            player.sendMessage(" ");
            player.sendMessage("§7[§cRedLoto§7] Lancement du prochain Loto, résultat dans : "+delayInMin+" mins ! Faites vos jeux... (/loto)");
            player.sendMessage(" ");
            player.sendMessage("§8§m-------------------------------------------------");
            player.sendMessage(" ");
        }
        RedLoto.gameRunning = true;
        RedLoto.lotoScheduler = new LotoScheduler();
        RedLoto.lotoMessageScheduler = new LotoMessageScheduler();
        RedLoto.getInstance().getLotoScheduler().runTaskLaterAsynchronously(RedLoto.getInstance(), delay*20L);
        int messageDelay = RedLoto.getInstance().getLotoConfig().get().getInt("lotto.messageTimer");
        RedLoto.getInstance().getLotoMessageScheduler().runTaskTimerAsynchronously(RedLoto.getInstance(), messageDelay*20L, messageDelay*20L);
    }


}
