package fr.redloto.loto;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LotoMessageScheduler extends BukkitRunnable {

    @Override
    public void run() {
        if (RedLoto.gameRunning) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(" ");
                player.sendMessage("§8§m-------------------------------------------------");
                player.sendMessage(" ");
                player.sendMessage("§7[§cRedLoto§7] Un lotto est en cours ! Faites vos jeux... (/loto)");
                player.sendMessage(" ");
                player.sendMessage("§8§m-------------------------------------------------");
            }
        }
    }

}
