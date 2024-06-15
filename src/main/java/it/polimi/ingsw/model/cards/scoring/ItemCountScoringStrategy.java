package it.polimi.ingsw.model.cards.scoring;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CardLocation;

/**
 * A scoring strategy that awards score based on the number of a specific item present in the player's inventory.
 */
public class ItemCountScoringStrategy implements ScoringStrategy {

    private final Corner itemToCount;
    private final int scorePerItem;

    /**
     * Constructs a new ItemCountScoringStrategy with the specified item to count and score per item.
     *
     * @param itemType     the Corner that contains the specific item to count in the player's inventory.
     * @param scorePerItem the score awarded per item.
     */
    public ItemCountScoringStrategy(Corner itemType, int scorePerItem) {
        if (itemType == null || itemType == Corner.EMPTY) throw new RuntimeException("itemType must be a valid item");

        this.itemToCount = itemType;
        this.scorePerItem = scorePerItem;
    }

    /**
     * Computes the score obtained by the player when placing a card using the ItemCountScoringStrategy.
     * This strategy awards score based on the number of the specified item present in the player's inventory.
     *
     * @param player          the Player who is placing the card.
     * @param placingLocation the location on the game board where the card is being placed (not used in this strategy).
     * @return the score obtained by the player upon placement.
     */
    @Override
    public int evaluate(Player player, CardLocation placingLocation) {
        return scorePerItem * player.getInventory().count(itemToCount);
    }

    @Override
    public String toString() {
        return this.scorePerItem + " x " + itemToCount;
    }
}
