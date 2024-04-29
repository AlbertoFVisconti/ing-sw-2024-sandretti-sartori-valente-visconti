package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.controller.GameSelector;
import it.polimi.ingsw.model.events.messages.client.*;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class GameControllerWrapper extends UnicastRemoteObject implements VirtualController {
    GameSelector gameSelector = GameSelector.getInstance();

    public GameControllerWrapper() throws RemoteException {
        super();
    }

    @Override
    public void placeCard(String playerIdentifier, int index, boolean onBackSide ,CardLocation location) throws RemoteException {
        ClientMessage message = new PlaceCardMessage(playerIdentifier, index, onBackSide, location);
        gameSelector.forwardMessage(message);
    }

    @Override
    public void drawCard(String playerIdentifier, int index) throws RemoteException {
        ClientMessage message = new DrawCardMessage(playerIdentifier, index);
        gameSelector.forwardMessage(message);
    }

    @Override
    public void placeStartCard(String playerIdentifier, boolean onBackSide) throws RemoteException {
        ClientMessage message = new PlaceStartCardMessage(playerIdentifier, onBackSide);
        gameSelector.forwardMessage(message);
    }

    @Override
    public void selectPrivateGoal(String playerIdentifier, int index) throws RemoteException {
        ClientMessage message = new SelectGoalMessage(playerIdentifier, index);
        gameSelector.forwardMessage(message);
    }
}
