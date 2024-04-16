package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The ScoreBoard class represents a scoreboard that tracks players' score throughout the game.
 */
public class ScoreBoard {
    private final HashMap<Player, Integer> scores;

    /**
     * Construct a new ScoreBoard object for the given list of players.
     * Initial scores are set to 0 for each player.
     *
     * @param players A list of Players participating in the game.
     */
    public ScoreBoard(List<Player> players) {
        scores = new HashMap<>();

        for (Player p : players) {
            scores.put(p,0);
        }
    }

    /**
     * Retrieves the score of the specified player from the scoreboard.
     *
     * @param player the Player whose score is to be retrieved
     * @return The score of the specified player.
     */
    public int getScore(Player player) {
        if(!scores.containsKey(player)) throw new NoSuchElementException("This player is not in the scoreboard");
        return scores.get(player);
    }

    /**
     * Sets the score for the specified player in the scoreboard.
     *
     * @param player the Player whose score needs to be set.
     * @param score the new score to set for the player.
     * @throws NoSuchElementException if the provided player is not in the scoreboard
     */
    public void setScore(Player player, int score) {
        if(!scores.containsKey(player)) throw new NoSuchElementException("This player is not in the scoreboard");
        scores.put(player,score);
    }

    public void addScore(Player player, int scoreDelta) {
        if(!scores.containsKey(player)) throw new NoSuchElementException("This player is not in the scoreboard");
        scores.put(player,scores.get(player) + scoreDelta);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for(Player p : this.scores.keySet()) {
            s.append(p.nickName).append(": ").append(scores.get(p)).append("\n");
        }

        return s.toString();
    }
}
