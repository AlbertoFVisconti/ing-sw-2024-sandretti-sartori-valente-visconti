package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.events.messages.client.*;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class GameControllerWrapper extends UnicastRemoteObject implements VirtualController {
    private final GameController gameController;

    public GameControllerWrapper(GameController game) throws RemoteException {
        super();
        this.gameController = game;
    }

    public GameController getGameController() {
        return gameController;
    }

    @Override
    public void placeCard(String playerIdentifier, int index, boolean onBackSide ,CardLocation location) throws RemoteException {
        ClientMessage message = new PlaceCardMessage(playerIdentifier, index, onBackSide, location);
        gameController.forwardMessage(message);
    }

    @Override
    public void drawCard(String playerIdentifier, int index) throws RemoteException {
        ClientMessage message = new DrawCardMessage(playerIdentifier, index);
        gameController.forwardMessage(message);
    }

    @Override
    public void placeStartCard(String playerIdentifier, boolean onBackSide) throws RemoteException {
        ClientMessage message = new PlaceStartCardMessage(playerIdentifier, onBackSide);
        gameController.forwardMessage(message);
    }

    @Override
    public void selectPrivateGoal(String playerIdentifier, int index) throws RemoteException {
        ClientMessage message = new SelectGoalMessage(playerIdentifier, index);
        gameController.forwardMessage(message);
    }
}
