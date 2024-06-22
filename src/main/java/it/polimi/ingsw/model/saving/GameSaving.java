package it.polimi.ingsw.model.saving;

import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.decks.Deck;
import it.polimi.ingsw.model.goals.Goal;

import java.io.Serializable;
import java.util.ArrayList;

public class GameSaving implements Serializable {
    private final int expectedPlayers;
    private final ArrayList<PlayerSaving> players;
    private final String gameId;
    private final Deck<PlayCard> goldCardsDeck;
    private final Deck<PlayCard> resourceCardsDeck;
    private final PlayCard[] visibleCards;
    private final ScoreBoard scoreBoard;
    private final Goal[] publicGoal;
    private final Deck<StartCard> startCardsDeck;
    private final Deck<Goal> goalsDeck;
    private final int currentTurn;

    public GameSaving(int expectedPlayers, ArrayList<PlayerSaving> players, String gameId,
                      Deck<PlayCard> goldCardsDeck, Deck<PlayCard> resourceCardsDeck,
                      PlayCard[] visibleCards, ScoreBoard scoreBoard, Goal[] publicGoal,
                      Deck<StartCard> startCardsDeck, Deck<Goal> goalsDeck, int currentTurn) {
        this.expectedPlayers = expectedPlayers;
        this.players = players;
        this.gameId = gameId;
        this.scoreBoard = scoreBoard;
        this.publicGoal = publicGoal;
        this.goldCardsDeck = goldCardsDeck;
        this.resourceCardsDeck = resourceCardsDeck;
        this.visibleCards = visibleCards;
        this.startCardsDeck = startCardsDeck;
        this.goalsDeck = goalsDeck;
        this.currentTurn = currentTurn;
    }

    public int getExpectedPlayers() {
        return expectedPlayers;
    }

    public ArrayList<PlayerSaving> getPlayers() {
        return players;
    }

    public String getGameId() {
        return gameId;
    }

    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    public Goal[] getPublicGoal() {
        return publicGoal;
    }

    public Deck<PlayCard> getGoldCardsDeck() {
        return goldCardsDeck;
    }

    public Deck<PlayCard> getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    public PlayCard[] getVisibleCards() {
        return visibleCards;
    }

    public Deck<Goal> getGoalsDeck() {
        return goalsDeck;
    }

    public Deck<StartCard> getStartCardsDeck() {
        return startCardsDeck;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }
}
