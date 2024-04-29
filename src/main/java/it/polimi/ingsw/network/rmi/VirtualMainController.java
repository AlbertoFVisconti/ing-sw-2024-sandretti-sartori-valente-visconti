package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 */
public interface VirtualMainController extends Remote {

    /**
     * Allows a remote client to try to join a specified game.
     * <p>
     * Might throw RuntimeExceptions if the player's nickname or color is unavailable, or if the selected game doesn't exist.
     *
     * @param view A remote object representing the client's view. Used to push updates of the game model
     * @param IDgame The numerical identifier of the game, chosen among the available ones
     * @param color The players color, unique for the game
     * @param nick The players nickname, unique for the game
     * @throws RemoteException in case of errors with the remote communication
     * @throws RuntimeException if color or nickname are unavailable or if game doesn't exist
     */
    void joinGame (VirtualView view, int IDgame, PlayerColor color, String nick) throws RemoteException;

    /**
     * Allows a remote client to try to create and join a new game.
     *
     * @param view a remote object representing the client's view. Used to push updates of the game model
     * @param expectedPlayers The number of players the game is expected to handle
     * @param color The color of the player creating the game
     * @param nick The nickname of the player creating the game
     * @throws RemoteException in case of errors with the remote communication
     */
    void createGame(VirtualView view, int expectedPlayers, PlayerColor color, String nick) throws RemoteException;

    /**
     * Asks the controller to retrieve a list of all the available games' identifier in order to allow a client to select one to join.
     *
     * @return a List of integer where each number represents a game
     * @throws RemoteException in case of errors with the remote communication
     */
    List<Integer> getAvailableGames() throws RemoteException;
}
