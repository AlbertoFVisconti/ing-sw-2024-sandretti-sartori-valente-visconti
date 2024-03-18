package it.polimi.ingsw.model;

import it.polimi.ingsw.model.decks.PlayCardDeck;
import it.polimi.ingsw.model.decks.StartCardDeck;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.decks.GoalDeck;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private int currentTurn;
    private List<Player> players;

    private boolean isFinal;
    private boolean isStarted;

    private Goal[] commonGoals;

    private final PlayCardDeck goldCardsDeck;
    private final PlayCardDeck resourceCardsDeck;
    private final StartCardDeck startCardsDeck;
    private final GoalDeck goalsDeck;

    private ScoreBoard scoreBoard;

    public Game(String goldCardFileName, String resourceCardFileName, String startCardFileName, String goalFileName) throws IOException {
        this.goldCardsDeck = new PlayCardDeck(goldCardFileName);
        this.resourceCardsDeck = new PlayCardDeck(resourceCardFileName);
        this.startCardsDeck = new StartCardDeck(startCardFileName);
        this.goalsDeck = new GoalDeck(goalFileName);
        this.players = new ArrayList<>();
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

    private void setCommonGoals(Goal commonGoal1, Goal commonGoal2)  {
        if(isStarted) throw new RuntimeException();
        this.commonGoals = new Goal[2];
        this.commonGoals[0] = commonGoal1;
        this.commonGoals[1] = commonGoal2;
    }

    public void startGame() {
        if(isStarted) return;

        this.setCommonGoals(
                this.goalsDeck.draw(),
                this.goalsDeck.draw()
        );

        for(Player player : this.players) {
            player.setPrivateGoal(this.goalsDeck.draw());
            player.setStartCard(this.startCardsDeck.draw());

            player.setPlayerCard(this.resourceCardsDeck.draw(), 0);
            player.setPlayerCard(this.resourceCardsDeck.draw(), 1);
            player.setPlayerCard(this.goldCardsDeck.draw(), 2);
        }

        this.scoreBoard = new ScoreBoard(players);

        this.currentTurn = 0;
        this.isFinal = false;
        this.isStarted = true;
    }

    public boolean isGameStarted() {
        return this.isStarted;
    }

}
