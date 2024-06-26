package it.polimi.ingsw.model.saving;

import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.decks.Deck;
import it.polimi.ingsw.model.goals.Goal;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * GameData allows to store and share information regarding a game (with no information
 * regarding the game phase)
 */
public class GameData implements Serializable {
    // the number of expected players
    private final int expectedPlayers;

    // the list of PlayerData object in the same order as the players in the game
    private final ArrayList<PlayerData> players;

    // the game identifier
    private final String gameId;

    // the golden cards deck
    private final Deck<PlayCard> goldCardsDeck;

    // the resource cards deck
    private final Deck<PlayCard> resourceCardsDeck;

    // the game's visible cards
    private final PlayCard[] visibleCards;

    // the game's scoreboard
    private final ScoreBoard scoreBoard;

    // the public goals
    private final Goal[] publicGoals;

    // the starting cards deck
    private final Deck<StartCard> startCardsDeck;

    // the goals deck
    private final Deck<Goal> goalsDeck;

    // the current turn number
    private final int currentTurn;

    /**
     * Builds a GameData message that describes a game whose data is provided.
     *
     * @param expectedPlayers the number of expected players
     * @param players ArrayList of PlayerData matching the list of players playing the game
     * @param gameId the game identifier
     * @param goldCardsDeck the game's golden cards deck
     * @param resourceCardsDeck the games' resource cards deck
     * @param visibleCards the game's visible cards
     * @param scoreBoard the game's scoreboard
     * @param publicGoals teh game's public goal
     * @param startCardsDeck the game's starting cards deck
     * @param goalsDeck the game's goals deck
     * @param currentTurn the current game's turn number
     */
    public GameData(int expectedPlayers, ArrayList<PlayerData> players, String gameId,
                    Deck<PlayCard> goldCardsDeck, Deck<PlayCard> resourceCardsDeck,
                    PlayCard[] visibleCards, ScoreBoard scoreBoard, Goal[] publicGoals,
                    Deck<StartCard> startCardsDeck, Deck<Goal> goalsDeck, int currentTurn) {
        this.expectedPlayers = expectedPlayers;
        this.players = players;
        this.gameId = gameId;
        this.scoreBoard = scoreBoard;
        this.publicGoals = publicGoals;
        this.goldCardsDeck = goldCardsDeck;
        this.resourceCardsDeck = resourceCardsDeck;
        this.visibleCards = visibleCards;
        this.startCardsDeck = startCardsDeck;
        this.goalsDeck = goalsDeck;
        this.currentTurn = currentTurn;
    }

    /**
     * Retrieves the number of expected players
     *
     * @return the number of expected players
     */
    public int getExpectedPlayers() {
        return expectedPlayers;
    }

    /**
     * Retrieves the ArrayList of PlayerData objects
     * that match the list of players in the game.
     *
     * @return the ArrayList of PlayerData object
     */
    public ArrayList<PlayerData> getPlayers() {
        return players;
    }

    /**
     * Retrieves the game identifier
     *
     * @return the game identifier
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Retrieves the game's scoreboard
     *
     * @return the game's scoreboard
     */
    public ScoreBoard getScoreBoard() {
        return scoreBoard;
    }

    /**
     * Retrieves the game's public goals
     *
     * @return the game's public goals
     */
    public Goal[] getPublicGoals() {
        return publicGoals;
    }

    /**
     * Retrieves the game's golden cards deck
     *
     * @return the game's golden cards deck
     */
    public Deck<PlayCard> getGoldCardsDeck() {
        return goldCardsDeck;
    }

    /**
     * Retrieves the game's resource cards deck
     *
     * @return the game's resource cards deck
     */
    public Deck<PlayCard> getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    /**
     * Retrieves the game's visible cards.
     *
     * @return the game's visible cards
     */
    public PlayCard[] getVisibleCards() {
        return visibleCards;
    }

    /**
     * Retrieves the game's goals deck
     *
     * @return the gams's goal deck
     */
    public Deck<Goal> getGoalsDeck() {
        return goalsDeck;
    }

    /**
     * Retrieves the game's starting cards deck
     *
     * @return the game's starting cards deck
     */
    public Deck<StartCard> getStartCardsDeck() {
        return startCardsDeck;
    }

    /**
     * Retrieves the game's current turn number
     *
     * @return the game's current turn number
     */
    public int getCurrentTurn() {
        return currentTurn;
    }
}
