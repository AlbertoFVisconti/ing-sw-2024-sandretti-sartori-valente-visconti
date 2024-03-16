package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.ScoringStrategy;

public class PlayCard extends Card {
    final ItemCollection constraint;

    final ScoringStrategy scoringStrategy;

    final Resource type;
    protected boolean isGold;

    public ItemCollection getConstraint() {
        return new ItemCollection(constraint);
    }

    public ScoringStrategy getScoringStrategy() {
        return scoringStrategy;
    }


    protected PlayCard(Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight,
                       Resource resourceType,
                       boolean isGold,
                       ItemCollection placementConstraint,
                       ScoringStrategy scoringStrategy) {
        super(topLeft, topRight, bottomLeft, bottomRight);
        this.isGold = isGold;

        if(isGold) this.constraint = new ItemCollection(placementConstraint);
        else this.constraint = new ItemCollection();

        this.scoringStrategy = scoringStrategy;
        this.type = resourceType;
    }

    public Resource getType() {
        return type;
    }

    @Override
    protected Corner getCorner(int index) {
        if(!this.isOnBackSide())
            return super.getCorner(index);

        return Corner.EMPTY;
    }

    @Override
    public ItemCollection collectItems() {
        if(isOnBackSide()) {
            return new ItemCollection().add(this.type.getCorner());
        }
        return super.collectItems();
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
