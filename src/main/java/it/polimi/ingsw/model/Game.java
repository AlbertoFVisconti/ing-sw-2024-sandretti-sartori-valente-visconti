package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameStatus;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.decks.Deck;
import it.polimi.ingsw.model.decks.DeckLoader;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

import java.io.IOException;
import java.util.*;

/**
 * The Game class represents a game session of the Codex Naturalis game
 */
public class Game {
    private int currentTurn;
    private final List<Player> players;

    private boolean isFinal;
    private boolean isStarted;

    private Goal[] commonGoals;

    private final Deck<PlayCard> goldCardsDeck;
    private final Deck<PlayCard> resourceCardsDeck;

    private PlayCard[] visibleCards;

    private final Deck<StartCard> startCardsDeck;
    private final Deck<Goal> goalsDeck;
    private GameStatus currStatus;
    private ScoreBoard scoreBoard;
    private final int idGame;
    private final int expectedPlayers;
    private Set<PlayerColor> availableColor;


    /**
     * Constructs a new Game object, initializing the game components.
     * In particular, the Decks' content is loaded from the provided files.
     *
     * @param goldCardDeckLoader DeckLoader that loads the game's Gold Cards
     * @param resourceCardDeckLoader DeckLoader that loads the game's Resource Cards
     * @param startCardDeckLoader DeckLoader that loads the game's Start Cards
     * @param goalDeckLoader DeckLoader that loads the game's Goals
     * @param idGame the Id of the game that players can use to rejoin a specific game
     * @throws IOException if there's a problem reading one of the files
     */
    public Game(DeckLoader<PlayCard> goldCardDeckLoader, DeckLoader<PlayCard> resourceCardDeckLoader,
                DeckLoader<StartCard> startCardDeckLoader, DeckLoader<Goal> goalDeckLoader, int idGame, int expectedPlayers) throws IOException {
        this.goldCardsDeck = goldCardDeckLoader.getDeck();
        this.resourceCardsDeck = resourceCardDeckLoader.getDeck();

        this.startCardsDeck = startCardDeckLoader.getDeck();
        this.goalsDeck = goalDeckLoader.getDeck();

        this.players = new ArrayList<>();
        this.currStatus=GameStatus.LOBBY;
        this.idGame=idGame;
        this.expectedPlayers=expectedPlayers;
        this.availableColor= new HashSet<>();
        this.availableColor.addAll(Arrays.asList(PlayerColor.BLUE,PlayerColor.GREEN,PlayerColor.RED,PlayerColor.YELLOW));
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
        availableColor.remove(player.color);
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
    public void shufflePlayers(){
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

        // Draws the visible cards
        this.visibleCards = new PlayCard[]{
                this.resourceCardsDeck.draw(),
                this.resourceCardsDeck.draw(),
                this.goldCardsDeck.draw(),
                this.goldCardsDeck.draw()
        };


        for(Player player : this.players) {
            // draws the player's private startcard
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

    public List<Player> getPlayers(){
        return Collections.unmodifiableList(players);
    }
    public int getExpectedPlayers(){return this.expectedPlayers;}
    public Set<PlayerColor> getAvailableColor(){ return this.availableColor;}
    public Goal getGoal(){return  this.goalsDeck.draw();}
    public ScoreBoard getScoreBoard(){return this.scoreBoard;}

    public Deck<PlayCard> getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    public Deck<PlayCard> getGoldCardsDeck() {
        return goldCardsDeck;
    }

    public PlayCard[] getVisibleCards() {
        return visibleCards;
    }

    public void refillVisibleCards() {
        // first i try to fill the empty slots with the preferred card type
        for(int i = 0; i < visibleCards.length; i++) {
            if(visibleCards[i] == null) {
                if(i < 2) {
                    // resource card preferred
                    if(!this.resourceCardsDeck.isEmpty()) {
                        visibleCards[i] = resourceCardsDeck.draw();
                    }
                }
                else {
                    // gold card
                    if(!this.goldCardsDeck.isEmpty()) {
                        visibleCards[i] = goldCardsDeck.draw();
                    }
                }
            }
        }

        // in the second run i try to fill the empty slots with whichever type of card is available
        for(int i = 0; i < visibleCards.length; i++) {
            if(visibleCards[i] == null) {
                if(i < 2) {
                    // resource card preferred
                    if(!this.goldCardsDeck.isEmpty()) {
                        visibleCards[i] = goldCardsDeck.draw();
                    }
                }
                else {
                    // gold card
                    if(!this.resourceCardsDeck.isEmpty()) {
                        visibleCards[i] = resourceCardsDeck.draw();
                    }
                }
            }
        }
    }

    public boolean isFirstPlayersTurn() {
        return currentTurn == 0;
    }
    public int getIdGame() {return this.idGame;}
    public boolean emptyDecks(){ return (this.goldCardsDeck.isEmpty()&& this.resourceCardsDeck.isEmpty());}
}
