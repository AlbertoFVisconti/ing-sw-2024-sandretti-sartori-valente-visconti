package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.FreeScoreScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ScoringStrategy;
import it.polimi.ingsw.utils.ItemCollection;

/**
 * Represents both Gold and Resource cards.
 * Basically, the cards that are actually playable throughout the game
 * (The StartCard is never actually "played", the player just choose the side)
 */
public class PlayCard extends Card {
    private final ItemCollection constraint;

    private final ScoringStrategy scoringStrategy;

    private final Resource type;
    protected final boolean isGold;

    /**
     * Constructs a PlayCard object with specified corners, placement constraint and scoring strategy.
     *
     * @param cardID              the card identifier
     * @param topLeft             the top-left corner on the front side.
     * @param topRight            the top-right corner on the front side.
     * @param bottomLeft          the bottom-left corner on the front side.
     * @param bottomRight         the bottom-right corner on the front side.
     * @param resourceType        the resource type of the card (also the permanent resource on the back).
     * @param isGold              flag to distinguish if a cards is a gold or resource card.
     * @param placementConstraint ItemCollection representing the items the player needs to have in order to place the card.
     * @param scoringStrategy     ScoringStrategy to evaluate the score to award the player with upon placement.
     */
    private PlayCard(String cardID, String frontpath, String backpath,
                        Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight,
                        Resource resourceType,
                        boolean isGold,
                        ItemCollection placementConstraint,
                        ScoringStrategy scoringStrategy) {
        super(cardID, frontpath, backpath, topLeft, topRight, bottomLeft, bottomRight);
        this.isGold = isGold;

        if (isGold) this.constraint = new ItemCollection(placementConstraint);
        else this.constraint = new ItemCollection();

        this.scoringStrategy = scoringStrategy;
        this.type = resourceType;
    }

    /**
     * Builds a new PlayCard representing a Gold card.
     * Since Gold and Resource card share the same class, this method (alongside with the
     * generateResourceCard method) allows to create the card and to avoid creating
     * one with invalid data.
     *
     * @param cardID              the card's identifier
     * @param topLeft             the top-left corner on the front side of the card.
     * @param topRight            the top-right corner on the front side of the card.
     * @param bottomLeft          the bottom-left corner on the front side of the card.
     * @param bottomRight         the bottom-right corner on the front side of the card.
     * @param resourceType        the Resource type of the card (also the permanent resource on the back).
     * @param placementConstraint ItemCollection representing the items the players needs to have to place the card.
     * @param scoringStrategy     ScoringStrategy to use to evaluate the score the player is awarded upon placing the card.
     * @return a new PlayCard object representing the Gold card with the specified values.
     */
    public static PlayCard generateGoldCard(String cardID, String frontpath, String backpath,
                                            Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight,
                                            Resource resourceType,
                                            ItemCollection placementConstraint,
                                            ScoringStrategy scoringStrategy) {
        return new PlayCard(cardID, frontpath, backpath, topLeft, topRight, bottomLeft, bottomRight, resourceType,
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
     * @param cardID             the card's identifier
     * @param topLeft            the top-left corner on the front side of the card.
     * @param topRight           the top-right corner on the front side of the card.
     * @param bottomLeft         the bottom-left corner on the front side of the card.
     * @param bottomRight        the bottom-right corner on the front side of the card.
     * @param resourceType       the Resource type of the card (also the permanent resource on the back).
     * @param scoreUponPlacement the score the player is awarded upon placing the card.
     * @return a new PlayCard object representing the Resource card with the specified values.
     */
    public static PlayCard generateResourceCard(String cardID, String frontpath, String backpath,
                                                Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight,
                                                Resource resourceType,
                                                int scoreUponPlacement) {
        return new PlayCard(cardID, frontpath, backpath, topLeft, topRight, bottomLeft, bottomRight, resourceType,
                false,
                null,
                new FreeScoreScoringStrategy(scoreUponPlacement));
    }

    /**
     * Retrieves the placement constraint.
     *
     * @return a new ItemCollection that represents the constraint
     */
    public ItemCollection getConstraint() {
        return new ItemCollection(constraint);
    }

    /**
     * Retrieves the scoring strategy.
     *
     * @return ScoringStrategy object that's used to evaluate this card's placement.
     */
    public ScoringStrategy getScoringStrategy() {
        return scoringStrategy;
    }

    /**
     * Retrieves the Resource representing the type of the card.
     *
     * @return Resource representing the cards' type.
     */
    public Resource getType() {
        return type;
    }

    /**
     * Retrieves the required corner on the back side of the card
     *
     * @param index the index of the desired corner
     * @return the corner in the specified position, on the back side
     */
    @Override
    protected Corner getBackCorner(int index) {
        return Corner.EMPTY;
    }

    /**
     * Retrieves the items that the player has obtained by placing the card on the back side.
     * Intended for internal use.
     *
     * @return ItemCollection containing  the items that the player has obtained by placing the card on the back side
     */
    @Override
    protected ItemCollection collectBackItems() {
        return new ItemCollection().add(this.getType().getCorner());
    }


    /**
     * Allows decks to ask for the resource on the back of the card.
     *
     * @return the resource on the back of the card (the type of the card)
     */
    @Override
    public Resource getCardResourceType() {
        return this.getType();
    }

    /**
     * Allows to check whether the card is a golden card.
     *
     * @return {@code true} if this is a golden card, {@code false} otherwise.
     */
    public boolean isGold() {
        return isGold;
    }

    @Override
    public String toString() {
        return super.toString() +
                "isGold = " + isGold + "\n" +
                "type = " + type + "\n" +
                "constraint = " + constraint.toString() + "\n" +
                "scoringStrategy = " + scoringStrategy.toString();
    }
}
