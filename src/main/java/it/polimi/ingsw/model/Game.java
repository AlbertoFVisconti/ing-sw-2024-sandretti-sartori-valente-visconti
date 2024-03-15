package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.goals.GoalDeck;
import it.polimi.ingsw.model.player.Player;

import java.util.Collections;
import java.util.List;

public class Game {
    private int currentTurn;
    private List<Player> players;

    private boolean isFinal;
    private boolean isStarted;

    private Goal[] commonGoals;

    private Deck goldCardsDeck;
    private Deck resourceCardsDeck;
    private GoalDeck goalsDeck;

    private ScoreBoard scoreBoard;

    public Game(/*TODO*/) {
        // TODO
    }

    public void addPlayer(Player player) throws Exception {
        if(isStarted) throw new Exception();

        players.add(player);
    }

    public Player getTurn() {
        return players.get(currentTurn);
    }

    public void nextTurn() throws Exception {
        if(!isStarted) throw new Exception();
        currentTurn = (currentTurn+1)%players.size();
    }

    public void setFinalRound() throws Exception {
        if(!isStarted) throw new Exception();
        this.isFinal = true;
    }

    public boolean isFinalRound() {
        return isFinal;
    }

    public void shufflePlayers() throws Exception {
        if(isStarted) throw new Exception();
        Collections.shuffle(this.players);
    }

    public Goal[] getCommonGoals() {
        return this.commonGoals.clone();
    }

    public void setCommonGoals(Goal commonGoal1, Goal commonGoal2) throws Exception {
        if(isStarted) throw new Exception();
        this.commonGoals = new Goal[2];
        this.commonGoals[0] = commonGoal1;
        this.commonGoals[1] = commonGoal2;
    }

    public void startGame() {
        this.isStarted = true;
    }

    public boolean isGameStarted() {
        return this.isStarted;
    }

}
