package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.player.Player;

/**
 * The Goal functional interface allows providing a function that evaluates a player's board (or inventory).
 * This function will return an integer value that represents the score the player obtained by satisfying
 * certain conditions.
 */
public interface Goal {
    /**
     * Evaluates the score the player achieved based on certain conditions on their board or inventory.
     *
     * @param player Player whose board or inventory needs to be evaluated.
     * @return score obtained by the player.
     */
    int evaluate(Player player);
}
