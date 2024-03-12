package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Item;
import it.polimi.ingsw.model.cards.corners.CornerStatus;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.ScoringStrategy;

public class PlayCard extends Card {
    final ItemCollection constraint = null;

    final ScoringStrategy scoringStrategy = null;

    final Resource type = null;
    private boolean isGold;

    public ItemCollection getConstraint() {
        return new ItemCollection(constraint);
    }

    public ScoringStrategy getScoringStrategy() {
        return scoringStrategy;
    }


    protected PlayCard() {}

    public Resource getType() {
        return type;
    }

    @Override
    protected Item getCorner(int index) {
        if(!this.isOnBackSide())
            return super.getCorner(index);

        return CornerStatus.EMPTY;
    }

    @Override
    public ItemCollection collectItems() {
        if(isOnBackSide()) {
            return new ItemCollection().add(this.type);
        }
        return super.collectItems();
    }
}
