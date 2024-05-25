package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.decks.Drawable;
import it.polimi.ingsw.utils.ItemCollection;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Abstract representation of in-game cards
 * Holds methods and parameters that helps handle and generate every card in the game.
 */
public abstract class Card implements Drawable, Serializable {
    private final String cardID;

    private final Corner[] corners;
    private final String frontpath;
    private final String backpath;

    /**
     * Stores the front face's corners in an array.
     * Since Card is an abstract class, this constructor is only used by subclasses.
     *
     * @param cardID      the unique ID that identifies the card
     * @param topLeft     the front top-left corner of the card
     * @param topRight    the front top-right corner of the card
     * @param bottomLeft  the front bottom-left corner of the card
     * @param bottomRight the front bottom-right corner of the card
     */
    protected Card(String cardID, String frontpath, String backpath, Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight) {
        this.cardID = cardID;
        this.corners = new Corner[]{topLeft, topRight, bottomLeft, bottomRight};
        this.frontpath=frontpath;
        this.backpath=backpath;
    }

    public String getCardID() {
        return cardID;
    }

    /**
     * Retrieves the corner at the specified index in the front face.
     * This method is intended for internal usage in order to avoid code repetition.
     * <p>
     * Subclasses override this method in order to provide the correct Corner depending
     * on the side of the card.
     * This method definition always retrieves the corner on the front side.
     * That's mainly because the corners array (representing the front corners) is private to this
     * class, so subclasses cannot access it.
     * In order to provide the corner, if the card has the front side up, subclasses needs to
     * cass this version of the method (through super.getCorner(index)).
     *
     * @param index the index of the desired corner
     * @return the requested corner on the front face, {@code null} if the corner is hidden
     */
    private Corner getFrontCorner(int index) {
        return corners[index];
    }

    protected abstract Corner getBackCorner(int index);

    private Corner getCorner(int index, boolean onBackSide) {
        if (onBackSide) return this.getBackCorner(index);
        else return this.getFrontCorner(index);
    }

    /**
     * Retrieves the top-left corner of the currently shown face of the card.
     * <p>
     * It uses the getCorner method so that subclasses only needs to redefine that method and
     * not the four get[Top/Bottom][Left/right]Corner() methods.
     *
     * @return the top-left corner, {@code null} if the corner is hidden
     */
    final public Corner getTopLeftCorner(boolean onBackSide) {
        return this.getCorner(0, onBackSide);
    }

    /**
     * Retrieves the top-right corner of the currently shown face of the card.
     * <p>
     * It uses the getCorner method so that subclasses only needs to redefine that method and
     * not the four get[Top/Bottom][Left/right]Corner() methods.
     *
     * @return the top-right corner, {@code null} if the corner is hidden
     */
    final public Corner getTopRightCorner(boolean onBackSide) {
        return this.getCorner(1, onBackSide);
    }

    /**
     * Retrieves the bottom-left corner of the currently shown face of the card.
     * <p>
     * It uses the getCorner method so that subclasses only needs to redefine that method and
     * not the four get[Top/Bottom][Left/right]Corner() methods.
     *
     * @return the bottom-left corner, {@code null} if the corner is hidden
     */
    final public Corner getBottomLeftCorner(boolean onBackSide) {
        return this.getCorner(2, onBackSide);
    }

    /**
     * Retrieves the bottom-right corner of the currently shown face of the card.
     * <p>
     * It uses the getCorner method so that subclasses only needs to redefine that method and
     * not the four get[Top/Bottom][Left/right]Corner() methods.
     *
     * @return the bottom-right corner, {@code null} if the corner is hidden
     */
    final public Corner getBottomRightCorner(boolean onBackSide) {
        return this.getCorner(3, onBackSide);
    }

    /**
     * Retrieves all the items the player has obtained by placing this card.
     * This implementation always return the content of the front side's corners.
     * That's because the front side's corners are stored in the corner array which
     * is private to this class, because The front is the same (in terms of resources to collect)
     * for all the cards (start, gold and resource cards).
     * Thus, subclasses needs to call this method if the card has the front side up, the
     * redefinition handles the collection of items on the back side.
     *
     * @return ItemCollection containing the items in the card's front side's corner.
     */
    public ItemCollection collectItems(boolean onBackSide) {
        if (onBackSide) return collectBackItems();
        else return collectFrontItems();
    }

    protected abstract ItemCollection collectBackItems();

    private ItemCollection collectFrontItems() {
        return new ItemCollection()
                .add(getTopLeftCorner(false))
                .add(getTopRightCorner(false))
                .add(getBottomLeftCorner(false))
                .add(getBottomRightCorner(false));
    }

    @Override
    public String toString() {
        return "frontCorners = " + Arrays.toString(corners) + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card objCard) {
            return objCard.cardID.equals(this.cardID);
        } else return false;
    }

    public String getFrontpath() {
        return frontpath;
    }
    public String getBackpath() {
        return backpath;
    }

}
