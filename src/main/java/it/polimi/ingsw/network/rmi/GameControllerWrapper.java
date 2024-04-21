package it.polimi.ingsw.network.rmi;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.GameSelector;
import it.polimi.ingsw.utils.CardLocation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
public class GameControllerWrapper extends UnicastRemoteObject implements VirtualController {
    GameSelector gameSelector = GameSelector.getInstance();

    public GameControllerWrapper() throws RemoteException {
        super();
    }

    @Override
    public void placeCard(String playerIdentifier, int index, CardLocation location) throws RemoteException {
        GameController controller = gameSelector.getPlayersGame(playerIdentifier);

        if (controller == null) throw new IllegalArgumentException("Unknown player");

        controller.placeCard(gameSelector.getPlayer(playerIdentifier), index, location);
    }

    @Override
    public void drawCard(String playerIdentifier, int index) throws RemoteException {
        GameController controller = gameSelector.getPlayersGame(playerIdentifier);

        if (controller == null) throw new IllegalArgumentException("Unknown player");

        controller.drawCard(gameSelector.getPlayer(playerIdentifier), index);
    }

    @Override
    public void placeStartCard(String playerIdentifier, boolean onBackSide) throws RemoteException {
        GameController controller = gameSelector.getPlayersGame(playerIdentifier);

        if (controller == null) throw new IllegalArgumentException("Unknown player");

        controller.placeStartCard(gameSelector.getPlayer(playerIdentifier), onBackSide);
    }

    @Override
    public void selectPrivateGoal(String playerIdentifier, int index) throws RemoteException {
        GameController controller = gameSelector.getPlayersGame(playerIdentifier);

        if (controller == null) throw new IllegalArgumentException("Unknown player");

        controller.selectPrivateGoal(gameSelector.getPlayer(playerIdentifier), index);
    }
}
