package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.decks.Drawable;
import it.polimi.ingsw.utils.ItemCollection;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Abstract representation of in-game cards
 * Holds methods and parameters that helps handle and generate every card in the game.
 * Cards are immutable objects, allowing to create a single object for each card in the game
 * and reuse the same set of object for all the games.
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

    /**
     * Retrieves the card's ID
     *
     * @return the card's ID
     */
    public String getCardID() {
        return cardID;
    }

    /**
     * Retrieves the corner at the specified index in the front face.
     * This method is intended for internal usage in order to avoid code repetition.
     *
     * @param index the index of the desired corner
     * @return the requested corner on the front face, {@code null} if the corner is hidden
     */
    private Corner getFrontCorner(int index) {
        return corners[index];
    }

    /**
     * Retrieves the corner at the specified index in the back face.
     * This method is intended for internal usage in order to avoid code repetition.
     *
     * @param index the index of the desired corner
     * @return the requested corner on the front face, {@code null} if the corner is hidden
     */
    protected abstract Corner getBackCorner(int index);

    /**
     * Retrieves the corner at the specified index in the specified face.
     * This method is intended for internal usage in order to avoid code repetition.
     *
     * @param index the index of the desired corner
     * @param onBackSide {@code true} if the requested corner is on the back side, {@code false} otherwise
     * @return the requested corner on the selected face, {@code null} if the corner is hidden
     */
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
     * Retrieves all the items the player has obtained by placing this card
     * on the specified side.
     *
     * @return ItemCollection containing the items that the cards provide
     */
    public ItemCollection collectItems(boolean onBackSide) {
        if (onBackSide) return collectBackItems();
        else return collectFrontItems();
    }

    /**
     * Retrieves the items that the player has obtained by placing the card on the back side.
     * Intended for internal use.
     *
     * @return ItemCollection containing  the items that the player has obtained by placing the card on the back side
     */
    protected abstract ItemCollection collectBackItems();

    /**
     * Retrieves the items that the player has obtained by placing the card on the front side
     * Intended for internal use.
     *
     * @return ItemCollection containing  the items that the player has obtained by placing the card on the front side
     */
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

    /**
     * Retrieves the path to the image that represents the front side of the card
     * @return the path to the image that represents the front side of the card
     */
    public String getFrontpath() {
        return frontpath;
    }

    /**
     * Retrieves the path to the image that represents the back side of the card
     * @return the path to the image that represents the back side of the card
     */
    public String getBackpath() {
        return backpath;
    }

}
