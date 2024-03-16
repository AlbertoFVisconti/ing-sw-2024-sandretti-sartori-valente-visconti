package it.polimi.ingsw.model.cards.scoring;

import it.polimi.ingsw.model.player.Player;

import java.awt.*;

public class FreeScoreScoringStrategy implements ScoringStrategy {

    private final int freeScore;
    @Override
    public int evaluate(Player player, Point placingLocation) {
        return freeScore;
    }

    public FreeScoreScoringStrategy(int freeScore) {
        this.freeScore = freeScore;
    }

    @Override
    public String toString() {
        return this.freeScore + " points upon placement";
    }
}
