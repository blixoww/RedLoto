package fr.blixow.redloto.card;

import fr.blixow.redloto.RedLoto;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class LotoCardInventory {
    private final Inventory inventory;
    private final String moneyName;

    public LotoCardInventory(LotoCard card) {
        this.inventory = Bukkit.createInventory((InventoryHolder)null, 27, "Lotto Carte :" + card.getID());
        this.moneyName = RedLoto.getInstance().getVaultAPI().getEconomy().currencyNamePlural();
        this.setupInventory(card);
    }

    private void setupInventory(LotoCard card) {
        for(int i = 0; i < 27; ++i) {
            if (i == 11) {
                this.inventory.setItem(i, this.createItem("§c-", "§7Retirer 100 " + this.moneyName + ".", Material.WOOL, (short)14, 1));
            } else if (i == 13) {
                this.inventory.setItem(i, this.createItem("§7Valeur", "§7Valeur de " + card.getCardValue() + " " + this.moneyName + ".", Material.PAPER, 1));
            } else if (i == 15) {
                this.inventory.setItem(i, this.createItem("§a+", "§7Ajouter 100 " + this.moneyName + ".", Material.WOOL, (short)5, 1));
            } else if (i == 25) {
                this.inventory.setItem(i, this.createItem("§4Retour", "§7Revenir au menu des cartes.", Material.SIGN, 1));
            } else if (i == 26) {
                this.inventory.setItem(i, this.createItem("§4Fermer", "§7Fermer le menu lotto.", Material.BARRIER, 1));
            } else {
                this.inventory.setItem(i, this.createItem(" ", " ", Material.STAINED_GLASS_PANE, (short)7, 1));
            }
        }

    }

    public void updateInventory(LotoCard card) {
        this.inventory.setItem(13, this.createItem("§7Valeur", "§7Valeur de " + card.getCardValue() + " " + this.moneyName + ".", Material.PAPER, 1));

        for(HumanEntity human : this.inventory.getViewers()) {
            Player player = (Player)human;
            player.updateInventory();
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

    public ItemStack createItem(String name, String description, Material material, short ID, int nb) {
        ItemStack itemStack = new ItemStack(material, nb, ID);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(Collections.singletonList(description));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Inventory get() {
        return this.inventory;
    }
}
