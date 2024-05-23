package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.events.messages.client.*;
import it.polimi.ingsw.model.player.PlayerColor;
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
        ClientMessage message = new PlaceCardMessage(index, onBackSide, location);
        message.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(message);
    }

    @Override
    public void drawCard(String playerIdentifier, int index) throws RemoteException {
        ClientMessage message = new DrawCardMessage(index);
        message.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(message);
    }

    @Override
    public void placeStartCard(String playerIdentifier, boolean onBackSide) throws RemoteException {
        ClientMessage message = new PlaceStartCardMessage(onBackSide);
        message.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(message);
    }

    @Override
    public void selectPrivateGoal(String playerIdentifier, int index) throws RemoteException {
        ClientMessage message = new SelectGoalMessage(index);
        message.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(message);
    }

    @Override
    public void selectColor(String playerIdentifier, PlayerColor color) throws RemoteException {
        ClientMessage message = new SelectColorMessage(color);
        message.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(message);
    }

    @Override
    public void sendChatMsg(String playerIdentifier, String message, String addressee) throws RemoteException {
        ClientMessage clientMessage = new ClientChatMsgMessage(message, addressee);
        clientMessage.setPlayerIdentifier(playerIdentifier);
        gameController.forwardMessage(clientMessage);
    }
}
