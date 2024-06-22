package it.polimi.ingsw.model;

import it.polimi.ingsw.events.Observable;
import it.polimi.ingsw.events.messages.server.PlayersListUpdateMessage;
import it.polimi.ingsw.events.messages.server.CommonGoalsUpdateMessage;
import it.polimi.ingsw.events.messages.server.VisibleCardUpdateMessage;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.cards.StartCard;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.decks.Deck;
import it.polimi.ingsw.model.decks.DeckLoader;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.saving.*;
import it.polimi.ingsw.network.cliendhandlers.ClientHandler;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The Game class represents a game session of the Codex Naturalis game
 */
public class Game extends Observable {
    private int currentTurn = 0;
    private final List<Player> players;

    private boolean isFinal;
    private boolean isStarted;

    private Goal[] commonGoals;

    private Deck<PlayCard> goldCardsDeck;
    private Deck<PlayCard> resourceCardsDeck;

    private PlayCard[] visibleCards;

    private Deck<StartCard> startCardsDeck;
    private Deck<Goal> goalsDeck;
    private ScoreBoard scoreBoard;
    private final String idGame;
    private final int expectedPlayers;
    private final Set<PlayerColor> availableColor;

    private final Chat chat;

    /**
     * Constructs a new Game object, initializing the game components.
     * In particular, the Decks' content is loaded from the provided files.
     *
     * @param goldCardDeckLoader     DeckLoader that loads the game's Gold Cards
     * @param resourceCardDeckLoader DeckLoader that loads the game's Resource Cards
     * @param startCardDeckLoader    DeckLoader that loads the game's Start Cards
     * @param goalDeckLoader         DeckLoader that loads the game's Goals
     * @param idGame                 the identifier of the game that players can use to rejoin a specific game
     * @throws IOException if there's a problem reading one of the files
     */
    public Game(DeckLoader<PlayCard> goldCardDeckLoader, DeckLoader<PlayCard> resourceCardDeckLoader,
                DeckLoader<StartCard> startCardDeckLoader, DeckLoader<Goal> goalDeckLoader, String idGame, int expectedPlayers) throws IOException {
        if (goldCardDeckLoader != null) {
            this.goldCardsDeck = goldCardDeckLoader.getDeck();
            this.goldCardsDeck.setDeckIdentifier(1);
        } else {
            this.goldCardsDeck = null;
        }
        if (resourceCardDeckLoader != null) {
            this.resourceCardsDeck = resourceCardDeckLoader.getDeck();
            this.resourceCardsDeck.setDeckIdentifier(0);
        } else {
            this.resourceCardsDeck = null;
        }

        if (startCardDeckLoader != null)
            this.startCardsDeck = startCardDeckLoader.getDeck();
        else
            this.startCardsDeck = null;
        if (goalDeckLoader != null)
            this.goalsDeck = goalDeckLoader.getDeck();
        else
            this.goalsDeck = null;

        this.players = new ArrayList<>();
        this.idGame = idGame;
        this.expectedPlayers = expectedPlayers;
        this.availableColor = new HashSet<>();
        this.availableColor.addAll(Arrays.asList(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.YELLOW));

        scoreBoard = new ScoreBoard();

        this.chat = new Chat();
    }



    /**
     * Constructs a new Game object from existing data
     *
     * @param gsm game data
     */
    public Game(GameSaving gsm) {
        players = new ArrayList<>();
        this.availableColor = new HashSet<>();
        this.availableColor.addAll(Arrays.asList(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.YELLOW));
        for (PlayerSaving psm : gsm.getPlayers()) {
            Player p = new Player(psm);
            players.add(p);
            availableColor.remove(p.getColor());
        }
        this.expectedPlayers = gsm.getExpectedPlayers();
        this.idGame = gsm.getGameId();
        this.scoreBoard = gsm.getScoreBoard();
        this.commonGoals = gsm.getPublicGoal();
        this.goldCardsDeck = gsm.getGoldCardsDeck();
        this.resourceCardsDeck = gsm.getResourceCardsDeck();
        this.startCardsDeck = gsm.getStartCardsDeck();
        this.goalsDeck = gsm.getGoalsDeck();
        this.visibleCards = gsm.getVisibleCards();
        this.currentTurn = gsm.getCurrentTurn();

        this.chat = new Chat();
    }

    /**
     * Retrieves the Game's data in a format that can be loaded by another Game object.
     *
     * @return GameSaving object containing the Game's data
     */
    public GameSaving getSaving() {
        ArrayList<PlayerSaving> playerSavings = new ArrayList<>();
        for (Player p : this.players) {
            playerSavings.add(p.getSaving());
        }

        return new GameSaving(expectedPlayers, playerSavings, idGame, this.goldCardsDeck, this.resourceCardsDeck, visibleCards, scoreBoard, commonGoals, startCardsDeck, goalsDeck, this.currentTurn);
    }

    /**
     * Retrieves the Game's data in a format that can be loaded by another Game in the Client's model.
     * Requires a nickname in order to only put "secret" information if the data is going to be sent to a player
     * that is authorized to receive such information.
     *
     * @param clientNickname the nickname of the client that will receive the requested data
     * @return ClientGameSaving object containing the Game's data that the receiving player is authorized to receive.
     */
    public ClientGameSaving getClientSaving(String clientNickname) {
        ArrayList<ClientPlayerSaving> playerSavings = new ArrayList<>();
        for (Player p : this.players) {
            playerSavings.add(p.getClientSaving(clientNickname));
        }

        return new ClientGameSaving(playerSavings, idGame, this.goldCardsDeck.getTopOfTheStack(), this.resourceCardsDeck.getTopOfTheStack(), visibleCards, scoreBoard, commonGoals);
    }

    /**
     * Update the availableColors set according to the updated players list
     */
    public void updateAvailableColors() {
        this.availableColor.addAll(Arrays.asList(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.YELLOW));

        for (Player p : this.players) {
            availableColor.remove(p.getColor());
        }

        notifyPlayersListUpdate();
    }

    /**
     * Notify all subscribers (usually players) that the players list has been updated.
     */
    private void notifyPlayersListUpdate() {
        String[] nicknames = new String[players.size()];
        PlayerColor[] colors = new PlayerColor[players.size()];

        for (int i = 0; i < players.size(); i++) {
            nicknames[i] = players.get(i).nickname;
            colors[i] = players.get(i).getColor();
        }

        notifyObservers(new PlayersListUpdateMessage(nicknames, colors));
    }

    public String getBackupKey() {
        String[] nicknames = new String[players.size()];

        for (int i = 0; i < players.size(); i++) {
            nicknames[i] = players.get(i).nickname;
        }

        String s = Arrays.stream(nicknames).sorted().collect(Collectors.joining("\n"));

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        md.update(s.getBytes());
        byte[] digest = md.digest();

        return String.format("%032X", new BigInteger(1, digest));
    }

    /**
     * Empties the players list
     */
    public void resetPlayers() {
        if (isStarted) throw new RuntimeException();
        this.players.clear();
    }

    /**
     * Adds a player to the game.
     * A player can only be added if the game is not started yet.
     *
     * @param player Player to add to the game.
     * @throws RuntimeException if the game's already started.
     */
    public void addPlayer(Player player) {
        if (isStarted) throw new RuntimeException("Cannot add a new player to a started game");

        if (this.expectedPlayers != -1 && players.size() >= this.expectedPlayers)
            throw new RuntimeException("Game's full");

        players.add(player);
        availableColor.remove(player.getColor());

        this.subscribe(player.getClientHandler());

        notifyPlayersListUpdate();
    }

    /**
     * Removes one of the players from the players list.
     * Won't work if the game has already been started.
     *
     * @param player the Player object that needs to be removed from the game.
     */
    public void removePlayer(Player player) {
        if (isStarted) throw new RuntimeException();
        this.players.remove(player);

        updateAvailableColors();
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
     * @throws RuntimeException if the game has not started yet.
     */
    public void nextTurn() {
        if (!isStarted) throw new RuntimeException("The game has not been started yet");
        currentTurn = (currentTurn + 1) % players.size();
    }

    /**
     * Sets the game to the final round
     *
     * @throws RuntimeException if the game has not started yet.
     */
    public void setFinalRound() {
        if (!isStarted) throw new RuntimeException("the game has not been started yet");
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
     * @throws RuntimeException if the game has already started
     */
    public void shufflePlayers() {
        if (this.isStarted) throw new RuntimeException("Game has already started");
        Collections.shuffle(this.players);

        notifyPlayersListUpdate();
    }

    /**
     * Retrieves the game's Chat object.
     *
     * @return Game's Chat object.
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * Allows to set the game's common goals.
     * Won't work if the game has already been started.
     *
     * @param goals the array containing the common goals
     */
    public void setCommonGoals(Goal[] goals) {
        if (this.isStarted) throw new RuntimeException("Game has already started");
        this.commonGoals = goals.clone();

        this.notifyObservers(new CommonGoalsUpdateMessage(commonGoals));
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
     * Create the ScoreBoard for the game with the players currently connected.
     * This method shouldn't be used on the server, since it is intended for the client to create a
     * "default" scoreboard before it receive an updated one from the server.
     */
    public void setupScoreBoard() {
        if (isStarted) throw new UnsupportedOperationException("setupScoreBoard shouldn't be used after a game starts");
        this.scoreBoard = new ScoreBoard(players);
    }

    public void loadGame(GameSaving gameSaving) {
        if (isStarted) throw new UnsupportedOperationException("cannot load game data on a started game");

        ArrayList<PlayerSaving> playerListBackup = gameSaving.getPlayers();

        if(this.players.size() != playerListBackup.size()) throw new RuntimeException("players lists incompatible");

        for(int i = 0; i < this.players.size(); i++) {
            for(int j = 0; j < this.players.size(); j++) {
                if(this.players.get(j).nickname.equals(playerListBackup.get(i).getNick())) {
                    Collections.swap(this.players, i,j);
                    break;
                }
            }

            if(!this.players.get(i).nickname.equals(playerListBackup.get(i).getNick())) throw new RuntimeException("players list incompatible");
            this.players.get(i).loadPlayer(playerListBackup.get(i));
        }

        this.availableColor.addAll(Arrays.asList(PlayerColor.BLUE, PlayerColor.GREEN, PlayerColor.RED, PlayerColor.YELLOW));

        for (Player p : this.players) {
            availableColor.remove(p.getColor());
        }

        this.scoreBoard = gameSaving.getScoreBoard();

        this.commonGoals = gameSaving.getPublicGoal();

        this.goldCardsDeck = gameSaving.getGoldCardsDeck();

        this.resourceCardsDeck = gameSaving.getResourceCardsDeck();
        this.startCardsDeck = gameSaving.getStartCardsDeck();
        this.goalsDeck = gameSaving.getGoalsDeck();
        this.visibleCards = gameSaving.getVisibleCards();
        this.currentTurn = gameSaving.getCurrentTurn();

        this.isStarted = true;

        this.subscribeCommonObservers();
    }

    /**
     * Starts the game by drawing and distributing cards.
     * Once this method is called, it becomes impossible to edit the game's info.
     */
    public void startGame() {
        if (isStarted) return;

        this.scoreBoard = new ScoreBoard(players);
        this.subscribeCommonObservers();

        // Draws the common goals
        this.setCommonGoals(new Goal[]{
                this.goalsDeck.draw(),
                this.goalsDeck.draw()
        });

        // Draws the visible cards
        this.visibleCards = new PlayCard[4];
        refillVisibleCards();


        for (Player player : this.players) {
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

        this.currentTurn = 0;
        this.isFinal = false;
        this.isStarted = true;
    }

    /**
     * Retrieves the players that are playing the game (or that are waiting to play the game)
     *
     * @return a list of Player objects that represents the players in the game.
     */
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Retrieves the number of players that the game is expecting.
     *
     * @return the number of players that the game is expecting.
     */
    public int getExpectedPlayers() {
        return this.expectedPlayers;
    }

    /**
     * Retrieves the available player colors for this game.
     *
     * @return a set of PlayerColor that represents the set of available colors.
     */
    public Set<PlayerColor> getAvailableColor() {
        return this.availableColor;
    }

    /**
     * Retrieves the game scoreboard.
     *
     * @return a reference to the scoreboard that contains player's score for this game.
     */
    public ScoreBoard getScoreBoard() {
        return this.scoreBoard;
    }

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
     * Empties the visible cards and recreate a new (empty) array
     * with a specified length.
     * <p>
     * Used on the client's View.
     *
     * @param length the length of the new visible cards array.
     */
    public void resetVisibleCards(int length) {
        if (this.isStarted) throw new RuntimeException("Game has already started");

        this.visibleCards = new PlayCard[length];
        for (int i = 0; i < length; i++) visibleCards[i] = null;
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
        for (int i = 0; i < visibleCards.length; i++) {
            if (visibleCards[i] == null) {
                if (i < 2) {
                    // resource card preferred
                    if (!this.resourceCardsDeck.isEmpty()) {
                        visibleCards[i] = resourceCardsDeck.draw();
                        this.notifyObservers(new VisibleCardUpdateMessage(visibleCards[i], i));
                    }
                } else {
                    // gold card
                    if (!this.goldCardsDeck.isEmpty()) {
                        visibleCards[i] = goldCardsDeck.draw();
                        this.notifyObservers(new VisibleCardUpdateMessage(visibleCards[i], i));
                    }
                }
            }
        }

        // in the second run i try to fill the empty slots with whichever type of card is available
        for (int i = 0; i < visibleCards.length; i++) {
            if (visibleCards[i] == null) {
                if (i < 2) {
                    // resource card preferred
                    if (!this.goldCardsDeck.isEmpty()) {
                        visibleCards[i] = goldCardsDeck.draw();
                        this.notifyObservers(new VisibleCardUpdateMessage(visibleCards[i], i));
                    }
                } else {
                    // gold card
                    if (!this.resourceCardsDeck.isEmpty()) {
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
    public String getIdGame() {
        return this.idGame;
    }

    /**
     * Checks whether both decks are empty.
     *
     * @return {@code true} if both decks are empty, {@code false} otherwise.
     */
    public boolean emptyDecks() {
        return (this.goldCardsDeck.isEmpty() && this.resourceCardsDeck.isEmpty());
    }

    /**
     * Subscribes players to the decks and to the game itself.
     */
    public void subscribeCommonObservers() {
        for (Player p1 : this.players) {
            for (Player p2 : this.players) {
                p1.subscribe(p2.getClientHandler());
            }

            this.goldCardsDeck.subscribe((p1.getClientHandler()));
            this.resourceCardsDeck.subscribe((p1.getClientHandler()));
            this.scoreBoard.subscribe(p1.getClientHandler());
        }
    }

    /**
     * Subscribes a certain player to the common observers
     *
     * @param clientHandler the player that needs to be subscribed.
     */
    public void subscribeCommonObservers(ClientHandler clientHandler) {
        for (Player p1 : this.players) {
            p1.subscribe(clientHandler);
        }

        this.goldCardsDeck.subscribe(clientHandler);
        this.resourceCardsDeck.subscribe(clientHandler);
        this.scoreBoard.subscribe(clientHandler);
    }

    /**
     * Unsubscribes a certain player from the common observers
     *
     * @param clientHandler the player that needs to be unsubscribed.
     */
    public void unsubscribeFromCommonObservable(ClientHandler clientHandler) {
        for (Player p1 : this.players) {
            p1.unsubscribe(clientHandler);
        }
        this.goldCardsDeck.unsubscribe(clientHandler);
        this.resourceCardsDeck.unsubscribe(clientHandler);
        this.scoreBoard.unsubscribe(clientHandler);

    }

    /**
     * Retrieves the StartCard deck
     *
     * @return the StartCard Deck
     */
    public Deck<StartCard> getStartCardsDeck() {
        return startCardsDeck;
    }

    /**
     * Retrieves the Goal Deck
     *
     * @return the Goal Deck
     */
    public Deck<Goal> getGoalsDeck() {
        return goalsDeck;
    }


}
