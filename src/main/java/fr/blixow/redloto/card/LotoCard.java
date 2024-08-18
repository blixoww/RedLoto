package fr.blixow.redloto.card;

import fr.blixow.redloto.RedLoto;

public class LotoCard {

    private final String ownerName;
    private int cardValue;
    private final int ID;
    private final LotoCardInventory inventory;

    public LotoCard(String ownerName, int cardID) {
        this.ownerName = ownerName;
        this.ID = cardID;
        RedLoto.getInstance().addInGameCard(this);
        this.cardValue = RedLoto.getInstance().getLotoConfig().get().getInt("lotto.cardValue");
        this.inventory = new LotoCardInventory(this);
    }

    public boolean removeValue(int value) {
        if (this.cardValue - value >= RedLoto.getInstance().getLotoConfig().get().getInt("lotto.cardValue")) {
            this.cardValue -= value;
            return true;
        } else {
            return false;
        }
    }

    public void addValue(int value) {
        this.cardValue += value;
    }

    public int getCardValue() {
        return this.cardValue;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public LotoCardInventory getInventory() {
        return this.inventory;
    }

    public final int getID() {
        return this.ID;
    }
}
