package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.MainController;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {

        MainController mainController = new MainController();
        GameControllerWrapper gameControllerWrapper = new GameControllerWrapper();

        Registry registry = LocateRegistry.createRegistry(1234);
        registry.bind("MainController", mainController);
        registry.bind("GameController", gameControllerWrapper);

        System.out.println("Server ready");
    }
}
