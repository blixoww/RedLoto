package fr.blixow.redloto.manager;

import fr.blixow.redloto.RedLoto;
import fr.blixow.redloto.card.LotoCard;
import fr.blixow.redloto.event.LotoInventory;
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
        return this.ownerName;
    }

    public void openLotoInventory(Player player) {
        this.inventory.updateInventory(this);
        player.openInventory(this.inventory.getInventory());
    }

    public void updateLottoInventory() {
        this.inventory.updateInventory(this);
    }

    public ArrayList<LotoCard> getCards() {
        return this.cards;
    }

    public LotoCard getCardByID(int ID) {
        for (LotoCard card : this.cards) {
            if (card.getID() == ID) {
                return card;
            }
        }

        return null;
    }

    public LotoCard getCardByInventoryName(String inventoryName) {
        for (LotoCard card : this.cards) {
            if (card.getInventory().get().getName().equals(inventoryName)) {
                return card;
            }
        }
        return null;
    }
}
