package fr.blixow.redloto.event;

import fr.blixow.redloto.RedLoto;
import fr.blixow.redloto.card.LotoCard;
import fr.blixow.redloto.manager.LotoPlayer;
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
            if (event.getClickedInventory().getName().equals("Lotto")) {
                if (event.getCurrentItem() != null) {
                    event.setCancelled(true);
                    ItemStack itemStack = event.getCurrentItem();
                    if (itemStack.getType() == Material.BARRIER) {
                        event.getWhoClicked().closeInventory();
                    }

                    if (itemStack.getType() == Material.PAPER) {
                        LotoPlayer player = RedLoto.getInstance().getLottoPlayer(event.getWhoClicked().getName());
                        int ID = Integer.parseInt((String) itemStack.getItemMeta().getLore().get(1));
                        LotoCard card = player.getCardByID(ID);
                        event.getWhoClicked().openInventory(card.getInventory().get());
                    }

                    if (itemStack.getType() == Material.GOLD_NUGGET) {
                        LotoPlayer player = RedLoto.getInstance().getLottoPlayer(event.getWhoClicked().getName());
                        if (player.getCards().size() < RedLoto.getInstance().getLotoConfig().get().getInt("lotto.maxCard")) {
                            OfflinePlayer offlinePlayer = Bukkit.getPlayer(player.getOwnerName());
                            int cardValue = RedLoto.getInstance().getLotoConfig().get().getInt("lotto.cardValue");
                            if (RedLoto.getInstance().getVaultAPI().getEconomy().getBalance(offlinePlayer) >= (double) cardValue) {
                                RedLoto.getInstance().getVaultAPI().getEconomy().withdrawPlayer(offlinePlayer, (double) cardValue);
                                LotoCard card = new LotoCard(player.getOwnerName(), RedLoto.getInstance().newCardID());
                                player.getCards().add(card);
                                player.updateLottoInventory();
                            } else {
                                event.getWhoClicked().sendMessage("§2[" + RedLoto.getLotoLogger().getName() + "] §cVous n'avez pas assez d'argent pour ça !");
                            }
                        } else {
                            event.getWhoClicked().sendMessage("§2[" + RedLoto.getLotoLogger().getName() + "] §cVous avez acheté le nombre maximum de carte.");
                        }
                    }
                }
            } else if (event.getClickedInventory().getName().contains("Lotto Carte :") && event.getCurrentItem() != null) {
                event.setCancelled(true);
                ItemStack itemStack = event.getCurrentItem();
                if (itemStack.getType() == Material.BARRIER) {
                    event.getWhoClicked().closeInventory();
                }

                if (itemStack.getType() == Material.SIGN) {
                    LotoPlayer player = RedLoto.getInstance().getLottoPlayer(event.getWhoClicked().getName());
                    assert player != null;
                    player.openLotoInventory((Player) event.getWhoClicked());
                }
                LotoPlayer player = RedLoto.getInstance().getLottoPlayer(event.getWhoClicked().getName());
                if (itemStack.getType() == Material.WOOL && itemStack.getDurability() == 14) {
                    LotoCard card = player.getCardByInventoryName(event.getClickedInventory().getTitle());
                    int cardValue = RedLoto.getInstance().getLotoConfig().get().getInt("lotto.cardValue");
                    OfflinePlayer offlinePlayer = Bukkit.getPlayer(player.getOwnerName());
                    if (card.getCardValue() - cardValue >= cardValue) {
                        RedLoto.getInstance().getVaultAPI().getEconomy().depositPlayer(offlinePlayer, (double) cardValue);
                        card.removeValue(cardValue);
                        card.getInventory().updateInventory(card);
                    } else {
                        event.getWhoClicked().sendMessage("§2[" + RedLoto.getLotoLogger().getName() + "] §cLa carte a atteint la valeur minimum !");
                    }
                }

                if (itemStack.getType() == Material.WOOL && itemStack.getDurability() == 5) {
                    LotoCard card = player.getCardByInventoryName(event.getClickedInventory().getTitle());
                    int cardValue = RedLoto.getInstance().getLotoConfig().get().getInt("lotto.cardValue");
                    OfflinePlayer offlinePlayer = Bukkit.getPlayer(player.getOwnerName());
                    if (RedLoto.getInstance().getVaultAPI().getEconomy().getBalance(offlinePlayer) >= (double) cardValue) {
                        RedLoto.getInstance().getVaultAPI().getEconomy().withdrawPlayer(offlinePlayer, (double) cardValue);
                        card.addValue(cardValue);
                        card.getInventory().updateInventory(card);
                    } else {
                        event.getWhoClicked().sendMessage("§2[" + RedLoto.getLotoLogger().getName() + "] §cVous n'avez pas assez d'argent pour ça !");
                    }
                }
            }
        }

    }
}
