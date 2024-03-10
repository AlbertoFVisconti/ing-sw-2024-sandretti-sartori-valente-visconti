package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.ScoringStrategy;

import java.util.Collection;

public class PlayCard extends Card {
    // TODO: define actual type for constraint, player inventory, starting card's permanent resources and goals
    final Collection<Resource> constraint = null;

    final ScoringStrategy scoringStrategy = null;

    final Resource type = null;
    private boolean isGold;

    public Collection<Resource> getConstraint() {
        return constraint;
    }

    public ScoringStrategy getScoringStrategy() {
        return scoringStrategy;
    }

    public Resource getType() {
        return type;
    }

    @Override
    protected Corner getCorner(int index) {
        if(!this.isOnBackSide())
            return super.getCorner(index);

        return null;
    }

    public static PlayCard generateGoldCard() {
        PlayCard c = new PlayCard();
        c.isGold = true;
        return c;
    }

    public static PlayCard generateResourceCard() {
        PlayCard c = new PlayCard();
        c.isGold = false;
        return c;
    }
    private PlayCard() {}
}
