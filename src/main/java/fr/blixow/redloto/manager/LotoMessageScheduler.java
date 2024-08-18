package fr.blixow.redloto.manager;


import fr.blixow.redloto.RedLoto;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LotoMessageScheduler extends BukkitRunnable {

    public void run() {
        if (RedLoto.gameRunning) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(" ");
                player.sendMessage("§8§m-------------------------------------------------");
                player.sendMessage(" ");
                player.sendMessage("§7[§2RedLoto§7] Un loto est en cours ! Faites vos jeux... (/loto)");
                player.sendMessage(" ");
                player.sendMessage("§8§m-------------------------------------------------");
            }
        }
    }
}
