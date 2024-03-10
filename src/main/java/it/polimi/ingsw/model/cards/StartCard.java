package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;

import java.util.Collection;

public class StartCard extends Card {
    private final Corner[] backCorners = null;

    private final Collection<Resource> permanentResources = null;

    @Override
    protected Corner getCorner(int index) {
        if (this.isOnBackSide())
            return super.getCorner(index);
        return backCorners[index];
    }
}
