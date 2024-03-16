package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.cards.corners.Corner;
import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.cards.scoring.FreeScoreScoringStrategy;
import it.polimi.ingsw.model.cards.scoring.ScoringStrategy;

public abstract class Card {
    private boolean isBack=false;
    private boolean isPlaced=false;
    private int placementTurn;

    private final Corner[] corners;

    protected Card(Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight) {
        this.corners = new Corner[]{topLeft, topRight, bottomLeft, bottomRight};
    }

    public void flip()  {
        if(isPlaced) throw new UnsupportedOperationException("can't flip a placed card");
        this.isBack = !this.isBack;
    }

    public void place(int currentTurn) {
        this.isPlaced = true;
        this.placementTurn = currentTurn;
    }

    public boolean placedAfter(Card card) throws Exception {
        if (!card.isPlaced || !this.isPlaced) throw new Exception("cannot check placement order if one of the cards isn't placed");
        return this.placementTurn > card.placementTurn;
    }

    public boolean isOnBackSide() {
        return isBack;
    }

    protected Corner getCorner(int index) {
        return corners[index];
    }

    final public Corner getTopLeftCorner() {
        return this.getCorner(0);
    }
    final public Corner getTopRightCorner() {
        return this.getCorner(1);
    }
    final public Corner getBottomLeftCorner() {
        return this.getCorner(2);
    }
    final public Corner getBottomRightCorner() {
        return this.getCorner(3);
    }

    public static StartCard generateStartCard(Corner frontTopLeft, Corner frontTopRight, Corner frontBottomLeft, Corner frontBottomRight,
                                              Corner backTopLeft, Corner backTopRight, Corner backBottomLeft, Corner backBottomRight,
                                              ItemCollection permanentResources) {
        return new StartCard(frontTopLeft, frontTopRight, frontBottomLeft, frontBottomRight,
                backTopLeft, backTopRight, backBottomLeft, backBottomRight,
                permanentResources);
    }

    public static PlayCard generateGoldCard(Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight,
                                            Resource resourceType,
                                            ItemCollection placementConstraint,
                                            ScoringStrategy scoringStrategy) {
        return new PlayCard(topLeft, topRight, bottomLeft, bottomRight, resourceType,
                true,
                placementConstraint,
                scoringStrategy);
    }

    public static PlayCard generateResourceCard(Corner topLeft, Corner topRight, Corner bottomLeft, Corner bottomRight,
                                                Resource resourceType,
                                                int scoreUponPlacement) {
        return new PlayCard(topLeft, topRight, bottomLeft, bottomRight, resourceType,
                false,
                null,
                new FreeScoreScoringStrategy(scoreUponPlacement));
    }

    public ItemCollection collectItems() {
        return new ItemCollection()
                .add(getTopLeftCorner())
                .add(getTopRightCorner())
                .add(getBottomLeftCorner())
                .add(getBottomRightCorner());
    }
}
