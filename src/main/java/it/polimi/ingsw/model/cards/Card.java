package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Corner;

public abstract class Card {
    private boolean isBack;
    private boolean isPlaced;

    private int placementTurn;

    private final Corner[] corners = null;

    protected Card(/*TODO*/) {
        // TODO
    }

    public void flip()  {
        if(isPlaced) throw new UnsupportedOperationException("can't flip a placed card");
        this.isBack = !this.isBack;
    }

    public void place(int currentTurn) {
        this.isPlaced = true;
        this.placementTurn = currentTurn;
    }

    public boolean placedAfter(Card card) throws Exception {
        if (!card.isPlaced || !this.isPlaced) throw new Exception("cannot check placement order if one of the cards isn't placed");
        return this.placementTurn > card.placementTurn;
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

    public static StartCard generateStartCard(/*TODO*/) {
        return new StartCard(/*TODO*/);
    }

    public static PlayCard generateGoldCard(/*TODO*/) {
        return new PlayCard(/*TODO*/);
    }

    public static PlayCard generateResourceCard(/*TODO*/) {
        return new PlayCard(/*TODO*/);
    }

    public ItemCollection collectItems() {
        return new ItemCollection()
                .add(getTopLeftCorner())
                .add(getTopRightCorner())
                .add(getBottomLeftCorner())
                .add(getBottomRightCorner());
    }
}
