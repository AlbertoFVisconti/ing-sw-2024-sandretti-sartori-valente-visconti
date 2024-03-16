package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Corner;

public class StartCard extends Card {
    private final Corner[] backCorners;

    private final ItemCollection permanentResources;

    protected StartCard(Corner frontTopLeft, Corner frontTopRight, Corner frontBottomLeft, Corner frontBottomRight,
                        Corner backTopLeft, Corner backTopRight, Corner backBottomLeft, Corner backBottomRight,
                        ItemCollection permanentResources) {
        super(frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight);
        this.backCorners = new Corner[]{backTopLeft, backTopRight, backBottomLeft, backBottomRight};
        this.permanentResources = new ItemCollection(permanentResources);
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
