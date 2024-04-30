package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.decks.Drawable;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

/**
 * The Goal class provides an abstract representation of an object that describes a goal. This object has only one
 * method serving as interface, such method evaluates a player's board (or inventory).
 * This function will return an integer value that represents the score the player obtained by satisfying
 * certain conditions.
 */
public abstract class Goal implements Drawable, Serializable {
    /**
     * Evaluates the score the player achieved based on certain conditions on their board or inventory.
     *
     * @param player Player whose board or inventory needs to be evaluated.
     * @return score obtained by the player.
     */
    abstract public int evaluate(Player player);

    /**
     * @return {@code null}
     */
    @Override
    public Resource getCardResourceType() {
        return null;
    }
}
