package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.utils.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Corner;

import java.util.Arrays;

/**
 * Represents Starting cards.
 * This kind of cards is only used in the very beginning of the game.
 * It's the first card on the Players board and has some peculiarities
 * compared to PlayCards.
 */
public class StartCard extends Card {
    private final Corner[] backCorners;

    private final ItemCollection permanentResources;

    /**
     * Constructs a StartCard object with specified parameters.
     * StartCards also has different corners on the back side, that's why
     * the constructor needs two sets of corners.
     *
     * @param frontTopLeft the top-left corner on the front side of the card.
     * @param frontTopRight the top-right corner on the front side of the card.
     * @param frontBottomLeft the bottom-left corner on the front side of the card.
     * @param frontBottomRight the bottom-right corner on the front side of the card.
     * @param backTopLeft the top-left corner on the back side of the card.
     * @param backTopRight the top-right corner on the back side of the card.
     * @param backBottomLeft the bottom-left corner on the back side of the card.
     * @param backBottomRight the bottom-right corner on the back side of the card.
     * @param permanentResources ItemCollection representing the set of permanent resources on the back of the card.
     */
    public StartCard(Corner frontTopLeft, Corner frontTopRight, Corner frontBottomLeft, Corner frontBottomRight,
                        Corner backTopLeft, Corner backTopRight, Corner backBottomLeft, Corner backBottomRight,
                        ItemCollection permanentResources) {
        super(frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight);
        this.backCorners = new Corner[]{backTopLeft, backTopRight, backBottomLeft, backBottomRight};
        this.permanentResources = new ItemCollection(permanentResources);
    }

    /**
     * Retrieves the corner at the specified index in the shown side of the card.
     * This method is intended for internal usage in order to avoid code repetition.
     * <p>
     * Because of how StartCards work, if the back side's up, then the
     * results can be found in the backCorners array, in this class.
     * Otherwise, Card.getCorner method will be invoked to return the corner.
     *
     * @param index the index of the desired corner.
     * @return the corner at the specified index in the shown side of the card.
     */
    @Override
    protected Corner getCorner(int index) {
        if (this.isOnBackSide()) {
            return backCorners[index];
        }
        return super.getCorner(index);

    }

    /**
     * Retrieves all the items the player has obtained by "placing" this card on the current side.
     * <p>
     * If the card's the front side up, the Card.collectItems will be called.
     * Otherwise, the collected items are just represented by the permanent resources on the back.
     * <p>
     * Note: the StartGame cards have corners in the back, but they are always empty or hidden, that
     * why they're not included in the collection of items.
     *
     * @return ItemCollection containing the items in the shown side's corner.
     */
    @Override
    public ItemCollection collectItems() {
        if(isOnBackSide()) {
            return new ItemCollection(permanentResources);
        }
        return super.collectItems();
    }

    @Override
    public String toString() {
        return super.toString() +
                "frontCorners = " + Arrays.toString(backCorners) + "\n" +
                "permanentResources = " + permanentResources.toString();
    }
}
