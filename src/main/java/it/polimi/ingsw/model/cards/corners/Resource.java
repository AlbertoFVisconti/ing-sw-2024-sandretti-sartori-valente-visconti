package it.polimi.ingsw.model.cards.corners;

public enum Resource {
    FUNGUS(Corner.FUNGUS),
    PLANT(Corner.PLANT),
    ANIMAL(Corner.ANIMAL),
    INSECT(Corner.INSECT);

    private final Corner equivalentCorner;

    Resource(Corner equivalentCorner) {
        this.equivalentCorner = equivalentCorner;
    }

    public Corner getCorner() {
        return this.equivalentCorner;
    }
}
