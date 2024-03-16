package it.polimi.ingsw.model.cards.scoring;

import it.polimi.ingsw.model.player.Player;

import java.awt.*;

public class CoveredCornersScoringStrategy implements ScoringStrategy {
    private final int scorePerCoveredCorner;

    private static CoveredCornersScoringStrategy defaultCoveredCornersScoringStrategy=null;

    public static CoveredCornersScoringStrategy getDefault() {
        if (defaultCoveredCornersScoringStrategy == null) {
            defaultCoveredCornersScoringStrategy = new CoveredCornersScoringStrategy(2);
        }

        return defaultCoveredCornersScoringStrategy;
    }

    public CoveredCornersScoringStrategy(int scorePerCoveredCorner) {
        this.scorePerCoveredCorner = scorePerCoveredCorner;
    }

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
