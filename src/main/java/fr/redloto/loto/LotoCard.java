package fr.redloto.loto;

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
        if (cardValue-value >= RedLoto.getInstance().getLotoConfig().get().getInt("lotto.cardValue")) {
            cardValue -= value;
            return true;
        } else return false;
    }

    public void addValue(int value) {
        cardValue += value;
    }

    public int getCardValue() {
        return cardValue;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public LotoCardInventory getInventory() {
        return inventory;
    }

    public final int getID() {
        return ID;
    }
}
