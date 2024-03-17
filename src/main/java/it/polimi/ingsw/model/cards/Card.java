package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.FreeScoreScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ScoringStrategy;

import java.util.Arrays;

/**
 * Abstract representation of in-game cards
 * Holds methods and parameters that helps handle and generate every card in the game.
 */
public abstract class Card {
    private boolean isBack=false;
    private boolean isPlaced=false;
    private int placementTurn=-1;

    private final Corner[] corners;

    /**
     * Stores the front face's corners in an array.
     * Since Card is an abstract class, this constructor is only used by subclasses.
     *
     * @param topLeft the front top-left corner of the card
     * @param topRight the front top-right corner of the card
     * @param bottomLeft the front bottom-left corner of the card
     * @param bottomRight the front bottom-right corner of the card
     */
    protected Card(Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight) {
        this.corners = new Corner[]{topLeft, topRight, bottomLeft, bottomRight};
    }

    /**
     * Flips the card if not placed
     *
     * @throws UnsupportedOperationException if the card was placed beforehand.
     */
    public void flip()  {
        if(isPlaced) throw new UnsupportedOperationException("can't flip a placed card");
        this.isBack = !this.isBack;
    }

    /**
     * Places the cards and prevents it from being flipped.
     * The method requires the current turn number so that it can be stored.
     * <p>
     * Storing the placement turn allows to say which card is on top of the other.
     *
     * @param currentTurn the turn in which the card is placed.
     */
    public void place(int currentTurn) {
        this.isPlaced = true;
        this.placementTurn = currentTurn;
    }

    /**
     * Checks if the current card (this) was placed on a later turn than the given card's.
     * Both cards need to be placed in order to use this method.
     *
     * @param card Card that we are comparing this against
     * @return {@code true} if this was placed after the given cards, {@code false} otherwise.
     * @throws UnsupportedOperationException if one of the cards is not placed.
     */
    public boolean placedAfter(Card card) {
        if (!card.isPlaced || !this.isPlaced) throw new UnsupportedOperationException("cannot check placement order if one of the cards isn't placed");
        return this.placementTurn > card.placementTurn;
    }

    /**
     * Checks whether the card has the back side exposed.
     *
     * @return {@code true} if the card has the back side up, {@code false} otherwise.
     */
    public boolean isOnBackSide() {
        return isBack;
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
    protected Corner getCorner(int index) {
        return corners[index];
    }

    /**
     * Retrieves the top-left corner of the currently shown face of the card.
     * <p>
     * It uses the getCorner method so that subclasses only needs to redefine that method and
     * not the four get[Top/Bottom][Left/right]Corner() methods.
     *
     * @return the top-left corner, {@code null} if the corner is hidden
     */
    final public Corner getTopLeftCorner() {
        return this.getCorner(0);
    }

    /**
     * Retrieves the top-right corner of the currently shown face of the card.
     * <p>
     * It uses the getCorner method so that subclasses only needs to redefine that method and
     * not the four get[Top/Bottom][Left/right]Corner() methods.
     *
     * @return the top-right corner, {@code null} if the corner is hidden
     */
    final public Corner getTopRightCorner() {
        return this.getCorner(1);
    }

    /**
     * Retrieves the bottom-left corner of the currently shown face of the card.
     * <p>
     * It uses the getCorner method so that subclasses only needs to redefine that method and
     * not the four get[Top/Bottom][Left/right]Corner() methods.
     *
     * @return the bottom-left corner, {@code null} if the corner is hidden
     */
    final public Corner getBottomLeftCorner() {
        return this.getCorner(2);
    }

    /**
     * Retrieves the bottom-right corner of the currently shown face of the card.
     * <p>
     * It uses the getCorner method so that subclasses only needs to redefine that method and
     * not the four get[Top/Bottom][Left/right]Corner() methods.
     *
     * @return the bottom-right corner, {@code null} if the corner is hidden
     */
    final public Corner getBottomRightCorner() {
        return this.getCorner(3);
    }

    /**
     * Builds a new StartCard.
     * It basically just uses the StartCard constructor.
     * <p>
     * This method exists mainly to standardize the creation of cards.
     * Gold Card and Resource Card, being both represented by the PlayCard class,
     * need to be created through a factory method.
     *
     * @param frontTopLeft the top-left corner on the front side of the card.
     * @param frontTopRight the top-right corner on the front side of the card.
     * @param frontBottomLeft the bottom-left corner on the front side of the card.
     * @param frontBottomRight the bottom-right corner on the front side of the card.
     * @param backTopLeft the top-left corner on the back side of the card.
     * @param backTopRight the top-right corner on the back side of the card.
     * @param backBottomLeft the bottom-left corner on the back side of the card.
     * @param backBottomRight the bottom-right corner on the back side of the card.
     * @param permanentResources the ItemCollection representing the permanent resources on the back side of the card.
     * @return a new StartCard object representing the card with the specified values.
     */
    public static StartCard generateStartCard(Corner frontTopLeft, Corner frontTopRight, Corner frontBottomLeft, Corner frontBottomRight,
                                              Corner backTopLeft, Corner backTopRight, Corner backBottomLeft, Corner backBottomRight,
                                              ItemCollection permanentResources) {
        return new StartCard(frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight,
                backTopLeft, backTopRight, backBottomLeft, backBottomRight,
                permanentResources);
    }

    /**
     * Builds a new PlayCard representing a Gold card.
     * Since Gold and Resource card share the same class, this method (alongside with the
     * generateResourceCard method) allows to create the card and to avoid creating
     * one with invalid data.
     *
     * @param topLeft the top-left corner on the front side of the card.
     * @param topRight the top-right corner on the front side of the card.
     * @param bottomLeft the bottom-left corner on the front side of the card.
     * @param bottomRight the bottom-right corner on the front side of the card.
     * @param resourceType the Resource type of the card (also the permanent resource on the back).
     * @param placementConstraint ItemCollection representing the items the players needs to have to place the card.
     * @param scoringStrategy ScoringStrategy to use to evaluate the score the player is awarded upon placing the card.
     * @return a new PlayCard object representing the Gold card with the specified values.
     */
    public static PlayCard generateGoldCard(Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight,
                                            Resource resourceType,
                                            ItemCollection placementConstraint,
                                            ScoringStrategy scoringStrategy) {
        return new PlayCard(topLeft, topRight, bottomLeft, bottomRight, resourceType,
                true,
                placementConstraint,
                scoringStrategy);
    }

    /**
     * Builds a new PlayCard representing a Resource card.
     * Since Gold and Resource card share the same class, this method (alongside with the
     * generateGoldCard method) allows to create the card and to avoid creating
     * one with invalid data.
     *
     * @param topLeft the top-left corner on the front side of the card.
     * @param topRight the top-right corner on the front side of the card.
     * @param bottomLeft the bottom-left corner on the front side of the card.
     * @param bottomRight the bottom-right corner on the front side of the card.
     * @param resourceType the Resource type of the card (also the permanent resource on the back).
     * @param scoreUponPlacement the score the player is awarded upon placing the card.
     * @return a new PlayCard object representing the Resource card with the specified values.
     */
    public static PlayCard generateResourceCard(Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight,
                                                Resource resourceType,
                                                int scoreUponPlacement) {
        return new PlayCard(topLeft, topRight, bottomLeft, bottomRight, resourceType,
                false,
                null,
                new FreeScoreScoringStrategy(scoreUponPlacement));
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
    public ItemCollection collectItems() {
        return new ItemCollection()
                .add(getTopLeftCorner())
                .add(getTopRightCorner())
                .add(getBottomLeftCorner())
                .add(getBottomRightCorner());
    }

    @Override
    public String toString() {
        return "isBack = " + isBack + "\n" +
                "isPlaced = " + isPlaced + "\n" +
                "placementTurn = " + placementTurn + "\n" +
                "frontCorners = " + Arrays.toString(corners) + "\n";
    }
}
