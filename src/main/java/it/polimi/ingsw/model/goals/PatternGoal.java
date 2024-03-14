package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.player.Player;

public class PatternGoal implements Goal {
    private final int scorePerMatch;
    private final Resource[][] pattern;
    @Override
    public int evaluate(Player player) {
        // TODO
        return 0;
    }

    public PatternGoal(Resource[][] pattern, int scorePerMatch) {
        this.scorePerMatch = scorePerMatch;
        this.pattern = pattern.clone();
    }
}
