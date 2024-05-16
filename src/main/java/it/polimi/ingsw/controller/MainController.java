package it.polimi.ingsw.controller;

import it.polimi.ingsw.events.messages.server.GameListMessage;
import it.polimi.ingsw.events.messages.server.JoinConfirmationMessage;
import it.polimi.ingsw.events.messages.server.ServerErrorMessage;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.decks.GoalDeckLoader;
import it.polimi.ingsw.model.decks.PlayCardDeckLoader;
import it.polimi.ingsw.model.decks.StartCardDeckLoader;
import it.polimi.ingsw.events.messages.client.ClientMessage;
import it.polimi.ingsw.events.messages.server.ConnectionConfirmationMessage;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.cliendhandlers.RMIClientHandler;
import it.polimi.ingsw.network.rmi.GameControllerWrapper;
import it.polimi.ingsw.network.cliendhandlers.ClientHandler;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Allows to manage multiple games. It's a singleton that resides on the Server.
 * Receive messages from clients that wants to join or creates game.
 * Those messages are put in a BlockingQueue and are processed asynchronously.
 */
public class MainController extends Thread implements VirtualMainController {
    private final Map<Integer, GameControllerWrapper> gameControllerWrappers;
    private final Map<String, GameController> playerIdentifierToGameController;
    private final Map<String, ClientHandler> playerIdentifierToClientHandler;
    private int nextGameID =0;

    private final BlockingQueue<ClientMessage> messageQueue;

    // Default deck loaders ready to build decks for all game (thus accessing the files only once)
    GoalDeckLoader goalDeckLoader= new GoalDeckLoader("src/main/resources/json/goals.json");
    PlayCardDeckLoader resourceCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/resourcecards.json");
    PlayCardDeckLoader goldCardDeckLoader= new PlayCardDeckLoader("src/main/resources/json/cards/goldcards.json");
    StartCardDeckLoader startCardDeckLoader= new StartCardDeckLoader("src/main/resources/json/cards/startcards.json");

    private static MainController instance = null;

    /**
     * Construct the GameSelector object (only one since it's a singleton).
     * It's a private method because only the getInstance() method should be used
     * to get the instance.
     * Upon being created, the GameSelector instance starts processing messages
     * right away.
     */
    private MainController(){
        this.playerIdentifierToClientHandler = new HashMap<>();
        this.gameControllerWrappers = new HashMap<>();
        this.playerIdentifierToGameController = new HashMap<>();
        this.messageQueue = new ArrayBlockingQueue<>(100);
        this.start();
    }

    /**
     * Retrieves the only available instance of GameSelector.
     * If the instance hasn't been created yet, the method creates it first.
     *
     * @return reference to the GameSelector instance.
     */
    public static MainController getInstance() {
        if(instance == null) {
            instance = new MainController();
        }
        return instance;
    }

    /**
     * Allows to put a message in the GameSelector queue.
     *
     * @param message the message that needs to be processed by the GameSelector.
     */
    public void forwardMessage(ClientMessage message)  {
        try {
            this.messageQueue.put(message);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Helper method that allows to check whether a given nickname is available in a given Game.
     *
     * @param game the Game you want to check.
     * @param nickname the nick you want to check.
     * @return {@code true} if there are no player with the same nickname in the selected game, {@code false} otherwise.
     * **/
    public static boolean isAvailable(Game game, String nickname){
        List<Player> temp=game.getPlayers();
        for(Player p: temp){
            if(p.nickName.equals(nickname))  return false;
        }
        return true;
    }

    /**
     * Removes a specified game from the Game list
     *
     * @param idGame the id of the game that needs to be removed.
     */
    public void RemoveGame(int idGame){
        gameControllerWrappers.remove(idGame);
    }


    /**
     * Return the GameController object for a given game.
     *
     * @param gameId the gameID of the game whose GameController is needed.
     * @return a reference to the GameController that handles the specified game, {@code null} if there's no game with the provided GameID.
     */
    public GameController getGameController(int gameId) {
        return this.gameControllerWrappers.get(gameId).getGameController();
    }

    /**
     * Retrieves the GameController that handles the game that
     * the player (whose identifier is provided) is playing.
     *
     * @param playerIdentifier the identifier of the player whose GameController is needed.
     * @return a reference to the GameController that handles the player's game, {@code null} if the player wasn't recognised.
     */
    public GameController getPlayersGameController(String playerIdentifier) {
        return playerIdentifierToGameController.get(playerIdentifier);
    }

    public ClientHandler getPlayersClientHandler(String playerIdentifier) {
        return playerIdentifierToClientHandler.get(playerIdentifier);
    }

    /**
     * Retrieves the Player object that represents the player whose playerIdentifier is provided.
     *
     * @param playerIdentifier the identifier of the player whose object is needed.
     * @return a reference to the Player with the specified playerIdentifier, {@code null} if the player wasn't recognised.
     */
    public Player getPlayer(String playerIdentifier) {
        GameController controller = playerIdentifierToGameController.get(playerIdentifier);
        if(controller == null) return null;

        Game game = controller.getGame();

        for(Player p : game.getPlayers()) {
            if(Objects.equals(p.getClientHandler().getPlayerIdentifier(), playerIdentifier)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Thread method that asynchronously processes the GameSelector messages.
     */
    @Override
    public void run() {
        while(true) {
            System.out.println("waiting...");
            ClientMessage message;
            try {
                message = this.messageQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            try {
                message.execute(this, null);
            }
            catch (RuntimeException e) {
                ClientHandler clientHandler = this.getPlayersClientHandler(message.getPlayerIdentifier());
                if(clientHandler != null) {
                    clientHandler.sendMessage(new ServerErrorMessage(e));
                }
            }
        }
    }

    @Override
    public void joinGame(String playerIdentifier, int IDGame, String nickname) throws RemoteException {
        ClientHandler clientHandler = this.playerIdentifierToClientHandler.get(playerIdentifier);
        if(clientHandler == null) throw new RuntimeException("player was not recognized");

        GameControllerWrapper gameControllerWrapper = this.gameControllerWrappers.get(IDGame);
        if(gameControllerWrapper == null) throw new RuntimeException("the selected game does not exist");

        GameController controller = gameControllerWrapper.getGameController();
        Game game = controller.getGame();

        if(!isAvailable(game, nickname)) throw new RuntimeException("the selected nickname is unavailable");

        clientHandler.setController(this.gameControllerWrappers.get(IDGame));
        this.playerIdentifierToGameController.put(clientHandler.getPlayerIdentifier(), this.gameControllerWrappers.get(IDGame).getGameController());

        // will throw a RuntimeException if the game's already started
        game.addPlayer(new Player( nickname, clientHandler));


        clientHandler.sendMessage(new JoinConfirmationMessage(nickname));

        System.err.println(nickname + " joined the game");

        controller.subscribe(clientHandler);
        controller.updateStatus();
    }

    @Override
    public void createGame(String playerIdentifier, int expectedPlayers, String nick) throws RemoteException {
        Game g= null;
        try {
            g = new Game(goldCardDeckLoader,resourceCardDeckLoader,startCardDeckLoader,goalDeckLoader, nextGameID,expectedPlayers );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GameController controller = new GameController(g);
        GameControllerWrapper controllerWrapper = new GameControllerWrapper(controller);

        gameControllerWrappers.put(nextGameID,controllerWrapper);

        joinGame(playerIdentifier, nextGameID++, nick);
    }

    @Override
    public void getAvailableGames(String playerIdentifier) throws RemoteException {
        ClientHandler clientHandler = this.playerIdentifierToClientHandler.get(playerIdentifier);
        if(clientHandler != null) {
            clientHandler.sendMessage(new GameListMessage(this.gameControllerWrappers.keySet()));
        }
    }

    @Override
    public void connect(VirtualView view) throws RemoteException {
        this.connectClient(new RMIClientHandler(view));
    }

    public void connectClient(ClientHandler clientHandler) {
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
