package it.polimi.ingsw.model.cards.corners;

/**
 * Subset of the Corner enum representing only the Resource types that can be
 * used as Card types. It is possible to convert a Resource object to the
 * matching Corner object.
 */
public enum Resource {
    FUNGUS(Corner.FUNGUS, "/image/resource_card1b.png", "/image/golden_card1b.png"),
    PLANT(Corner.PLANT, "/image/resource_card2b.png", "/image/golden_card2b.png"),
    ANIMAL(Corner.ANIMAL, "/image/resource_card3b.png", "/image/golden_card3b.png"),
    INSECT(Corner.INSECT, "/image/resource_card4b.png", "/image/golden_card4b.png");

    private final Corner equivalentCorner;
    private final String backpath;
    private final String goldenBackpath;

    /**
     * Builds a Resource constant
     *
     * @param equivalentCorner the matching Corner object
     * @param backpath the path to the image of the back of a generic resource card with the specified type
     * @param goldenBackpath the path to the image of the back of a generic golden card with the specified type
     */
    Resource(Corner equivalentCorner, String backpath, String goldenBackpath) {
        this.equivalentCorner = equivalentCorner;
        this.backpath=backpath;
        this.goldenBackpath=goldenBackpath;

    }

    /**
     * Turns the Resource object into its matching Corner object
     *
     * @return a Corner object matching the Resource
     */
    public Corner getCorner() {
        return this.equivalentCorner;
    }

    /**
     * Retrieves the path to the image of the back of a generic resource card with the specified type
     *
     * @return the path to the image of the back of a generic resource card with the specified type
     */
    public String getPath(){
        return this.backpath;
    }

    /**
     * Retrieves the path to the image of the back of a generic golden card with the specified type
     *
     * @return the path to the image of the back of a generic golden card with the specified type
     */
    public String getGoldenPath(){
        return this.goldenBackpath;
    }
}
