package fr.redloto.loto;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class LotoCommand extends Command implements PluginIdentifiableCommand {

    public LotoCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        if (RedLoto.gameRunning) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                if (player.isOp())
                    return this.onCommand(commandSender, label, args);
                if (player.hasPermission("loto"))
                    return this.onCommand(commandSender, label, args);
                commandSender.sendMessage("Vous n'avez pas la permission d'utiliser cette commande !");
            } else {
                commandSender.sendMessage("This command can be execute only by player !");
            }
        } else {
            commandSender.sendMessage("§7[§2"+ RedLoto.getLotoLogger().getName()+"§7] §cLe loto n'est pas lancé !");
        }
        return false;
    }

    public boolean onCommand(CommandSender commandSender, String label, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (!RedLoto.getInstance().isPlayerInPlaying(player))
                RedLoto.getInstance().getLotoPlayersArrays().add(new LotoPlayer(player.getName()));
            RedLoto.getInstance().getLotoPlayer(player.getName()).openLotoInventory(player);
            return true;
        }
        return false;
    }

    @Override
    public Plugin getPlugin() {
        return RedLoto.getInstance();
    }
}
