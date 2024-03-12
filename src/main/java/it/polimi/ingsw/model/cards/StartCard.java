package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Item;
import it.polimi.ingsw.model.cards.corners.CornerStatus;

public class StartCard extends Card {
    private final CornerStatus[] backCorners = null;

    private final ItemCollection permanentResources = null;

    @Override
    protected Item getCorner(int index) {
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

    protected StartCard() {}
}
