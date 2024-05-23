package it.polimi.ingsw.model;

import it.polimi.ingsw.events.Observable;
import it.polimi.ingsw.events.messages.server.ScoreUpdateMessage;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The ScoreBoard class represents a scoreboard that tracks players' score throughout the game.
 */
public class ScoreBoard extends Observable implements Serializable {
    private HashMap<String, Integer> scores;

    /**
     * Construct a new ScoreBoard object for the given list of players.
     * Initial scores are set to 0 for each player.
     *
     * @param players A list of Players participating in the game.
     */
    public ScoreBoard(List<Player> players) {
        scores = new HashMap<>();

        for (Player p : players) {
            scores.put(p.nickName, 0);
        }
    }

    /**
     * Construct an empty ScoreBoard object.
     * No operation is allowed on such object beside copyScore.
     * This constructor is used in the client's model to set up a disposable object that will be overridden as soon as an
     * updated scoreboard is received.
     */
    public ScoreBoard() {
        this.scores = null;
    }

    /**
     * Retrieves the score of the specified player from the scoreboard.
     *
     * @param nickName the nickname of the Player whose score is to be retrieved
     * @return The score of the specified player.
     */
    public int getScore(String nickName) {
        if (!scores.containsKey(nickName)) throw new NoSuchElementException("This player is not in the scoreboard");
        return scores.get(nickName);
    }

    /**
     * Sets the score for the specified player in the scoreboard.
     *
     * @param nickName the nickname of the Player whose score needs to be set.
     * @param score    the new score to set for the player.
     * @throws NoSuchElementException if the provided player is not in the scoreboard
     */
    public void setScore(String nickName, int score) {
        if (!scores.containsKey(nickName)) throw new NoSuchElementException("This player is not in the scoreboard");
        scores.put(nickName, score);

        notifyObservers(new ScoreUpdateMessage(this));
    }

    /**
     * Increases the score of a player in the scoreboard by a specified amount.
     *
     * @param nickName   the nickname of the Player whose score needs to be updated
     * @param scoreDelta the amount to add to the Player's score.
     */
    public void addScore(String nickName, int scoreDelta) {
        if (!scores.containsKey(nickName)) throw new NoSuchElementException("This player is not in the scoreboard");
        this.setScore(nickName, scores.get(nickName) + scoreDelta);
    }

    public void copyScore(ScoreBoard scoreBoard) {
        this.scores = new HashMap<>();
        for (String nickname : scoreBoard.scores.keySet()) {
            this.scores.put(nickname, scoreBoard.scores.get(nickname));
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();

        for (String nickname : this.scores.keySet()) {
            s.append(nickname).append(": ").append(scores.get(nickname)).append("\n");
        }

        return s.toString();
    }
}
