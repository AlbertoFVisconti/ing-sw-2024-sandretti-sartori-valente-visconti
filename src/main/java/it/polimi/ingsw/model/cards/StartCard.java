package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.utils.ItemCollection;

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
     * @param cardID             the card's identifier
     * @param frontTopLeft       the top-left corner on the front side of the card.
     * @param frontTopRight      the top-right corner on the front side of the card.
     * @param frontBottomLeft    the bottom-left corner on the front side of the card.
     * @param frontBottomRight   the bottom-right corner on the front side of the card.
     * @param backTopLeft        the top-left corner on the back side of the card.
     * @param backTopRight       the top-right corner on the back side of the card.
     * @param backBottomLeft     the bottom-left corner on the back side of the card.
     * @param backBottomRight    the bottom-right corner on the back side of the card.
     * @param permanentResources ItemCollection representing the set of permanent resources on the back of the card.
     */
    public StartCard(String cardID, String frontpath, String backpath,
                        Corner frontTopLeft, Corner frontTopRight, Corner frontBottomLeft, Corner frontBottomRight,
                        Corner backTopLeft, Corner backTopRight, Corner backBottomLeft, Corner backBottomRight,
                        ItemCollection permanentResources) {
        super(cardID, frontpath, backpath, frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight);
        this.backCorners = new Corner[]{backTopLeft, backTopRight, backBottomLeft, backBottomRight};
        this.permanentResources = new ItemCollection(permanentResources);
    }

    /**
     * Retrieves the startcard's permanent resources
     *
     * @return ItemCollection containing the card's permanent resources
     */
    public ItemCollection getPermanentResources() {
        return new ItemCollection(this.permanentResources);
    }

    /**
     * @return {@code null}
     */
    @Override
    public Resource getCardResourceType() {
        return null;
    }

    /**
     * Retrieves the required corner on the back side of the card
     *
     * @param index the index of the desired corner
     * @return the corner in the specified position, on the back side
     */
    @Override
    protected Corner getBackCorner(int index) {
        return this.backCorners[index];
    }

    /**
     * Retrieves the items that the player has obtained by placing the card on the back side.
     * Intended for internal use.
     *
     * @return ItemCollection containing  the items that the player has obtained by placing the card on the back side
     */
    @Override
    protected ItemCollection collectBackItems() {
        return new ItemCollection()
                .add(this.permanentResources)
                .add(this.backCorners[0])
                .add(this.backCorners[1])
                .add(this.backCorners[2])
                .add(this.backCorners[3]);
    }

    @Override
    public String toString() {
        return super.toString() +
                "backCorners = " + Arrays.toString(backCorners) + "\n" +
                "permanentResources = " + permanentResources.toString();
    }
}
