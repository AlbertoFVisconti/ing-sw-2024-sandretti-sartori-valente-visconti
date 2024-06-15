package it.polimi.ingsw.model.cards.corners;

/**
 * Subset of the Corner enum representing only the Resource types that can be
 * used as Card types. It is possible to convert a Resource object to the
 * matching Corner object.
 */
public enum Resource {
    FUNGUS(Corner.FUNGUS, "/image/resource_cards/resource_card1b.png", "/image/golden_cards/golden_card1b.png"),
    PLANT(Corner.PLANT, "/image/resource_cards/resource_card2b.png", "/image/golden_cards/golden_card2b.png"),
    ANIMAL(Corner.ANIMAL, "/image/resource_cards/resource_card3b.png", "/image/golden_cards/golden_card3b.png"),
    INSECT(Corner.INSECT, "/image/resource_cards/resource_card4b.png", "/image/golden_cards/golden_card4b.png");

    // the Corner constant that represent the same item as the Resource constant
    private final Corner equivalentCorner;

    // the path to the image that contains the back side of a generic resource card with the type described by the constant
    private final String backPath;

    // the path to the image that contains the back side of a generic gold card with the type described by the constant
    private final String goldenBackPath;

    /**
     * Builds a Resource constant
     *
     * @param equivalentCorner the matching Corner object
     * @param backPath the path to the image of the back of a generic resource card with the specified type
     * @param goldenBackPath the path to the image of the back of a generic golden card with the specified type
     */
    Resource(Corner equivalentCorner, String backPath, String goldenBackPath) {
        this.equivalentCorner = equivalentCorner;
        this.backPath = backPath;
        this.goldenBackPath = goldenBackPath;

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
        return this.backPath;
    }

    /**
     * Retrieves the path to the image of the back of a generic golden card with the specified type
     *
     * @return the path to the image of the back of a generic golden card with the specified type
     */
    public String getGoldenPath(){
        return this.goldenBackPath;
    }
}
