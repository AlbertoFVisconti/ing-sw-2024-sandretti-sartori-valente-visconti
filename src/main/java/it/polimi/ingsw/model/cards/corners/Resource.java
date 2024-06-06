package it.polimi.ingsw.model.cards.corners;

public enum Resource {
    FUNGUS(Corner.FUNGUS, "/image/resource_card1b.png", "/image/golden_card1b.png"),
    PLANT(Corner.PLANT, "/image/resource_card2b.png", "/image/golden_card2b.png"),
    ANIMAL(Corner.ANIMAL, "/image/resource_card3b.png", "/image/golden_card3b.png"),
    INSECT(Corner.INSECT, "/image/resource_card4b.png", "/image/golden_card4b.png");

    private final Corner equivalentCorner;
    private final String backpath;
    private final String goldenBackpath;

    Resource(Corner equivalentCorner, String backpath, String goldenBackpath) {
        this.equivalentCorner = equivalentCorner;
        this.backpath=backpath;
        this.goldenBackpath=goldenBackpath;

    }

    public Corner getCorner() {
        return this.equivalentCorner;
    }
    public String getPath(){
        return this.backpath;
    }
    public String getGoldenPath(){
        return this.goldenBackpath;
    }
}
