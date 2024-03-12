package it.polimi.ingsw.model.cards.scoring;

import it.polimi.ingsw.model.cards.corners.CornerStatus;
import it.polimi.ingsw.model.cards.corners.Item;
import it.polimi.ingsw.model.player.Player;

import java.awt.*;

public class ItemCountScoringStrategy implements ScoringStrategy {

    private final Item itemToCount;
    private final int scorePerItem;
    @Override
    public int evaluate(Player player, Point placingLocation) {
        // TODO
        return 0;
    }

    public ItemCountScoringStrategy(Item itemToCount, int scorePerItem) throws Exception {
        if(itemToCount instanceof CornerStatus) throw new Exception("itemToCount must be a valid item (artifact or resource)");

        this.itemToCount = itemToCount;
        this.scorePerItem = scorePerItem;
    }
}
