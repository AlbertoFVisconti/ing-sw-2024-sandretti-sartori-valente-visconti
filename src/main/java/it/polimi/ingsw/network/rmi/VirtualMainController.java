package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;
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
     * Might throw RuntimeExceptions if the player's nickname or color is unavailable, or if the selected game doesn't exist.
     *
     * @param playerIdentifier the identifier of the client that is joining the game
     * @param IDgame The numerical identifier of the game, chosen among the available ones
     * @param color The players color, unique for the game
     * @param nick The players nickname, unique for the game
     * @throws RemoteException in case of errors with the remote communication
     * @throws RuntimeException if color or nickname are unavailable or if game doesn't exist
     */
    void joinGame (String playerIdentifier, int IDgame, PlayerColor color, String nick) throws RemoteException;

    /**
     * Allows a remote client to try to create and join a new game.
     *
     * @param playerIdentifier the identifier of the client that is creating a game
     * @param expectedPlayers The number of players the game is expected to handle
     * @param color The color of the player creating the game
     * @param nick The nickname of the player creating the game
     * @throws RemoteException in case of errors with the remote communication
     */
    void createGame(String playerIdentifier, int expectedPlayers, PlayerColor color, String nick) throws RemoteException;

    /**
     * Asks the controller to retrieve a list of all the available games' identifier in order to allow a client to select one to join.
     *
     * @param playerIdentifier the identifier of the client that is creating a game
     * @throws RemoteException in case of errors with the remote communication
     */
    void getAvailableGames(String playerIdentifier) throws RemoteException;

    void connect(VirtualView view) throws RemoteException;
}
