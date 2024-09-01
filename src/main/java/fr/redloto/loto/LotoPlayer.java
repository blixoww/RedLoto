package fr.redloto.loto;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class LotoPlayer {

    private final ArrayList<LotoCard> cards;
    private final LotoInventory inventory;
    private final String ownerName;

    public LotoPlayer(String ownerName) {
        this.ownerName = ownerName;
        this.cards = new ArrayList<>();
        this.cards.add(new LotoCard(ownerName, RedLoto.getInstance().newCardID()));
        this.inventory = new LotoInventory(this);
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void openLotoInventory(Player player) {
        inventory.updateInventory(this);
        player.openInventory(inventory.getInventory());
    }

    public void updateLottoInventory() {
        inventory.updateInventory(this);
    }

    public ArrayList<LotoCard> getCards() {
        return cards;
    }

    public LotoCard getCardByID(int ID) {
        for (LotoCard card : this.cards) {
            if (card.getID() == ID)
                return card;
        }
        return null;
    }

    public LotoCard getCardByInventoryName(String inventoryName) {
        for (LotoCard card : this.cards) {
            if (card.getInventory().get().getName().equals(inventoryName))
                return card;
        }
        return null;
    }

}
