package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Item;

public abstract class Card {
    private boolean isBack;
    private boolean isPlaced;

    private final Item[] corners = null;

    public void flip() throws Exception {
        if(isPlaced) throw new Exception("can't flip a placed card");
        this.isBack = !this.isBack;
    }

    public void place() {
        this.isPlaced = true;
    }

    public boolean isOnBackSide() {
        return isBack;
    }

    protected Item getCorner(int index) {
        return corners[index];
    }

    final public Item getTopLeftCorner() {
        return this.getCorner(0);
    }
    final public Item getTopRightCorner() {
        return this.getCorner(1);
    }
    final public Item getBottomLeftCorner() {
        return this.getCorner(2);
    }
    final public Item getBottomRightCorner() {
        return this.getCorner(3);
    }

    public static StartCard generateStartCard() {
        return new StartCard();
    }

    public static PlayCard generateGoldCard() {
        return new PlayCard();
    }

    public static PlayCard generateResourceCard() {
        return new PlayCard();
    }

    public ItemCollection collectItems() {
        return new ItemCollection()
                .add(getTopLeftCorner())
                .add(getTopRightCorner())
                .add(getBottomLeftCorner())
                .add(getBottomRightCorner());
    }
}
