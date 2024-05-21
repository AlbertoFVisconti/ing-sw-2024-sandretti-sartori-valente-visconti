package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.MainController;
import it.polimi.ingsw.events.messages.client.*;
import it.polimi.ingsw.view.VirtualView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Remote object that allows players with RMI client to join and create game.
 * It also allows to access information regarding the available games.
 * This object doesn't process requests, it simply builds messages that represents such requests
 * and forward them to the GameSelector, that will answer with another invocation.
 */
public class MainControllerWrapper extends UnicastRemoteObject implements VirtualMainController {
    private final MainController mainController;

    /**
     * Construct the MainController remote object.
     *
     * @throws RemoteException in case of errors during the remote method invocation.
     */
    public MainControllerWrapper() throws RemoteException {
        mainController = MainController.getInstance();
    }

    /**
     * Receives requests to join an existing game.
     * Builds the message that represents such request and forwards it to the GameSelector.
     * The GameSelector will then give the player another remote object that will handle the communication for the game.
     *
     * @param playerIdentifier the identifier of the client
     * @param IDGame The numerical identifier of the game, chosen among the available ones
     * @param nick The players nickname, unique for the game
     * @throws RemoteException in case of errors during the remote method invocation.
     */
    @Override
    public void joinGame(String playerIdentifier, int IDGame, String nick) throws RemoteException {
        ClientMessage message = new JoinGameMessage(IDGame, false, -1, nick);
        message.setPlayerIdentifier(playerIdentifier);
        mainController.forwardMessage(message);
    }

    /**
     * Receives requests to create a new game and to join it.
     * Builds the message that represents such request and forward it to the GameSelector.
     * The GameSelector will then give the player another remote object that will handle the communication for the game.
     *
     * @param playerIdentifier the identifier of the client
     * @param expectedPlayers The number of players the game is expected to handle
     * @param nick The nickname of the player creating the game
     * @throws RemoteException in case of errors during the remote method invocation.
     */
    @Override
    public void createGame(String playerIdentifier, int expectedPlayers, String nick) throws RemoteException {
        ClientMessage message = new JoinGameMessage(-1, true, expectedPlayers, nick);
        message.setPlayerIdentifier(playerIdentifier);
        mainController.forwardMessage(message);

    }

    @Override
    public void getAvailableGames(String playerIdentifier) throws RemoteException {
        ClientMessage message = new GameListRequestMessage();
        message.setPlayerIdentifier(playerIdentifier);
        mainController.forwardMessage(message);
    }

    @Override
    public void connect(VirtualView view) throws RemoteException {
        ClientMessage message = new RMIConnectionPseudoMessage(view);
        mainController.forwardMessage(message);
    }

    @Override
    public void ping(String playerIdentifier, boolean isAnswer) throws RemoteException {
        ClientMessage message = new ClientToServerPingMessage(isAnswer);
        message.setPlayerIdentifier(playerIdentifier);
        mainController.forwardMessage(message);
    }
}
