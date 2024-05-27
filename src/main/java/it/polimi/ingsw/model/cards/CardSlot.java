package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.utils.ItemCollection;

import java.io.Serializable;

public record CardSlot(Card card, boolean onBackSide, int placementTurn) implements Serializable {
    public Corner getTopLeftCorner() {
        return card.getTopLeftCorner(onBackSide);
    }

    public Corner getTopRightCorner() {
        return card.getTopRightCorner(onBackSide);
    }

    public Corner getBottomLeftCorner() {
        return card.getBottomLeftCorner(onBackSide);
    }


    public Corner getBottomRightCorner() {
        return card.getBottomRightCorner(onBackSide);
    }

    public ItemCollection collectItems() {
        return card.collectItems(onBackSide);
    }
    public Card getCard() {
        return card;
    }
}
