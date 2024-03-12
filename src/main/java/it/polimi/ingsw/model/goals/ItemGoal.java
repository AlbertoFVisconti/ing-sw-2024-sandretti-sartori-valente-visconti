package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.ItemCollection;
import it.polimi.ingsw.model.player.Player;

public class ItemGoal implements Goal {
    private final int scorePerSet;
    private final ItemCollection items;
    @Override
    public int evaluate(Player player) {
        return scorePerSet* player.getInventory().divide(this.items);
    }

    public ItemGoal(ItemCollection items, int scorePerSet) {
        this.items = new ItemCollection(items);
        this.scorePerSet = scorePerSet;
    }
}
