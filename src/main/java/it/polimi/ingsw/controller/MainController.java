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

public class MainController extends UnicastRemoteObject implements VirtualMainController {
    public MainController() throws RemoteException {
    }

    @Override
    public void joinGame(VirtualView view, int IDgame, PlayerColor color, String nick) throws RemoteException {
        GameSelector gameSelector = GameSelector.getInstance();

        ClientMessage message = new JoinGameMessage(IDgame, false, -1, nick, color, new RMIClientHandler(view));
        gameSelector.forwardMessage(message);
    }

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
