package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;
import java.util.List;

public class ScoreBoard {
    private final HashMap<Player, Integer> scores;

    public ScoreBoard(List<Player> players) {
        scores = new HashMap<>();

        for (Player p : players) {
            scores.put(p,0);
        }
    }

    public int getScore(Player player) {
        return scores.get(player);
    }

    public void setScore(Player player, int score) {
        scores.put(player,score);
    }
}
