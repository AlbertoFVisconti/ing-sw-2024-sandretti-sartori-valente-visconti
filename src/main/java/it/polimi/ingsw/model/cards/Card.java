package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Corner;

public abstract class Card {
    private boolean isBack;
    private boolean isPlaced;

    private final Corner[] corners = null;

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

    protected Corner getCorner(int index) {
        return corners[index];
    }

    final public Corner getTopLeftCorner() {
        return this.getCorner(0);
    }
    final public Corner getTopRightCorner() {
        return this.getCorner(1);
    }
    final public Corner getBottomLeftCorner() {
        return this.getCorner(2);
    }
    final public Corner getBottomRightCorner() {
        return this.getCorner(3);
    }

    public ItemCollection collectItems() {
        return new ItemCollection()
                .add(getTopLeftCorner())
                .add(getTopRightCorner())
                .add(getBottomLeftCorner())
                .add(getBottomRightCorner());
    }
}
