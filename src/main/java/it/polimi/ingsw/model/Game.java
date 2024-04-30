package it.polimi.ingsw.model;

import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.decks.Deck;
import it.polimi.ingsw.model.decks.DeckLoader;
import it.polimi.ingsw.model.events.Observable;
import it.polimi.ingsw.model.events.messages.server.PublicGoalsUpdateMessage;
import it.polimi.ingsw.model.events.messages.server.VisibleCardUpdateMessage;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;

import java.io.IOException;
import java.util.*;

/**
 * The Game class represents a game session of the Codex Naturalis game
 */
public class Game extends Observable {
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
    private ScoreBoard scoreBoard;
    private final int idGame;
    private final int expectedPlayers;
    private final Set<PlayerColor> availableColor;

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
        this.goldCardsDeck.setDeckIdentifier(1);
        this.resourceCardsDeck = resourceCardDeckLoader.getDeck();
        this.resourceCardsDeck.setDeckIdentifier(0);

        this.startCardsDeck = startCardDeckLoader.getDeck();
        this.goalsDeck = goalDeckLoader.getDeck();

        this.players = new ArrayList<>();
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
    public void shufflePlayers() throws Exception {
        if (this.isStarted) throw new Exception("Game has already started");
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

        this.notifyObservers(new PublicGoalsUpdateMessage(commonGoals));

        // Draws the visible cards
        this.visibleCards = new PlayCard[]{
                this.resourceCardsDeck.draw(),
                this.resourceCardsDeck.draw(),
                this.goldCardsDeck.draw(),
                this.goldCardsDeck.draw()
        };

        for(int i = 0; i < visibleCards.length; i++) {
            this.notifyObservers(new VisibleCardUpdateMessage(visibleCards[i], i));
        }


        for(Player player : this.players) {
            // draws the player's private startcard
            player.setStartCard(this.startCardsDeck.draw());

            // draws the player 2 resource cards and 1 gold card
            player.setPlayerCard(this.resourceCardsDeck.draw(), 0);
            player.setPlayerCard(this.resourceCardsDeck.draw(), 1);
            player.setPlayerCard(this.goldCardsDeck.draw(), 2);

            Goal[] availableGoals = new Goal[]{
                    this.goalsDeck.draw(),
                    this.goalsDeck.draw()
            };

            player.setAvailableGoals(availableGoals);

        }

        // initialize the scoreboard with the current set of players
        this.scoreBoard = new ScoreBoard(players);

        this.currentTurn = 0;
        this.isFinal = false;
        this.isStarted = true;
    }

    /**
     * Retrieves the players that are playing the game (or that are waiting to play the game)
     *
     * @return a list of Player objects that represents the players in the game.
     */
    public List<Player> getPlayers(){
        return Collections.unmodifiableList(players);
    }

    /**
     * Retrieves the number of players that the game is expecting.
     *
     * @return the number of players that the game is expecting.
     */
    public int getExpectedPlayers(){return this.expectedPlayers;}

    /**
     * Retrieves the available player colors for this game.
     *
     * @return a set of PlayerColor that represents the set of available colors.
     */
    public Set<PlayerColor> getAvailableColor(){ return this.availableColor;}

    /**
     * Retrieves the game scoreboard.
     *
     * @return a reference to the scoreboard that contains player's score for this game.
     */
    public ScoreBoard getScoreBoard(){return this.scoreBoard;}

    /**
     * Retrieves the Resource cards deck.
     *
     * @return a reference to the resource cards deck.
     */
    public Deck<PlayCard> getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    /**
     * Retrieves the Gold cards deck.
     *
     * @return a reference to the gold cards deck.
     */
    public Deck<PlayCard> getGoldCardsDeck() {
        return goldCardsDeck;
    }

    /**
     * Retrieves the visible cards.
     *
     * @return an array of PlayCard that represents the visible cards that can be picked up.
     */
    public PlayCard[] getVisibleCards() {
        return visibleCards;
    }

    /**
     * Tries to fill the empty visible cards slots.
     * First, it tries to fill each slots with its expected card type.
     * It one of the decks runs out of cards, the visible cards slots that
     * expect this kind of card can be filled with cards of the opposite type.
     * If both decks are empty, the visible card slots will be left empty.
     * (the game is bound to end shortly after in this case).
     */
    public void refillVisibleCards() {
        // first i try to fill the empty slots with the preferred card type
        for(int i = 0; i < visibleCards.length; i++) {
            if(visibleCards[i] == null) {
                if(i < 2) {
                    // resource card preferred
                    if(!this.resourceCardsDeck.isEmpty()) {
                        visibleCards[i] = resourceCardsDeck.draw();
                        this.notifyObservers(new VisibleCardUpdateMessage(visibleCards[i], i));
                    }
                }
                else {
                    // gold card
                    if(!this.goldCardsDeck.isEmpty()) {
                        visibleCards[i] = goldCardsDeck.draw();
                        this.notifyObservers(new VisibleCardUpdateMessage(visibleCards[i], i));
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
                        this.notifyObservers(new VisibleCardUpdateMessage(visibleCards[i], i));
                    }
                }
                else {
                    // gold card
                    if(!this.resourceCardsDeck.isEmpty()) {
                        visibleCards[i] = resourceCardsDeck.draw();
                        this.notifyObservers(new VisibleCardUpdateMessage(visibleCards[i], i));
                    }
                }
            }
        }
    }

    /**
     * Checks whether a new turn is starting.
     *
     * @return {@code true} if the player that is expected to player is the first player of the list, {@code false} otherwise.
     */
    public boolean isFirstPlayersTurn() {
        return currentTurn == 0;
    }

    /**
     * Retrieves the game's Identifier.
     *
     * @return the GameID for the current game.
     */
    public int getIdGame() {return this.idGame;}

    /**
     * Checks whether both decks are empty.
     *
     * @return {@code true} if both decks are empty, {@code false} otherwise.
     */
    public boolean emptyDecks(){ return (this.goldCardsDeck.isEmpty()&& this.resourceCardsDeck.isEmpty());}

    /**
     * Subscribes players to the decks and to the game itself.
     */
    public void subscribeCommonObservers() {
        for(Player p : this.players) {
            this.subscribe(p.getClientHandler());
            this.goldCardsDeck.subscribe((p.getClientHandler()));
            this.resourceCardsDeck.subscribe((p.getClientHandler()));
        }
    }
}
