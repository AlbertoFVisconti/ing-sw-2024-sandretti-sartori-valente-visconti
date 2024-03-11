package it.polimi.ingsw.model;

import it.polimi.ingsw.model.player.Player;

import java.util.HashMap;

public class ScoreBoard {
    private HashMap<Player, Integer> scores;

    public int getScore(Player player) {
        return scores.get(player);
    }

    public void setScore(Player player, int score) {
        scores.put(player,score);
    }
}
