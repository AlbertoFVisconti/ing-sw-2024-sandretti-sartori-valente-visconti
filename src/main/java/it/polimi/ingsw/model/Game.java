package it.polimi.ingsw.model;

import it.polimi.ingsw.model.decks.PlayCardDeck;
import it.polimi.ingsw.model.decks.StartCardDeck;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.decks.GoalDeck;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.GameStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Game class represents a game session of the Codex Naturalis game
 */
public class Game {
    private int currentTurn;
    private final List<Player> players;

    private boolean isFinal;
    private boolean isStarted;

    private Goal[] commonGoals;

    private final PlayCardDeck goldCardsDeck;
    private final PlayCardDeck resourceCardsDeck;
    private final StartCardDeck startCardsDeck;
    private final GoalDeck goalsDeck;
    private GameStatus currStatus;
    private ScoreBoard scoreBoard;

    /**
     * Constructs a new Game object, initializing the game components.
     * In particular, the Decks' content is loaded from the provided files.
     *
     * @param goldCardFileName name of the file that contains the gold cards' data
     * @param resourceCardFileName name of the file that contains the resource cards' data
     * @param startCardFileName name of the file that contains the start cards' data
     * @param goalFileName name of the file that contains the goals' data
     * @throws IOException if there's a problem reading one of the files
     */
    public Game(String goldCardFileName, String resourceCardFileName, String startCardFileName, String goalFileName) throws IOException {
        this.goldCardsDeck = new PlayCardDeck(goldCardFileName);
        this.resourceCardsDeck = new PlayCardDeck(resourceCardFileName);
        this.startCardsDeck = new StartCardDeck(startCardFileName);
        this.goalsDeck = new GoalDeck(goalFileName);
        this.players = new ArrayList<>();
        this.currStatus=GameStatus.LOBBY;
    }

    /**
     * Adds a player to the game.
     * A player can only be added if the game is not started yet.
     *
     * @param player Player to add to the game.
     * @throws Exception if the game's already started.
     */
    public void addPlayer(Player player) throws Exception {
        if(isStarted) throw new Exception();
        players.add(player);
    }

    /**
     * Retrieves the player who needs to play now.
     *
     * @return Player whose turn it currently is.
     */
    public Player getTurn() {
        return players.get(currentTurn);
    }

    /**
     * Advances the game to the next turn
     *
     * @throws Exception if the game has not started yet.
     */
    public void nextTurn() throws Exception {
        if(!isStarted) throw new Exception();
        currentTurn = (currentTurn+1)%players.size();
    }

    /**
     * Sets the game to the final round
     *
     * @throws Exception if the game has not started yet.
     */
    public void setFinalRound() throws Exception {
        if(!isStarted) throw new Exception();
        this.isFinal = true;
    }

    /**
     * Checks if the game is in the final round.
     *
     * @return {@code true} if the current round is final, {@code false} otherwise.
     */
    public boolean isFinalRound() {
        return isFinal;
    }

    /**
     * Shuffles the players' turn order.
     *
     * @throws Exception if the game has already started
     */
    public void shufflePlayers() throws Exception {
        if(isStarted) throw new Exception();
        Collections.shuffle(this.players);
    }

    /**
     * Retrieves the common goals for the current game.
     *
     * @return An array containing the common goals for the game.
     */
    public Goal[] getCommonGoals() {
        return this.commonGoals.clone();
    }

    /**
     * Starts the game by drawing and distributing cards.
     * Once this method is called, it becomes impossible to edit the game's info.
     */
    public void startGame() {
        if(isStarted) return;

        // Draws the common goals
        this.commonGoals = new Goal[]{
                this.goalsDeck.draw(),
                this.goalsDeck.draw()
        };

        for(Player player : this.players) {
            // draws the player's private goal and startcard
            player.setPrivateGoal(this.goalsDeck.draw());
            player.setStartCard(this.startCardsDeck.draw());

            // draws the player 2 resource cards and 1 gold card
            player.setPlayerCard(this.resourceCardsDeck.draw(), 0);
            player.setPlayerCard(this.resourceCardsDeck.draw(), 1);
            player.setPlayerCard(this.goldCardsDeck.draw(), 2);
        }

        // initialize the scoreboard with the current set of players
        this.scoreBoard = new ScoreBoard(players);

        this.currentTurn = 0;
        this.isFinal = false;
        this.isStarted = true;
    }

    /**
     * Checks if the game has started.
     *
     * @return {@code true} if the game has started, {@code false} otherwise.
     */
    public boolean isGameStarted() {
        return this.isStarted;
    }

    public List<Player> getPlayers(){
        return Collections.unmodifiableList(players);
    }
    public GameStatus getCurrStatus(){
        return currStatus;
    }
    public void setCurrStatus(GameStatus gameStatus){
        currStatus=gameStatus;
    }
}
