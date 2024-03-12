package it.polimi.ingsw.model.cards.scoring;

import it.polimi.ingsw.model.player.Player;

import java.awt.*;

public class CoveredCornersScoringStrategy implements ScoringStrategy {
    private int scorePerCoveredCorner;

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
        // TODO
        return 0;
    }
}
