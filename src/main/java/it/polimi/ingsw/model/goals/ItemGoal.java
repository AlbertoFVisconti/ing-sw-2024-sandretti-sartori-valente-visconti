package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.utils.ItemCollection;
import it.polimi.ingsw.model.player.Player;

/**
 * Represents a goal based on collecting specific items.
 * This goal computes the score based on the number of sets of items collected by the player.
 */
public class ItemGoal extends Goal {
    private final int scorePerSet;
    private final ItemCollection items;

    /**
     * Constructs a new ItemGoal with the specified ItemCollection and score per set.
     *
     * @param items       ItemCollection of items required to satisfy the goal.
     * @param scorePerSet score obtained per complete set of items.
     */
    public ItemGoal(ItemCollection items, int scorePerSet) {
        this.items = new ItemCollection(items);
        this.scorePerSet = scorePerSet;
    }

    /**
     * Evaluates the score the player achieved collecting required items.
     *
     * @param player Player whose inventory needs to be evaluated.
     * @return the score obtained by the player
     */
    @Override
    public int evaluate(Player player) {
        return scorePerSet* player.getInventory().divide(this.items);
    }

    @Override
    public String toString() {
        return "ItemGoal{" +
                "scorePerSet=" + scorePerSet +
                ", items=" + items +
                '}';
    }
}
