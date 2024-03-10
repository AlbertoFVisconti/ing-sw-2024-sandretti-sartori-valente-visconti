package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;

abstract class Card {
    private boolean isBack;
    private boolean isPlaced;

    private final Corner[] corners = null;

    public void flip() throws Exception {}

    public void place() {}

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
    final public Corner getNottomRightCorner() {
        return this.getCorner(3);
    }
}
