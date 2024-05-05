package it.polimi.ingsw.network.serverhandlers;

import it.polimi.ingsw.model.player.PlayerColor;
import it.polimi.ingsw.network.rmi.VirtualController;
import it.polimi.ingsw.network.rmi.VirtualMainController;
import it.polimi.ingsw.view.ui.UserInterface;
import it.polimi.ingsw.utils.CardLocation;
import it.polimi.ingsw.view.ViewWrapper;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServerHandler extends ServerHandler{

    private VirtualController controller;
    private final VirtualMainController mainController;


    public RMIServerHandler(UserInterface userInterface,String ip, int port) throws RemoteException, NotBoundException {
        super(userInterface);

        Registry registry = LocateRegistry.getRegistry(ip, port);

        mainController = (VirtualMainController) registry.lookup("MainController");

        mainController.connect(new ViewWrapper(userInterface));

    }

    public void setController(VirtualController controller) {
        this.controller = controller;
    }

    @Override
    public void joinGame(int IDGame, String nick) {
        try {
            mainController.joinGame(this.getUserInterface().getPlayerIdentifier(), IDGame, nick);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGame(int expectedPlayers, String nick) {
        try {
            mainController.createGame(this.getUserInterface().getPlayerIdentifier(), expectedPlayers, nick);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void getAvailableGames() {
        try {
            mainController.getAvailableGames(this.getUserInterface().getPlayerIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeCard(int index, boolean onBackSide, CardLocation location) {
        try {
            controller.placeCard(this.getUserInterface().getPlayerIdentifier(), index, onBackSide, location);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawCard(int index) {
        try {
            controller.drawCard(this.getUserInterface().getPlayerIdentifier(), index);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeStartCard(boolean onBackSide) {
        try {
            controller.placeStartCard(this.getUserInterface().getPlayerIdentifier(), onBackSide);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectPrivateGoal(int index) {
        try {
            controller.selectPrivateGoal(this.getUserInterface().getPlayerIdentifier(), index);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void selectColor(PlayerColor color) {
        try {
            controller.selectColor(this.getUserInterface().getPlayerIdentifier(),color);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


}
