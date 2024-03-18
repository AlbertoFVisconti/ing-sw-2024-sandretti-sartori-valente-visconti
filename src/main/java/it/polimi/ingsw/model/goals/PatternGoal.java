package it.polimi.ingsw.model.goals;

import it.polimi.ingsw.model.cards.corners.Resource;
import it.polimi.ingsw.model.player.Player;

import java.util.Arrays;

/**
 * Represents a goal based on matching a specific pattern of resources on the player's board.
 * This goal computes the score based on how many times the pattern is found in the player's board
 */
public class PatternGoal implements Goal {
    private final int scorePerMatch;
    private final Resource[][] pattern;

    /**
     * Constructs a new PatternGoal with the specified pattern and score per match.
     *
     * @param pattern       pattern of resources to match on the player's board.
     * @param scorePerMatch score awarded per matching pattern.
     */
    public PatternGoal(Resource[][] pattern, int scorePerMatch) {
        this.scorePerMatch = scorePerMatch;
        this.pattern = pattern.clone();
    }

    /**
     * Evaluates the score obtained by the player based on how many times the pattern is found in the player's board.
     *
     * @param player Player whose board needs to be evaluated.
     * @return the score obtained by the player.
     */
    @Override
    public int evaluate(Player player) {
        // TODO
        return 0;
    }

    @Override
    public String toString() {
        return "PatternGoal{" +
                "scorePerMatch=" + scorePerMatch +
                ", pattern=" + Arrays.deepToString(pattern) +
                '}';
    }
}
