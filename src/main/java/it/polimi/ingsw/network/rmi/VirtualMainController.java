package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.view.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 */
public interface VirtualMainController extends Remote {

    /**
     * Allows a remote client to try to join a specified game.
     * <p>
     * Might throw RuntimeExceptions if the player's nickname is unavailable, or if the selected game doesn't exist.
     *
     * @param playerIdentifier the identifier of the client that is joining the game
     * @param IDgame           The identifier of the game, chosen among the available ones
     * @param nick             The players nickname, unique for the game
     * @throws RemoteException  in case of errors with the remote communication
     * @throws RuntimeException if color or nickname are unavailable or if game doesn't exist
     */
    void joinGame(String playerIdentifier, String IDgame, String nick) throws RemoteException;

    /**
     * Allows a remote client to try to create and join a new game.
     *
     * @param playerIdentifier the identifier of the client that is creating a game
     * @param expectedPlayers  The number of players the game is expected to handle
     * @param nick             The nickname of the player creating the game
     * @throws RemoteException in case of errors with the remote communication
     */
    void createGame(String playerIdentifier, int expectedPlayers, String nick) throws RemoteException;

    /**
     * Asks the controller to retrieve a list of all the available games' identifier in order to allow a client to select one to join.
     *
     * @param playerIdentifier the identifier of the client that is creating a game
     * @throws RemoteException in case of errors with the remote communication
     */
    void getAvailableGames(String playerIdentifier) throws RemoteException;

    /**
     * Allows a client to connect to the server.
     *
     * @param view a reference to the client's view (remote object)
     * @throws RemoteException in case of errors with the remote communication
     */
    void connect(VirtualView view) throws RemoteException;

    /**
     * Allows the client to send a ping message to the server.
     *
     * @param playerIdentifier the identifier of the player who is sending the ping message.
     * @param isAnswer         {@code true} if the server is answering to a previous ping message, {@code false} if the server is checking on the client.
     * @throws RemoteException in case of error with the remote communication.
     */
    void ping(String playerIdentifier, boolean isAnswer) throws RemoteException;

    /**
     * Allows the client to inform the server that the player wants to leave the game.
     *
     * @param playerIdentifier the identifier of the player who is leaving the game
     */
    void leaveGame(String playerIdentifier) throws RemoteException;
}
