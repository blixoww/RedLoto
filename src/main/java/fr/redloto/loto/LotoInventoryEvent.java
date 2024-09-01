package fr.redloto.loto;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class LotoInventoryEvent implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null) {
            if (event.getClickedInventory().getName().equals("Loto")) {
                if (event.getCurrentItem() != null) {
                    event.setCancelled(true);
                    ItemStack itemStack = event.getCurrentItem();
                    if (itemStack.getType() == Material.BARRIER)
                        event.getWhoClicked().closeInventory();
                    if (itemStack.getType() == Material.PAPER) {
                        LotoPlayer player = RedLoto.getInstance().getLotoPlayer(event.getWhoClicked().getName());
                        int ID = Integer.parseInt(itemStack.getItemMeta().getLore().get(1));
                        LotoCard card = player.getCardByID(ID);
                        event.getWhoClicked().openInventory(card.getInventory().get());
                    }
                    if (itemStack.getType() == Material.GOLD_NUGGET) {
                        LotoPlayer player = RedLoto.getInstance().getLotoPlayer(event.getWhoClicked().getName());
                        if (player.getCards().size() < RedLoto.getInstance().getLotoConfig().get().getInt("loto.maxCard")) {
                            OfflinePlayer offlinePlayer = Bukkit.getPlayer(player.getOwnerName());
                            int cardValue = RedLoto.getInstance().getLotoConfig().get().getInt("loto.cardValue");
                            if (RedLoto.getInstance().getVaultAPI().getEconomy().getBalance(offlinePlayer) >= cardValue) {
                                RedLoto.getInstance().getVaultAPI().getEconomy().withdrawPlayer(offlinePlayer, cardValue);
                                LotoCard card = new LotoCard(player.getOwnerName(), RedLoto.getInstance().newCardID());
                                player.getCards().add(card);
                                player.updateLottoInventory();
                            } else
                                event.getWhoClicked().sendMessage("§2[" + RedLoto.getLotoLogger().getName() + "] §cVous n'avez pas assez d'argent pour ça !");
                        } else
                            event.getWhoClicked().sendMessage("§2[" + RedLoto.getLotoLogger().getName() + "] §cVous avez acheté le nombre maximum de carte.");
                    }
                }
            } else if (event.getClickedInventory().getName().contains("Loto Carte :")) {
                if (event.getCurrentItem() != null) {
                    event.setCancelled(true);
                    ItemStack itemStack = event.getCurrentItem();
                    if (itemStack.getType() == Material.BARRIER)
                        event.getWhoClicked().closeInventory();
                    if (itemStack.getType() == Material.SIGN) {
                        LotoPlayer player = RedLoto.getInstance().getLotoPlayer(event.getWhoClicked().getName());
                        player.openLotoInventory((Player) event.getWhoClicked());
                    }
                    if (itemStack.getType() == Material.WOOL && itemStack.getDurability() == 14) {
                        LotoPlayer player = RedLoto.getInstance().getLotoPlayer(event.getWhoClicked().getName());
                        LotoCard card = player.getCardByInventoryName(event.getClickedInventory().getTitle());
                        int cardValue = RedLoto.getInstance().getLotoConfig().get().getInt("loto.cardValue");
                        OfflinePlayer offlinePlayer = Bukkit.getPlayer(player.getOwnerName());
                        if (card.getCardValue() - cardValue >= cardValue) {
                            RedLoto.getInstance().getVaultAPI().getEconomy().depositPlayer(offlinePlayer, cardValue);
                            card.removeValue(cardValue);
                            card.getInventory().updateInventory(card);
                        } else
                            event.getWhoClicked().sendMessage("§2[" + RedLoto.getLotoLogger().getName() + "] §cLa carte a atteint la valeur minimum !");
                    }
                    if (itemStack.getType() == Material.WOOL && itemStack.getDurability() == 5) {
                        LotoPlayer player = RedLoto.getInstance().getLotoPlayer(event.getWhoClicked().getName());
                        LotoCard card = player.getCardByInventoryName(event.getClickedInventory().getTitle());
                        int cardValue = RedLoto.getInstance().getLotoConfig().get().getInt("loto.cardValue");
                        OfflinePlayer offlinePlayer = Bukkit.getPlayer(player.getOwnerName());
                        if (RedLoto.getInstance().getVaultAPI().getEconomy().getBalance(offlinePlayer) >= cardValue) {
                            RedLoto.getInstance().getVaultAPI().getEconomy().withdrawPlayer(offlinePlayer, cardValue);
                            card.addValue(cardValue);
                            card.getInventory().updateInventory(card);
                        } else
                            event.getWhoClicked().sendMessage("§2[" + RedLoto.getLotoLogger().getName() + "] §cVous n'avez pas assez d'argent pour ça !");
                    }
                }
            }
        }
    }

}
