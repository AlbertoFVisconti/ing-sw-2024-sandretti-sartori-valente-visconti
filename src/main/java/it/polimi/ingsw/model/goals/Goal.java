package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.player.Player;

public interface Goal {
    int evaluate(Player player);
}
