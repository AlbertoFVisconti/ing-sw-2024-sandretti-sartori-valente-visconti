package it.polimi.ingsw.network.serverhandlers;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewWrapper;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerHandler extends ServerHandler{

    private VirtualController controller;
    private final VirtualMainController mainController;


    public RMIServerHandler(View clientView, String ip, int port) throws RemoteException, NotBoundException {
        super(clientView);

        Registry registry = LocateRegistry.getRegistry(ip, port);

        mainController = (VirtualMainController) registry.lookup("MainController");

        mainController.connect(new ViewWrapper(this));

    }

    public void setController(VirtualController controller) {
        this.controller = controller;
    }

    @Override
    public void joinGame(int IDGame, PlayerColor color, String nick) {
        try {
            mainController.joinGame(this.getClientView().getPlayerIdentifier(), IDGame ,color, nick);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(int expectedPlayers, PlayerColor color, String nick) {
        try {
            mainController.createGame(this.getClientView().getPlayerIdentifier(), expectedPlayers ,color, nick);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getAvailableGames() {
        try {
            mainController.getAvailableGames(this.getClientView().getPlayerIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeCard(int index, boolean onBackSide, CardLocation location) {
        try {
            controller.placeCard(this.getClientView().getPlayerIdentifier(), index, onBackSide, location);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawCard(int index) {
        try {
            controller.drawCard(this.getClientView().getPlayerIdentifier(), index);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeStartCard(boolean onBackSide) {
        try {
            controller.placeStartCard(this.getClientView().getPlayerIdentifier(), onBackSide);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectPrivateGoal(int index) {
        try {
            controller.selectPrivateGoal(this.getClientView().getPlayerIdentifier(), index);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


}
