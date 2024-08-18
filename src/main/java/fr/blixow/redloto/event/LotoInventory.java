package fr.blixow.redloto.event;

import fr.blixow.redloto.RedLoto;
import fr.blixow.redloto.card.LotoCard;
import fr.blixow.redloto.manager.LotoPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LotoInventory {

    private final Inventory inventory = Bukkit.createInventory((InventoryHolder) null, 27, "Loto");
    private final String moneyName = RedLoto.getInstance().getVaultAPI().getEconomy().currencyNamePlural();

    public LotoInventory(LotoPlayer player) {
        this.setupInventory(player);
    }

    private void setupInventory(LotoPlayer player) {
        this.updateInventory(player);

        for (int i = 18; i < 25; ++i) {
            this.inventory.setItem(i, this.createItem(" ", " ", Material.STAINED_GLASS_PANE, (short) 7, 1));
        }

        int cardValue = RedLoto.getInstance().getLotoConfig().get().getInt("loto.cardValue");
        List<String> description = Arrays.asList("§7Clique ici pour acheter une nouvelle carte !", "§7Valeur de §c" + cardValue + " " + this.moneyName + ".");
        this.inventory.setItem(25, this.createItem("§2Acheter une carte", description, Material.GOLD_NUGGET, 1));
        this.inventory.setItem(26, this.createItem("§4Fermer", "§7Fermer le menu lotto.", Material.BARRIER, 1));
    }

    public void updateInventory(LotoPlayer player) {
        for (int i = 0; i < 18; ++i) {
            if (player.getCards().size() > i) {
                LotoCard card = (LotoCard) player.getCards().get(i);
                List<String> description = Arrays.asList("§7Valeur de §c" + card.getCardValue() + " " + this.moneyName + ".", String.valueOf(card.getID()));
                this.inventory.setItem(i, this.createItem("§2Carte de Lotto", description, Material.PAPER, 1));
            } else {
                i = 20;
            }
        }

    }

    public ItemStack createItem(String name, String description, Material material, int nb) {
        ItemStack itemStack = new ItemStack(material, nb);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Collections.singletonList(description));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createItem(String name, List<String> description, Material material, int nb) {
        ItemStack itemStack = new ItemStack(material, nb);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(description);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack createItem(String name, String description, Material material, short ID, int nb) {
        ItemStack itemStack = new ItemStack(material, nb, ID);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Collections.singletonList(description));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Inventory getInventory() {
        return this.inventory;
    }
}
