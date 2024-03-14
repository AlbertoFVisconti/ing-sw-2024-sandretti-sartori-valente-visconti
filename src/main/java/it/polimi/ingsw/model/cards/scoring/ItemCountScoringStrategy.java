package it.polimi.ingsw.model.cards.scoring;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.player.Player;

import java.awt.*;

public class ItemCountScoringStrategy implements ScoringStrategy {

    private final Corner itemToCount;
    private final int scorePerItem;
    @Override
    public int evaluate(Player player, Point placingLocation) {
        return scorePerItem*player.getInventory().count(itemToCount);
    }

    public ItemCountScoringStrategy(Corner itemType, int scorePerItem) throws Exception {
        if(itemType == null || itemType == Corner.EMPTY) throw new Exception("itemToCount must be a valid item");

        this.itemToCount = itemType;
        this.scorePerItem = scorePerItem;
    }
}
