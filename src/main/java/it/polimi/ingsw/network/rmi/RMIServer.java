package it.polimi.ingsw.network.rmi;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    private static final String MAIN_CONTROLLER_OBJECT_NAME = "MainController";
    //private static final String GAME_CONTROLLER_OBJECT_NAME = "GameController";
    public static void setup(int registryPort) throws RemoteException, AlreadyBoundException {

        MainControllerWrapper mainController = new MainControllerWrapper();
        //GameControllerWrapper gameControllerWrapper = new GameControllerWrapper();

        Registry registry = LocateRegistry.createRegistry(registryPort);
        registry.bind(MAIN_CONTROLLER_OBJECT_NAME, mainController);
        //registry.bind(GAME_CONTROLLER_OBJECT_NAME, gameControllerWrapper);

        System.out.println("RMI Server ready");
    }
}
