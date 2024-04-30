package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.events.messages.client.ClientMessage;
import it.polimi.ingsw.model.events.messages.client.JoinGameMessage;
import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.view.RMIClientHandler;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 * Remote object that allows players with RMI client to join and create game.
 * It also allows to access information regarding the available games.
 * This object doesn't process requests, it simply builds messages that represents such requests
 * and forward them to the GameSelector, that will answer with another invocation.
 */
public class MainController extends UnicastRemoteObject implements VirtualMainController {
    /**
     * Construct the MainController remote object.
     *
     * @throws RemoteException in case of errors during the remote method invocation.
     */
    public MainController() throws RemoteException {}

    /**
     * Receives requests to join an existing game.
     * Builds the message that represents such request and forwards it to the GameSelector.
     * The GameSelector will then give the player another remote object that will handle the communication for the game.
     *
     * @param view A remote object representing the client's view. Used to push updates of the game model
     * @param IDGame The numerical identifier of the game, chosen among the available ones
     * @param color The players color, unique for the game
     * @param nick The players nickname, unique for the game
     * @throws RemoteException in case of errors during the remote method invocation.
     */
    @Override
    public void joinGame(VirtualView view, int IDGame, PlayerColor color, String nick) throws RemoteException {
        GameSelector gameSelector = GameSelector.getInstance();

        ClientMessage message = new JoinGameMessage(IDGame, false, -1, nick, color, new RMIClientHandler(view));

        gameSelector.forwardMessage(message);
    }

    /**
     * Receives requests to create a new game and to join it.
     * Builds the message that represents such request and forward it to the GameSelector.
     * The GameSelector will then give the player another remote object that will handle the communication for the game.
     *
     * @param view a remote object representing the client's view. Used to push updates of the game model
     * @param expectedPlayers The number of players the game is expected to handle
     * @param color The color of the player creating the game
     * @param nick The nickname of the player creating the game
     * @throws RemoteException in case of errors during the remote method invocation.
     */
    @Override
    public void createGame(VirtualView view, int expectedPlayers, PlayerColor color, String nick) throws RemoteException {
        GameSelector gameSelector = GameSelector.getInstance();


        ClientMessage message = new JoinGameMessage(-1, true, expectedPlayers, nick, color, new RMIClientHandler(view));
        gameSelector.forwardMessage(message);

    }

    @Override
    public List<Integer> getAvailableGames() throws RemoteException {
        return GameSelector.getInstance().getAvailableGames().stream().toList();
    }
}
