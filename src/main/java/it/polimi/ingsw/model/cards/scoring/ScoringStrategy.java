package it.polimi.ingsw.model.cards.scoring;

import it.polimi.ingsw.model.Player;

import java.awt.*;

public interface ScoringStrategy {
    int evaluate(Player player, Point placingLocation);
}
