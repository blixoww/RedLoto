package fr.redloto.loto;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LotoInventory {

    private final Inventory inventory;
    private final String moneyName;

    public LotoInventory(LotoPlayer player) {
        this.inventory = Bukkit.createInventory(null, 27, "Loto");
        this.moneyName = RedLoto.getInstance().getVaultAPI().getEconomy().currencyNamePlural();
        this.setupInventory(player);
    }

    private void setupInventory(LotoPlayer player) {
        this.updateInventory(player);
        for (int i = 18; i < 25; i++)
            this.inventory.setItem(i, createItem(" ", " ", Material.STAINED_GLASS_PANE, (short) 7, 1));
        int cardValue = RedLoto.getInstance().getLotoConfig().get().getInt("loto.cardValue");
        List<String> description = Arrays.asList("§7Clique ici pour acheter une nouvelle carte !", "§7Valeur de §c" + cardValue + " " + moneyName + ".");
        this.inventory.setItem(25, createItem("§2Acheter une carte", description, Material.GOLD_NUGGET, 1));
        this.inventory.setItem(26, createItem("§4Fermer", "§7Fermer le menu lotto.", Material.BARRIER, 1));
    }

    public void updateInventory(LotoPlayer player) {
        for (int i = 0; i < 18; i++) {
            if (player.getCards().size() > i) {
                LotoCard card = player.getCards().get(i);
                List<String> description = Arrays.asList("§7Valeur de §c" + card.getCardValue() + " " + moneyName + ".", String.valueOf(card.getID()));
                this.inventory.setItem(i, createItem("§2Carte de Loto", description, Material.PAPER, 1));
            } else
                i = 20;
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
