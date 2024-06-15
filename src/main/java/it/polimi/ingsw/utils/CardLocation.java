package it.polimi.ingsw.utils;

import java.io.Serializable;

/**
 * Record that allows to describe the location on the player's board of a placed card.
 * It can only contain legal coordinates.
 *
 * @param x the horizontal coordinate of the card's location
 * @param y the vertical coordinate of the card's location
 */
public record CardLocation(int x, int y) implements Serializable {
    /**
     * Builds a new CardLocation element.
     * Because of how the cards are represented on the board, two coordinates are a valid cards location
     * only if they're both odd or both even.
     *
     * @param x the horizontal coordinate of the card's location
     * @param y the vertical coordinate of the card's location
     * @throws IllegalArgumentException if the provided coordinates are not valid
     */
    public CardLocation {
        if ((Math.abs(x) % 2) != (Math.abs(y) % 2))
            throw new IllegalArgumentException("Card location invalid: card location's coordinates should be both even or bot odd");
    }

    /**
     * Creates a new CardLocation as the sum of the current object and the given one.
     * Since we assume that both object are valid (since they can't be created otherwise), the
     * result will always be a valid CardLocation.
     *
     * @param c CardLocation object to add to the current one.
     * @return a new CardLocation object representing the sum of the current and the given one.
     */
    public CardLocation add(CardLocation c) {
        return new CardLocation(this.x + c.x, this.y + c.y);
    }

    /**
     * Creates a new CardLocation as the sum of the current object and the given coordinates' deltas.
     * If the deltas are invalid (the first even and the second odd, or vice versa) an exception
     * will be launched by the constructor.
     *
     * @param dx the horizontal coordinate's delta
     * @param dy the vertical coordinate's delta
     * @return a new CardLocation object representing the sum of the current one and the given deltas
     */
    public CardLocation add(int dx, int dy) {
        return new CardLocation(this.x + dx, this.y + dy);
    }

    /**
     * Creates a new CardLocation as the subtraction of the given object from the current one.
     * Since we assume that both object are valid (since they can't be created otherwise), the
     * result will always be a valid CardLocation.
     *
     * @param c CardLocation object to subtract from the current one.
     * @return a new CardLocation object representing the subtraction of the given from the current one.
     */
    public CardLocation sub(CardLocation c) {
        return new CardLocation(this.x - c.x, this.y - c.y);
    }

    /**
     * Creates a new CardLocation as the subtraction from the current object of the given coordinates' deltas.
     * If the deltas are invalid (the first even and the second odd, or vice versa) an exception
     * will be launched by the constructor.
     *
     * @param dx the horizontal coordinate's delta
     * @param dy the vertical coordinate's delta
     * @return a new CardLocation object representing the subtraction from the current one of the given deltas
     */
    public CardLocation sub(int dx, int dy) {
        return add(-dx, -dy);
    }

    /**
     * Return the location of the top-left neighbour of the cards which location is the current one.
     *
     * @return the location of the top-left neighbour
     */
    public CardLocation topLeftNeighbour() {
        return this.add(-1, 1);
    }

    /**
     * Return the location of the top-right neighbour of the cards which location is the current one.
     *
     * @return the location of the top-right neighbour
     */
    public CardLocation topRightNeighbour() {
        return this.add(1, 1);
    }

    /**
     * Return the location of the boom-left neighbour of the cards which location is the current one.
     *
     * @return the location of the bottom-left neighbour
     */
    public CardLocation bottomLeftNeighbour() {
        return this.add(-1, -1);
    }

    /**
     * Return the location of the bottom-right neighbour of the cards which location is the current one.
     *
     * @return the location of the bottom-right neighbour
     */
    public CardLocation bottomRightNeighbour() {
        return this.add(1, -1);
    }

    @Override
    public String toString() {
        return "{" + x + "," + y + "}";
    }

}
