package it.polimi.ingsw.model.cards.scoring;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.utils.CardLocation;

/**
 * A scoring strategy that awards a fixed score to the player upon placing a card.
 * The score obtained remains constant regardless of the placement location on the game board.
 */
public class FreeScoreScoringStrategy implements ScoringStrategy {

    private final int freeScore;

    /**
     * Constructs a new FreeScoreScoringStrategy with the specified fixed score.
     *
     * @param freeScore the fixed score obtained upon placement.
     */
    public FreeScoreScoringStrategy(int freeScore) {
        this.freeScore = freeScore;
    }

    /**
     * Computes the score obtained by the player when placing a card using the FreeScoreScoringStrategy.
     * This strategy awards a fixed score regardless of the placement location.
     *
     * @param player          the Player who is placing the card.
     * @param placingLocation the location on the game board where the card is being placed.
     * @return the score obtained to the player upon placement.
     */
    @Override
    public int evaluate(Player player, CardLocation placingLocation) {
        return freeScore;
    }

    @Override
    public String toString() {
        return this.freeScore + " pt";
    }
}
