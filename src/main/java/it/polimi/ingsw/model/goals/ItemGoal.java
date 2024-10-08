package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.ItemCollection;

/**
 * Represents a goal based on collecting specific items.
 * This goal computes the score based on the number of sets of items collected by the player.
 */
public class ItemGoal extends Goal {
    private final ItemCollection items;

    /**
     * Constructs a new ItemGoal with the specified ItemCollection and score per set.
     *
     * @param items       ItemCollection of items required to satisfy the goal.
     * @param scorePerSet score obtained per complete set of items.
     */
    public ItemGoal(ItemCollection items, int scorePerSet, String path) {
        this.items = new ItemCollection(items);
        this.goalValue = scorePerSet;
        this.path=path;
    }

    /**
     * Evaluates the score the player achieved collecting required items.
     *
     * @param player Player whose inventory needs to be evaluated.
     * @return the score obtained by the player
     */
    @Override
    public int evaluate(Player player) {
        return goalValue * player.getInventory().divide(this.items);
    }

    /**
     * Retrieves the goal's required items
     *
     * @return ItemCollection containing the goal's required items
     */
    public ItemCollection getItems() {
        return new ItemCollection(items);
    }

    @Override
    public String toString() {
        return "ItemGoal{" +
                "scorePerSet=" + goalValue +
                ", items=" + items +
                '}';
    }
}
