package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.utils.ItemCollection;

import java.io.Serializable;

/**
 * Record that represent a Card along with its orientation and placement turn number (if the card was placed)
 * It also "remap" certain Card methods in order to take advantage of the information
 * in the record.
 *
 * @param card a reference to the Card object contained by the slot
 * @param onBackSide {@code true} if the card is on the back side, {@code false} otherwise
 * @param placementTurn a number representing the placement turn during which the card was placed (if it was ever placed)
 */
public record CardSlot(Card card, boolean onBackSide, int placementTurn) implements Serializable {
    /**
     * Retrieves the top left Corner of the card in its current orientation
     *
     * @return Corner object representing the top left Corner of the card in its current orientation
     */
    public Corner getTopLeftCorner() {
        return card.getTopLeftCorner(onBackSide);
    }

    /**
     * Retrieves the top right Corner of the card in its current orientation
     *
     * @return Corner object representing the top right Corner of the card in its current orientation
     */
    public Corner getTopRightCorner() {
        return card.getTopRightCorner(onBackSide);
    }

    /**
     * Retrieves the bottom left Corner of the card in its current orientation
     *
     * @return Corner object representing the bottom left Corner of the card in its current orientation
     */
    public Corner getBottomLeftCorner() {
        return card.getBottomLeftCorner(onBackSide);
    }

    /**
     * Retrieves the bottom right Corner of the card in its current orientation
     *
     * @return Corner object representing the bottom right Corner of the card in its current orientation
     */
    public Corner getBottomRightCorner() {
        return card.getBottomRightCorner(onBackSide);
    }

    /**
     * Retrieves the Items that the players has obtained by placing the card (in its current orientation)
     *
     * @return ItemCollection containing the Items that the players has obtained by placing the card (in its current orientation)
     */
    public ItemCollection collectItems() {
        return card.collectItems(onBackSide);
    }
}
