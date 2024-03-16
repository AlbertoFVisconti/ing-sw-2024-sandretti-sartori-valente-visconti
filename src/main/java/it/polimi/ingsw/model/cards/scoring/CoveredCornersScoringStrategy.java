package it.polimi.ingsw.model.cards.scoring;

import it.polimi.ingsw.model.player.Player;

import java.awt.*;

/**
 * A scoring strategy that awards points based on the number of corners covered by the card upon placement.
 * Each covered corner contributes a fixed score to the player's total score.
 */
public class CoveredCornersScoringStrategy implements ScoringStrategy {
    private final int scorePerCoveredCorner;

    /**
     * Constructs a new CoveredCornersScoringStrategy with the specified score per covered corner.
     *
     * @param scorePerCoveredCorner the score awarded per covered corner.
     */
    public CoveredCornersScoringStrategy(int scorePerCoveredCorner) {
        this.scorePerCoveredCorner = scorePerCoveredCorner;
    }

    /**
     * Computes the score obtained by the player when placing a card using the CoveredCornersScoringStrategy.
     * This strategy awards points based on the number of covered corners adjacent to the placement location.
     *
     * @param player          Player who is placing the card.
     * @param placingLocation the location on the game board where the card is being placed.
     * @return the score obtained by the player upon placement.
     */
    @Override
    public int evaluate(Player player, Point placingLocation) {
        int     countNeighbour = 0,
                x = placingLocation.x,
                y = placingLocation.y;

        if(player.getPlacedCard(new Point(x-1,y+1)) != null) countNeighbour++;
        if(player.getPlacedCard(new Point(x+1,y+1)) != null) countNeighbour++;
        if(player.getPlacedCard(new Point(x-1,y-1)) != null) countNeighbour++;
        if(player.getPlacedCard(new Point(x+1,y-1)) != null) countNeighbour++;

        return countNeighbour*scorePerCoveredCorner;
    }

    @Override
    public String toString() {
        return this.scorePerCoveredCorner + " points per covered corner";
    }
}
