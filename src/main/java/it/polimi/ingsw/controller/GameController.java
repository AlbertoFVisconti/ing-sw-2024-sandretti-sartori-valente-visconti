package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.Observable;
import it.polimi.ingsw.events.messages.client.ClientMessage;
import it.polimi.ingsw.events.messages.server.GameStatusUpdateMessage;
import it.polimi.ingsw.events.messages.server.JoinConfirmationMessage;
import it.polimi.ingsw.events.messages.server.ServerErrorMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.ScoreBoard;
import it.polimi.ingsw.model.cards.PlayCard;
import it.polimi.ingsw.model.decks.Deck;
import it.polimi.ingsw.model.goals.Goal;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.model.saving.ClientGameData;
import it.polimi.ingsw.model.saving.GameBackup;
import it.polimi.ingsw.model.saving.GameData;
import it.polimi.ingsw.network.cliendhandlers.ClientHandler;
import it.polimi.ingsw.network.rmi.GameControllerWrapper;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.utils.GameBackupManager;
import javafx.scene.paint.Color;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * GameController allows players to play a game.
 * It handles all the game logic and checks that the player is performing legal operations.
 **/
public class GameController extends Observable implements VirtualController, Runnable {
    // if this number of player isn't currently connected, the game will be paused
    private static final int MINIMUM_PLAYER_TO_RUN_GAME = 2;

    // the number of player that is currently online
    private int connectedPlayers;
    private boolean paused;


    // the game object that contains the data of the controlled game
    private final Game game;

    // the current game and turn status
    private GameStatus gameStatus;
    private TurnStatus turnStatus;


    // the queue that contains all messages that the controller needs to process
    private final BlockingQueue<ClientMessage> messageQueue;

    private String backupKey;

    private boolean newRoundStartedFlag = false;

    /**
     * Constructs a GameController that handles the provided game.
     * <p>
     * By default, the game will be in LOBBY status.
     *
     * @param game the game you want to be controlled
     **/
    public GameController(Game game) {
        System.err.println("GameController created");
        this.paused = false;
        this.game = game;
        turnStatus = null;
        gameStatus = GameStatus.LOBBY;
        this.messageQueue = new ArrayBlockingQueue<>(100);
        new Thread(this).start();
    }

    /**
     * Load a game back so that the current GameController starts handling the loaded game.
     *
     * @param gameBackup GameBackup that contains the game's data and game's status
     */
    public synchronized void loadBackup(GameBackup gameBackup) {
        this.gameStatus = gameBackup.gameStatus();
        this.turnStatus = gameBackup.turnStatus();
        this.game.loadGame(gameBackup.gameData());
    }

    /**
     * Retrieves the game's backup.
     *
     * @return GameBackup object that describe the game's data and the game's state
     */
    public synchronized GameBackup getBackup() {
        return new GameBackup(this.game.getSaving(), this.gameStatus, this.turnStatus);
    }

    /**
     * Allows to save this game content to file (if the GameBackupManager is enabled)
     */
    private void saveGameBackup() {
        if(this.gameStatus == GameStatus.END) return;
        GameBackupManager.saveGame(backupKey, this.getBackup());
    }


    /**
     * Add a new player to the game.
     *
     * @param nickname              the nickname of the joining player
     * @param clientHandler         the client handler of the joining player
     * @param gameControllerWrapper the remote object that wraps the GameController
     * @throws UnsupportedOperationException if the game is already started
     */
    public synchronized void addNewPlayer(String nickname, ClientHandler clientHandler, GameControllerWrapper gameControllerWrapper) {
        if (this.gameStatus != GameStatus.LOBBY) {
            throw new UnsupportedOperationException("Cannot add a new player to a running game");
        }
        Player newPlayer = new Player(nickname, clientHandler);
        game.addPlayer(newPlayer);

        clientHandler.setController(gameControllerWrapper);

        this.game.getChat().subscribe(clientHandler);

        this.game.getChat().sendMessage(newPlayer, null, "has joined the game", Color.DARKGREEN);

        clientHandler.sendMessage(new JoinConfirmationMessage(nickname));

        this.subscribe(clientHandler);
        updateStatus();
    }

    /**
     * Retrieves the current game status.
     *
     * @return the current game status.
     */
    public synchronized GameStatus getGameStatus() {
        return gameStatus;
    }

    /**
     * Retrieves the current turn status.
     *
     * @return the current turn status.
     */
    public synchronized TurnStatus getTurnStatus() {
        return turnStatus;
    }

    /**
     * Put a message for this Game in a blocking queue.
     * Messages are elaborated asynchronously on another thread.
     *
     * @param message the message that needs to be processed by this controller.
     */
    public synchronized void forwardMessage(ClientMessage message) {
        try {
            this.messageQueue.put(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Allows to let the game controller know that a player has disconnected.
     * If the player was actually playing this game, the game controller will handle
     * the disconnection. If the current number of online players
     * is below the threshold, the game will be paused.
     *
     * @param clientHandler the ClientHandler of the disconnected player
     */
    public synchronized void handleDisconnection(ClientHandler clientHandler) {
        Player player = this.helperGetPlayerByPlayerIdentifier(clientHandler.getPlayerIdentifier());

        if (player != null && this.game.getPlayers().contains(player)) {
            clientHandler.resetController();
            boolean isCurrentPlayer = false;

            System.err.println(player.nickname + " has disconnected");

            this.unsubscribe(clientHandler);
            this.game.unsubscribe(clientHandler);
            this.game.unsubscribeFromCommonObservable(clientHandler);

            this.game.getChat().unsubscribe(clientHandler);
            this.game.getChat().sendMessage(player, null, "has disconnected", Color.DARKRED);

            if (gameStatus == GameStatus.LOBBY) {
                game.removePlayer(player);
            } else {
                this.connectedPlayers--;
                player.leftTheGame();

                if (this.connectedPlayers < GameController.MINIMUM_PLAYER_TO_RUN_GAME) {
                    this.paused = true;
                }

                if (this.game.getTurn().hasLeft()) isCurrentPlayer = true;

                if(!paused && isCurrentPlayer) {
                    if(turnStatus == TurnStatus.DRAW) {
                        // the player has just placed a card
                        // in order to prevent cheating, the player should receive a random card
                        drawRandomCard(player);
                    }
                    advanceTurn();
                }
            }
        }

        if((this.gameStatus != GameStatus.LOBBY && connectedPlayers == 0)
            || (gameStatus==GameStatus.LOBBY && game.getPlayers().isEmpty())) {
            MainController.getInstance().removeGame(this.game.getIdGame());
            this.gameStatus = GameStatus.DELETED;
        }
    }

    private void drawRandomCard(Player player) {
        ArrayList<Integer> availableCards = new ArrayList<>();
        if(!game.getResourceCardsDeck().isEmpty()) availableCards.add(0);
        if(!game.getGoldCardsDeck().isEmpty()) availableCards.add(1);

        for(int i = 0; i < game.getVisibleCards().length; i++)
            if(game.getVisibleCards()[i] != null) availableCards.add(2+i);

        if(!availableCards.isEmpty()) {
            this.drawCardHelper(player, availableCards.get(new Random().nextInt(availableCards.size())));

        }

    }

    /**
     * Allows to let the game controller know that a player that had previously disconnected,
     * has now rejoined the game. If the player was actually playing this game and if
     * they were actually disconnected, the game controller will handle the reconnection.
     * If the game is currently paused and the number of online players reaches the
     * threshold, the game can be unpaused.
     *
     * @param nickname the nickname of the player who is rejoining
     * @param clientHandler the ClientHandler of the player who is rejoining
     * @param gameControllerWrapper the remote object that contains a reference to the game controller
     * @throws RuntimeException if the reconnection fails (either the player is unknown or the player is already online)
     */
    public synchronized void handleReconnection(String nickname, ClientHandler clientHandler, GameControllerWrapper gameControllerWrapper) {
        Player connectingPlayer = null;
        ClientGameData saving;

        for (Player player : game.getPlayers()) {
            if (player.nickname.equals(nickname)) {
                connectingPlayer = player;
                break;
            }
        }

        if (connectingPlayer == null) {
            throw new RuntimeException("Reconnection failed: unknown player");
        }
        if (!connectingPlayer.hasLeft()) {
            throw new RuntimeException("Reconnection failed: this player is already connected");
        }

        if (gameControllerWrapper != null) clientHandler.setController(gameControllerWrapper);


        System.err.println(nickname + " has rejoined the game");

        connectingPlayer.rejoinTheGame();

        this.game.getChat().subscribe(clientHandler);
        connectingPlayer.setClientHandler(clientHandler);

        this.game.subscribe(clientHandler);
        this.game.subscribeCommonObservers(clientHandler);

        saving = this.game.getClientSaving(nickname);

        this.subscribe(clientHandler);
        clientHandler.sendMessage(new JoinConfirmationMessage(nickname, saving));

        this.game.getChat().sendMessage(connectingPlayer, null, "has re-joined the game", Color.DARKGREEN);

        this.connectedPlayers++;

        if (this.paused && this.connectedPlayers >= GameController.MINIMUM_PLAYER_TO_RUN_GAME) {
            this.paused = false;

            if (game.getTurn().hasLeft()) {
                advanceTurn();
            } else {
                this.updateStatus();
            }
        } else {
            this.updateStatus();
        }
    }

    /**
     * Updates, if needed, the Game status.
     * Allows to start and end games.
     * A game enters the GAME_CREATION status once the number of players matches the
     * number of expected players. In this phase players can asynchronously select their
     * private goal and place their starting card.
     * When everyone has selected their goal and placed their starting card, the game
     * enters the NORMAL_TURN status.
     * When an even trigger the last turn of the game and the current turn ends,
     * the game enters its LAST_TURN status.
     * After the last player placed their last card, the game comes to an end.
     * Thus, the Game is put in END status.
     */
    private void updateStatus() {
        if (gameStatus == GameStatus.LOBBY) {
            if (game.getExpectedPlayers() == game.getPlayers().size() && game.getAvailableColor().size() == (4 - game.getExpectedPlayers())) {

                System.err.println("ExpectedPlayers amount reached, game starts. Connected players:");
                for (Player p : game.getPlayers()) {
                    System.err.println("\t" + p.nickname);
                }

                this.connectedPlayers = game.getPlayers().size();

                this.backupKey = game.getBackupKey();

                GameBackup backup = GameBackupManager.retrieveGame(backupKey);
                if(backup == null) {
                    gameStatus = GameStatus.GAME_CREATION;

                    game.shufflePlayers();

                    game.startGame();

                    this.turnStatus = TurnStatus.PLACE;
                }
                else {
                    this.loadBackup(backup);

                    for(Player p : game.getPlayers()) {
                        GameData gameData = game.getClientSaving(p.nickname);
                        p.getClientHandler().sendMessage(new JoinConfirmationMessage(p.nickname, gameData));
                    }
                }
            }
        } else if (gameStatus == GameStatus.GAME_CREATION) {
            boolean flag = true;
            for (Player p : game.getPlayers()) {
                if (p.getPlacedCardSlot(new CardLocation(0, 0)) == null
                        || p.getPrivateGoal() == null) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                System.err.println("Players placed starting cards and selected goal, first turn starts");
                gameStatus = GameStatus.NORMAL_TURN;
            }
        } else if (newRoundStartedFlag) {
            newRoundStartedFlag = false;

            if (gameStatus == GameStatus.LAST_TURN) {
                GameBackupManager.deleteBackup(this.backupKey);
                System.err.println("GAME FINISHED");
                gameStatus = GameStatus.END;

                this.computeWinner();

            } else if (gameStatus == GameStatus.NORMAL_TURN) {
                boolean flag = true;
                for (Player p : game.getPlayers()) {
                    if (game.getScoreBoard().getScore(p.nickname) >= 20) {
                        System.err.println(p.nickname + "reached 20 points, last turn starts");
                        this.gameStatus = GameStatus.LAST_TURN;
                        flag = false;
                        break;
                    }
                }

                if (flag && game.emptyDecks()) {

                    System.err.println("Decks are empty, last turn starts");
                    this.gameStatus = GameStatus.LAST_TURN;
                }

                if (gameStatus == GameStatus.LAST_TURN) game.setFinalRound();
            }
        }

        System.out.println("update status");

        notifyObservers(new GameStatusUpdateMessage(gameStatus, turnStatus, game.getTurn().nickname));
    }

    /**
     * Compute the game's winners.
     * If there's a single player with the higher score (after goals are evaluated)
     * that's the winner. If there's more than one player with the higher score,
     * the one with the high pre-goal-evaluation score wins.
     * If there's more than one player with the high score more than one of them
     * has the higher pre-goal-evaluation score, they all wins.
     */
    private void computeWinner() {
        if (gameStatus != GameStatus.END) {
            throw new RuntimeException("The game's still running");
        }

        ScoreBoard nOfCompletedGoals = new ScoreBoard(game.getPlayers());
        ScoreBoard scoreBoard = game.getScoreBoard();

        int score;
        for (Player p : game.getPlayers()) {
            for (Goal g : game.getCommonGoals()) {
                // evaluating the common goal
                score = g.evaluate(p);
                nOfCompletedGoals.addScore(p.nickname, score/g.getGoalValue());
                scoreBoard.addScore(p.nickname, score);
            }
            // evaluating the private goal
            score = p.getPrivateGoal().evaluate(p);
            nOfCompletedGoals.addScore(p.nickname, score/p.getPrivateGoal().getGoalValue());
            scoreBoard.addScore(p.nickname, score);
        }

        int maxScore = -1;
        for(Player p : game.getPlayers()) {
            if(game.getScoreBoard().getScore(p.nickname) > maxScore) {
                maxScore = game.getScoreBoard().getScore(p.nickname);
            }
        }

        List<String> winners = new ArrayList<>();
        for(Player p : game.getPlayers()) {
            if(maxScore == game.getScoreBoard().getScore(p.nickname)) {
                winners.add(p.nickname);
            }
        }

        if(winners.size() > 1) {
            maxScore = -1;
            for(String p : winners) {
                if(nOfCompletedGoals.getScore(p) > maxScore) {
                    maxScore = nOfCompletedGoals.getScore(p);
                }
            }

            List<String> actualWinners = new ArrayList<>();
            for(String p : winners) {
                if(maxScore == nOfCompletedGoals.getScore(p)) {
                    actualWinners.add(p);
                }
            }

            winners = actualWinners;
        }

        System.out.println("winners: " + winners);

        game.getScoreBoard().setWinners(winners);

    }

    /**
     * Allows external users to (synchronously) update the game controller status
     */
    public synchronized void forceUpdateStatus() {
        this.updateStatus();
    }

    /**
     * Allows advance the game turn.
     */
    private void advanceTurn() {
        boolean needToSkipTurn, wasFirstPlayersTurn, newRoundStarted;

        // checking if the current turn was the first player's
        wasFirstPlayersTurn = this.game.isFirstPlayersTurn() || (turnStatus == null);

        if (this.turnStatus == null) {
            // this is the first turn of the first round, the first player have to place a card
            this.turnStatus = TurnStatus.PLACE;
        } else if (this.turnStatus == TurnStatus.PLACE && this.gameStatus != GameStatus.LAST_TURN) {
            // the player who just placed a card needs to draw
            this.turnStatus = TurnStatus.DRAW;
        } else {
            // the previous player finished his turn, the next player needs to place a card
            this.turnStatus = TurnStatus.PLACE;
            this.game.nextTurn();
        }

        // checking if the current turn should be skipped
        needToSkipTurn = !this.paused && this.game.getTurn().hasLeft();

        // checking if the current advance caused a new round to start
        newRoundStarted = !wasFirstPlayersTurn && this.game.isFirstPlayersTurn();

        this.newRoundStartedFlag = newRoundStarted;

        if (needToSkipTurn) {
            // if the current turn is skipped, updateStatus is called only if a new round started
            if (newRoundStarted) this.updateStatus();
            // forcing the next advanceTurn to move on to the next player
            synchronized (this) {this.turnStatus = TurnStatus.DRAW;}
            this.advanceTurn();
        } else {
            this.updateStatus();
        }

    }

    /**
     * Helper method to check if a card can be placed in the player's board
     * in a specified location.
     *
     * @param location the location that needs to be checked.
     * @param player   the player whose board needs to be checked.
     * @return {@code true} if the location is valid and a card can be placed, {@code false} otherwise.
     */
    private synchronized boolean isLocationValid(CardLocation location, Player player) {
        // checking if there's a card in the location
        if (player.getPlacedCardSlot(location) != null) return false;

        // checking if the top right neighbouring card has a hidden corner that makes the location invalid
        if (player.getPlacedCardSlot(location.topRightNeighbour()) != null &&
                (player.getPlacedCardSlot(location.topRightNeighbour()).getBottomLeftCorner() == null)) {
            return false;
        }

        // checking if the top left neighbouring card has a hidden corner that makes the location invalid
        if (player.getPlacedCardSlot(location.topLeftNeighbour()) != null &&
                (player.getPlacedCardSlot(location.topLeftNeighbour()).getBottomRightCorner() == null)) {
            return false;
        }

        // checking if the bottom right neighbouring card has a hidden corner that makes the location invalid
        if (player.getPlacedCardSlot(location.bottomRightNeighbour()) != null &&
                (player.getPlacedCardSlot(location.bottomRightNeighbour()).getTopLeftCorner() == null)) {
            return false;
        }

        // checking if the bottom left neighbouring card has a hidden corner that makes the location invalid
        if (player.getPlacedCardSlot(location.bottomLeftNeighbour()) != null &&
                (player.getPlacedCardSlot(location.bottomLeftNeighbour()).getTopRightCorner() == null)) {
            return false;
        }

        // checking if the location has neighbouring cards
        return player.getPlacedCardSlot(location.topRightNeighbour()) != null
                || player.getPlacedCardSlot(location.topLeftNeighbour()) != null
                || player.getPlacedCardSlot(location.bottomRightNeighbour()) != null
                || player.getPlacedCardSlot(location.bottomLeftNeighbour()) != null;
    }


    /**
     * Allows to select the private goal, among the available ones, for the
     * specified player.
     * <p>
     * This operation is only valid if the game is in GAME_CREATION status, if the
     * player exists and if they haven't already selected their private goal.
     *
     * @param playerIdentifier the identifier of the player who is selecting the private goal.
     * @param index            the index of the selected goal in the available ones.
     */
    @Override
    public synchronized void selectPrivateGoal(String playerIdentifier, int index) {
        // retrieving player's object
        Player player = this.helperGetPlayerByPlayerIdentifier(playerIdentifier);
        if (player == null) throw new RuntimeException("Unknown Player");


        // checking if the current game phase allows to select private goals (GAME_CREATION)
        if (gameStatus != GameStatus.GAME_CREATION) {
            throw new RuntimeException("Cannot select private goal in this game status");
        }

        // checking if the player hadn't already selected their private goal
        if (player.getPrivateGoal() != null) {
            throw new RuntimeException("Cannot select private goal twice");
        }

        // retrieving the player's available private goals
        Goal[] availableGoals = player.getAvailableGoals();

        // setting the selected goal as the player's private goal
        if (index < 0 || index >= availableGoals.length) {
            throw new RuntimeException("index out of bound");
        }
        player.setPrivateGoal(availableGoals[index]);

        System.err.println(this.game.getIdGame() + ": " + player.nickname + " selected their goal");
        // status update
        updateStatus();
        this.saveGameBackup();
    }

    /**
     * Allows to place the starting card, on a specific side, for the player.
     * <p>
     * This operation is only valid if the game is in NORMAL_TURN status or in LAST_TURN status,
     * if the player exists and if they haven't already placed their starting card.
     *
     * @param playerIdentifier the identifier of the player who is placing the starting card.
     * @param onBackSide       {@code true} if the starting card needs to be placed with the back side up, {@code false} otherwise.
     */
    public synchronized void placeStartCard(String playerIdentifier, boolean onBackSide) {
        // retrieving player's object
        Player player = this.helperGetPlayerByPlayerIdentifier(playerIdentifier);
        if (player == null) throw new RuntimeException("Unknown Player");

        // checking if the current game phase allows to place start cards (GAME_CREATION)
        if (gameStatus != GameStatus.GAME_CREATION) {
            throw new RuntimeException("Cannot place starting card in this game status");
        }

        // checking if the player hadn't already placed their starting card
        if (player.getPlacedCardSlot(new CardLocation(0, 0)) != null) {
            throw new RuntimeException("starting card already placed");
        }

        try {
            // placing the player's starting card
            player.placeStartingCard(onBackSide);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        System.err.println(this.game.getIdGame() + ": " + player.nickname + " placed their starting card");
        // update status
        updateStatus();
        this.saveGameBackup();
    }

    /**
     * Allows to place a card, on a specific side, in a player's board.
     * <p>
     * This operation is only valid if the game is in NORMAL_TURN status or in LAST_TURN status,
     * if the player exists, if it's their turn and if they haven't already placed a card in the current turn.
     *
     * @param playerIdentifier the identifier of the player who is placing the card.
     * @param index            the index of the card in the player's hand that needs to be placed.
     * @param onBackSide       {@code true} if the starting card needs to be placed with the back side up, {@code false} otherwise.
     * @param location         the CardLocation where the card needs to be placed in the player's board.
     */
    @Override
    public synchronized void placeCard(String playerIdentifier, int index, boolean onBackSide, CardLocation location) {
        // retrieving the player's object (also checking if it is this player's turn)
        Player player = turnCheck(playerIdentifier);

        // checking if the player is expected to place a card
        if (this.turnStatus != TurnStatus.PLACE) {
            throw new RuntimeException("Placement already occurred, player's expected to draw a card");
        }

        // checking if the provided location is valid
        if (!isLocationValid(location, player)) {
            throw new RuntimeException("cannot place card in the provided location");
        }

        // checking if the provided index is valid
        if (index < 0 || index >= player.getPlayerCards().length) {
            throw new RuntimeException("Card index's out of bound");
        }

        // checking if the selected card exists
        PlayCard card = player.getPlayerCards()[index];
        if (card == null) {
            throw new RuntimeException("there's no card in the specified slot");
        }

        // checking if the card's constraint is met
        if (!onBackSide && !card.getConstraint().isSubSetOf(player.getInventory())) {
            throw new RuntimeException("Card constraint isn't met");
        }

        try {
            // placing the card in the player's board
            player.placeCard(player.getPlayerCard(index), onBackSide, location);
            player.setPlayerCard(null, index);


            if (!onBackSide) {
                // evaluating the card scoring strategy (only if placed on the front side)

                // it is safe to assume that the card that was just placed is a PlayCard
                // since it is certain to come from the player's hand
                int score = ((PlayCard) player.getPlacedCardSlot(location).card()).getScoringStrategy().evaluate(player, location);
                ScoreBoard scoreBoard = game.getScoreBoard();
                scoreBoard.addScore(player.nickname, score);
            }

        } catch (InvalidParameterException e) {
            // Invalid index
            throw new RuntimeException(e);
        }

        advanceTurn();

        System.err.println(this.game.getIdGame() + ": " + player.nickname + " placed a card");

        // update status
        updateStatus();
        this.saveGameBackup();
    }

    /**
     * Helper method that actually perform the drawing procedure.
     *
     * @param player player that needs to receive the drawn card
     * @param index the card that needs to be drawn
     */
    private void drawCardHelper(Player player, int index) {
        if (index == 0) {
            // drawing from resource card deck
            Deck<PlayCard> deck = game.getResourceCardsDeck();
            if (deck.isEmpty()) {
                throw new RuntimeException("Deck's empty");
            }

            try {
                player.addPlayerCard(deck.draw());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (index == 1) {
            // drawing from gold card deck
            Deck<PlayCard> deck = game.getGoldCardsDeck();
            if (deck.isEmpty()) {
                throw new RuntimeException("Deck's empty");
            }

            try {
                player.addPlayerCard(deck.draw());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // drawing one of the visible cards

            index = index - 2;

            PlayCard[] visibleCards = game.getVisibleCards();

            if (visibleCards[index] != null) {
                try {
                    player.addPlayerCard(visibleCards[index]);
                    // card drawn successfully
                    visibleCards[index] = null;
                    game.refillVisibleCards();

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new RuntimeException("There's no card to draw in the provided location");
            }
        }
    }

    /**
     * Allows to a player to pick up one of the visible cards or to draw from the decks.
     * <p>
     * This operation is only valid if the game is in NORMAL_TURN status,
     * if the player exists, if it's their turn and if they have already placed a card in the current turn.
     *
     * @param playerIdentifier the identifier of player who is drawing.
     * @param index            the index of the cards that the player wants to pick up.
     */
    @Override
    public synchronized void drawCard(String playerIdentifier, int index) {
        // retrieving the player's object (also checking if it is this player's turn)
        Player player = turnCheck(playerIdentifier);


        // checking if the player's expected to draw
        if (this.turnStatus != TurnStatus.DRAW) {
            throw new RuntimeException("the player must place a card before they can draw");
        }

        // checking if the provided index is valid
        if (index < 0 || index > 5) {
            throw new RuntimeException("index out of range");
        }

        this.drawCardHelper(player, index);

        // card successfully drawn
        advanceTurn();
        this.saveGameBackup();

        System.err.println(this.game.getIdGame() + ": " + player.nickname + " picked up a card");
    }

    /**
     * Checks if the player identified by the provided playerIdentifier is expected to play in the current turn.
     * Also provide a reference to the player's object
     *
     * @param playerIdentifier the playerIdentifier of the player who is trying to play
     * @return the player's object
     * @throws RuntimeException if the player isn't expected to play in the current turn
     */
    private Player turnCheck(String playerIdentifier) {
        if (paused) throw new RuntimeException("this game is currently paused");

        Player player = this.helperGetPlayerByPlayerIdentifier(playerIdentifier);
        if (player == null) throw new RuntimeException("Unknown Player");

        if (!player.getClientHandler().getPlayerIdentifier().equals(game.getTurn().getClientHandler().getPlayerIdentifier())) {
            throw new RuntimeException("it is not this player's turn");
        }
        if (this.gameStatus != GameStatus.NORMAL_TURN && this.gameStatus != GameStatus.LAST_TURN) {
            throw new RuntimeException("Game's not running (finished or never started");
        }
        return player;
    }

    /**
     * Allows to a player to select their color.
     * <p>
     * This operation is only valid if the game is in GAME_CREATION status,
     * if the player exists and if the selected color is available
     *
     * @param playerIdentifier the identifier of the player who is selecting a color
     * @param color            the color selected by the player
     * @throws RuntimeException if the player is u
     */
    @Override
    public synchronized void selectColor(String playerIdentifier, PlayerColor color) {
        // retrieving the player's object
        Player player = this.helperGetPlayerByPlayerIdentifier(playerIdentifier);
        if (player == null) throw new RuntimeException("Unknown Player");

        // checking if the game is still in Lobby
        if(this.gameStatus != GameStatus.LOBBY) {
            throw new RuntimeException("Color cannot be changed once the game starts");
        }

        // checking if the color is available
        if (game.getAvailableColor().contains(color)) {
            player.setColor(color);
            game.updateAvailableColors();
        } else {
            throw new RuntimeException("This color isn't available");
        }

        System.err.println(this.game.getIdGame() + ": " + player.nickname + " selected " + color + " as their color");
        updateStatus();
    }

    /**
     * Allow to put a message (sent by an authenticated player) in one of the game chats (public or private)
     *
     * @param playerIdentifier the string that the player received while joining/creating the game
     * @param message the message that the player want to send
     * @param addressee the nickname of the player that needs to receive this message, {@code null} for public messages
     */
    @Override
    public synchronized void sendChatMsg(String playerIdentifier, String message, String addressee) {
        Player senderPlayer = this.helperGetPlayerByPlayerIdentifier(playerIdentifier);
        if (senderPlayer == null) throw new RuntimeException("chat msg fail: unknown sender");

        Player addresseePlayer;
        if (addressee == null) {
            // public message
            addresseePlayer = null;
        } else {
            // private message
            addresseePlayer = this.getPlayerByNickname(addressee);
            if (addresseePlayer == null) throw new RuntimeException("chat msg fail: unknown addressee");
        }

        this.game.getChat().sendMessage(senderPlayer, addresseePlayer, message);
    }

    /**
     * Thread method that processes messages asynchronously.
     */
    @Override
    public void run() {
        MainController selector = MainController.getInstance();
        ClientMessage message;
        while (this.getGameStatus() != GameStatus.DELETED) {
            try {
                message = this.messageQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            ClientHandler sender = selector.getPlayersClientHandler(message.getPlayerIdentifier());

            try {
                message.execute(selector, this);
                if (sender != null) sender.messageReceived();
            } catch (RuntimeException e) {

                if (sender != null) {
                    // sender was recognized, sending back a ServerErrorMessage
                    sender.sendMessage(new ServerErrorMessage(e));
                }
            }
        }
    }


    /**
     * Helper internal method. Not synchronized.
     * Allows to retrieve a reference to the object of the player whose identifier is provided.
     *
     * @param playerIdentifier the identifier of the player whose object is required
     * @return Player object of the requested player, {@code null} if the player isn't recognized within this game
     */
    private Player helperGetPlayerByPlayerIdentifier(String playerIdentifier) {
        for (Player p : this.game.getPlayers()) {
            if (p.getClientHandler().getPlayerIdentifier().equals(playerIdentifier)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Helper internal method. Not synchronized.
     * Allows to retrieve a reference to the object of the player whose nickname is provided.
     *
     * @param nickname the nickname of the player whose object is required
     * @return Player object of the requested player, {@code null} if the player isn't recognized within this game
     */
    private Player helperGetPlayerByNickname(String nickname) {
        for (Player p : this.game.getPlayers()) {
            if (p.nickname.equals(nickname)) {
                return p;
            }
        }
        return null;
    }


    /**
     * Allows to retrieve a reference to the object of the player whose identifier is provided.
     *
     * @param playerIdentifier the identifier of the player whose object is required
     * @return Player object of the requested player, {@code null} if the player isn't recognized within this game
     */
    public synchronized Player getPlayerByPlayerIdentifier(String playerIdentifier) {
        return this.helperGetPlayerByPlayerIdentifier(playerIdentifier);
    }

    /**
     * Allows to retrieve a reference to the object of the player whose nickname is provided.
     *
     * @param nickname the nickname of the player whose object is required
     * @return Player object of the requested player, {@code null} if the player isn't recognized within this game
     */
    public synchronized Player getPlayerByNickname(String nickname) {
        return this.helperGetPlayerByNickname(nickname);
    }


    /**
     * Checks if a provided nickname is available for this game
     *
     * @param nickname the nickname that needs to be checked
     * @return {@code true} if the nickname is available, {@code false} otherwise
     */
    public synchronized boolean isNicknameAvailable(String nickname) {
        for (Player p : this.game.getPlayers()) {
            if (p.nickname.equalsIgnoreCase(nickname)) {
                return false;
            }
        }
        return true;
    }
}
