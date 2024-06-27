package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.messages.client.ClientMessage;
import it.polimi.ingsw.events.messages.server.ConnectionConfirmationMessage;
import it.polimi.ingsw.events.messages.server.GameListMessage;
import it.polimi.ingsw.events.messages.server.ServerErrorMessage;
import it.polimi.ingsw.events.messages.server.ServerToClientPingMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.decks.GoalDeckLoader;
import it.polimi.ingsw.model.decks.PlayCardDeckLoader;
import it.polimi.ingsw.model.decks.StartCardDeckLoader;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.cliendhandlers.ClientHandler;
import it.polimi.ingsw.network.cliendhandlers.RMIClientHandler;
import it.polimi.ingsw.network.rmi.GameControllerWrapper;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Allows to manage multiple games. It's a singleton that resides on the Server.
 * Receive messages from clients that wants to join or creates game.
 * Those messages are put in a BlockingQueue and are processed asynchronously.
 */
public class MainController extends Thread implements VirtualMainController {
    public static final int CLEANUP_PROCEDURE_TIMER = 30000; // 30sec

    private final Map<String, GameControllerWrapper> gameControllerWrappers;
    private final Map<String, GameController> playerIdentifierToGameController;
    private final Map<String, ClientHandler> playerIdentifierToClientHandler;

    private final BlockingQueue<ClientMessage> messageQueue;

    // Default deck loaders ready to build decks for all game (thus accessing the files only once)
    GoalDeckLoader goalDeckLoader = new GoalDeckLoader("/json/goals.json");
    PlayCardDeckLoader resourceCardDeckLoader = new PlayCardDeckLoader("/json/cards/resourcecards.json");
    PlayCardDeckLoader goldCardDeckLoader = new PlayCardDeckLoader("/json/cards/goldcards.json");
    StartCardDeckLoader startCardDeckLoader = new StartCardDeckLoader("/json/cards/startcards.json");

    private static MainController instance = null;

    /**
     * Construct the GameSelector object (only once since it's a singleton).
     * It's a private method because only the getInstance() method should be used
     * to get the instance.
     * Upon being created, the GameSelector instance starts processing messages
     * right away.
     */
    private MainController() {
        this.playerIdentifierToClientHandler = new HashMap<>();
        this.gameControllerWrappers = new HashMap<>();
        this.playerIdentifierToGameController = new HashMap<>();
        this.messageQueue = new ArrayBlockingQueue<>(100);
        this.start();


        ScheduledExecutorService disconnectionCheckerExecutor = Executors.newSingleThreadScheduledExecutor();
        disconnectionCheckerExecutor.scheduleAtFixedRate(new Thread(this::cleanUp), 0, CLEANUP_PROCEDURE_TIMER, TimeUnit.MILLISECONDS);


    }

    /**
     * Removes ended games and inactive players
     */
    private synchronized void cleanUp() {
        // removing inactive games (no connected players)
        /*
        for (String gameID : gameControllerWrappers.keySet()) {
            if (gameControllerWrappers.get(gameID).getGameController().getConnectedPlayers() == 0) {
                gameControllerWrappers.remove(gameID);
            }
        }
        */


        // removing inactive players' client handlers
        for (String playerID : playerIdentifierToClientHandler.keySet()) {
            ClientHandler clientHandler = playerIdentifierToClientHandler.get(playerID);
            if (clientHandler.getTimeSinceDisconnection() >= ClientHandler.DEFINITIVE_DISCONNECTION_THRESHOLD) {
                playerIdentifierToClientHandler.remove(playerID);
            }
        }

        // removing ended games' player->game mapping records
        for (String playerID : playerIdentifierToGameController.keySet()) {
            GameController gameController = playerIdentifierToGameController.get(playerID);
            if (gameController.getGameStatus() == GameStatus.END) {
                playerIdentifierToGameController.remove(playerID);
            }
        }
    }

    /**
     * Retrieves the only available instance of GameSelector.
     * If the instance hasn't been created yet, the method creates it first.
     *
     * @return reference to the GameSelector instance.
     */
    public static MainController getInstance() {
        if (instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    /**
     * Allows to put a message in the GameSelector queue.
     *
     * @param message the message that needs to be processed by the GameSelector.
     */
    public void forwardMessage(ClientMessage message) {
        try {
            this.messageQueue.put(message);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Removes a specified game from the Game list
     *
     * @param idGame the id of the game that needs to be removed.
     */
    public synchronized void removeGame(String idGame) {
        gameControllerWrappers.remove(idGame);
    }


    /**
     * Return the GameController object for a given game.
     *
     * @param gameId the gameID of the game whose GameController is needed.
     * @return a reference to the GameController that handles the specified game, {@code null} if there's no game with the provided GameID.
     */
    public synchronized GameController getGameController(String gameId) throws NullPointerException{
        return this.gameControllerWrappers.get(gameId).getGameController();
    }

    /**
     * Retrieves the GameController that handles the game that
     * the player (whose identifier is provided) is playing.
     *
     * @param playerIdentifier the identifier of the player whose GameController is needed.
     * @return a reference to the GameController that handles the player's game, {@code null} if the player wasn't recognised.
     */
    public synchronized GameController getPlayersGameController(String playerIdentifier) {
        return playerIdentifierToGameController.get(playerIdentifier);
    }

    /**
     * Retrieves the ClientHandler that handles the client of
     * the player whose identifier is provided.
     *
     * @param playerIdentifier the identifier of the player whose ClientHandler is needed
     * @return a reference to the ClientHandler that handles the client of the player, {@code null} if the player wasn't recognised.
     */
    public synchronized ClientHandler getPlayersClientHandler(String playerIdentifier) {
        return playerIdentifierToClientHandler.get(playerIdentifier);
    }

    /**
     * Retrieves the Player object that represents the player whose playerIdentifier is provided.
     *
     * @param playerIdentifier the identifier of the player whose object is needed.
     * @return a reference to the Player with the specified playerIdentifier, {@code null} if the player wasn't recognised.
     */
    public synchronized Player getPlayer(String playerIdentifier) {
        GameController controller = playerIdentifierToGameController.get(playerIdentifier);
        if (controller == null) return null;

        return controller.getPlayerByPlayerIdentifier(playerIdentifier);
    }

    /**
     * Thread method that asynchronously processes the GameSelector messages.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            ClientMessage message;
            try {
                // taking the next message that needs to be processed
                message = this.messageQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // retrieving the message sender's client handler
            ClientHandler clientHandler = this.getPlayersClientHandler(message.getPlayerIdentifier());

            // if the sender was recognized, resetting the clientHandler's ping/disconnection timer
            if (clientHandler != null) clientHandler.messageReceived();

            try {
                // executing the received message
                message.execute(this, null);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());

                if (clientHandler != null) {
                    // reporting the error to the message sender
                    clientHandler.sendMessage(new ServerErrorMessage(e));
                }
            }
        }
    }

    /**
     * Allows a player to join a game.
     *
     * @param playerIdentifier the identifier of the client that is joining the game
     * @param IDGame           The numerical identifier of the game, chosen among the available ones
     * @param nickname         The players nickname, unique for the game
     */
    @Override
    public synchronized void joinGame(String playerIdentifier, String IDGame, String nickname) {
        // retrieving the player's client handler
        ClientHandler clientHandler = this.getPlayersClientHandler(playerIdentifier);
        if (clientHandler == null) throw new RuntimeException("player was not recognized");

        // retrieving GameControllerWrapper and GameController
        GameControllerWrapper gameControllerWrapper;
        GameController controller;

        gameControllerWrapper = this.gameControllerWrappers.get(IDGame);
        if (gameControllerWrapper == null) throw new RuntimeException("the selected game does not exist");
        controller = gameControllerWrapper.getGameController();


        // no need to lock the GameController: if the GameStatus
        // wasn't lobby in the first place, it cannot go back to Lobby
        // if it was lobby and then changed it means that the player
        // tried to join a game that was filled just before they could
        // join: an exception will be raised by GameController.addNewPlayer() and
        // it will be reported to the user: the joining process will fail
        if (controller.getGameStatus() == GameStatus.LOBBY) {
            // game is in lobby ==> the player is NOT rejoining, it is a new player

            // checking if the selected nickname is available for this game
            if (!controller.isNicknameAvailable(nickname))
                throw new RuntimeException("the selected nickname is unavailable");

            // adding the new player to the game (might cause an exception to be thrown)
            controller.addNewPlayer(nickname, clientHandler, gameControllerWrapper);
        } else {
            // game is running ==> the player might be trying to rejoin
            // making the player rejoin the game (might cause an exception to be thrown)
            controller.handleReconnection(nickname, clientHandler, gameControllerWrapper);
        }

        this.playerIdentifierToGameController.put(clientHandler.getPlayerIdentifier(), controller);

        System.err.println(nickname + " joined the game");
    }

    /**
     * Allows the player to request a list of available games to join
     *
     * @param playerIdentifier the identifier of the client that is creating a game
     */
    @Override
    public synchronized void getAvailableGames(String playerIdentifier) {
        ClientHandler clientHandler = this.playerIdentifierToClientHandler.get(playerIdentifier);
        if (clientHandler != null) {
            clientHandler.sendMessage(new GameListMessage(this.gameControllerWrappers.keySet()));
        }
    }

    /**
     * Allows to connect a (RMI) client to the server.
     *
     * @param view a reference to the client's view (remote object)
     */
    @Override
    public void connect(VirtualView view) {
        this.connectClient(new RMIClientHandler(view));
    }

    /**
     * Allows the client to send a ping message to the server.
     *
     * @param playerIdentifier the identifier of the player who sending the ping message.
     * @param isAnswer         {@code true} if the server is answering to a previous ping message, {@code false} if the server is checking on the client.
     */
    @Override
    public synchronized void ping(String playerIdentifier, boolean isAnswer) {
        ClientHandler clientHandler = this.getPlayersClientHandler(playerIdentifier);

        if (clientHandler != null) {
            clientHandler.sendMessage(new ServerToClientPingMessage(true));
        }
    }

    /**
     * Allows to make a player leave a game (without the need for the client to disconnect from the server)
     *
     * @param playerIdentifier the identifier of the player who is leaving the game
     */
    @Override
    public synchronized void leaveGame(String playerIdentifier) {
        GameController gameController = playerIdentifierToGameController.get(playerIdentifier);

        if(gameController != null) {
            ClientHandler clientHandler = this.getPlayersClientHandler(playerIdentifier);
            gameController.handleDisconnection(clientHandler);
            this.playerIdentifierToGameController.remove(playerIdentifier);
        }
    }

    /**
     * Allows a player to create a new game.
     *
     * @param playerIdentifier the identifier of the client that is creating a game
     * @param expectedPlayers  The number of players the game is expected to handle
     * @param nick             The nickname of the player creating the game
     * @throws RemoteException if an error occurs while setting up the remote object that wraps the GameController
     */
    @Override
    public synchronized void createGame(String playerIdentifier, int expectedPlayers, String nick) throws RemoteException {
        if(gameControllerWrappers.containsKey(nick)) throw new RuntimeException("Game creators needs to have unique names and this one was already taken");

        Game g;
        try {
            g = new Game(goldCardDeckLoader, resourceCardDeckLoader, startCardDeckLoader, goalDeckLoader, nick, expectedPlayers);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GameController controller = new GameController(g);
        GameControllerWrapper controllerWrapper = new GameControllerWrapper(controller);

        gameControllerWrappers.put(nick, controllerWrapper);

        joinGame(playerIdentifier, nick, nick);
    }

    /**
     * Complete a client's connection to the server.
     * Generates, stores and sends (to the client) the player identifier.
     *
     * @param clientHandler the client handler of the client whose connection needs to be completed
     */
    public synchronized void connectClient(ClientHandler clientHandler) {
        Random rand = new Random();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String t = timestamp.toString() + rand.nextInt(0, 1000000);

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        md.update(t.getBytes());
        byte[] digest = md.digest();

        String playerIdentifier = String.format("%032X", new BigInteger(1, digest));

        this.playerIdentifierToClientHandler.put(playerIdentifier, clientHandler);

        clientHandler.setPlayerIdentifier(playerIdentifier);
        clientHandler.sendMessage(new ConnectionConfirmationMessage(playerIdentifier));
    }
}
