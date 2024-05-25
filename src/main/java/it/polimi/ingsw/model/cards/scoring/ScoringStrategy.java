package it.polimi.ingsw.model.cards.scoring;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CardLocation;

import java.io.Serializable;

/**
 * The ScoringStrategy functional interface allows providing a function that evaluates a card placement.
 * This function will return an integer value that represents the score the player obtained placing the card
 */
public interface ScoringStrategy extends Serializable {
    /**
     * Computes the score obtained by a player by placing a Card with the implemented scoring strategy
     * in a specific location on their board.
     *
     * @param player          Player who is placing the card
     * @param placingLocation the location on the game board where the card is being placed.
     * @return the score obtained by the player
     */
    int evaluate(Player player, CardLocation placingLocation);
}
