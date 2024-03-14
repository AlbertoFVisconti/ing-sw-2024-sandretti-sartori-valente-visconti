package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Corner;

public class StartCard extends Card {
    private final Corner[] backCorners = null;

    private final ItemCollection permanentResources = null;

    protected StartCard(/*TODO*/) {
        super(/*TODO*/);
        // TODO
    }

    @Override
    protected Corner getCorner(int index) {
        if (this.isOnBackSide()) {
            return backCorners[index];
        }
        return super.getCorner(index);

    }

    @Override
    public ItemCollection collectItems() {
        if(isOnBackSide()) {
            return new ItemCollection(permanentResources);
        }
        return super.collectItems();
    }
}
