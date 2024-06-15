package it.polimi.ingsw.network.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Contains static method (RMIServer.setup) that allows to set up the RMI components
 * that allows RMI clients to connect and play.
 */
public class RMIServer {
    // the remote object name in the RMI registry
    private static final String MAIN_CONTROLLER_OBJECT_NAME = "MainController";

    /**
     * Sets up the RMI components of the server that allows clients using this protocol
     * to connect and play.
     *
     * @param registryPort the port where the RMI registry will be accessible for clients
     * @throws RemoteException in case of errors with the remote communication.
     * @throws AlreadyBoundException in case of error with the remote object biding
     */
    public static void setup(int registryPort) throws RemoteException, AlreadyBoundException {
        MainControllerWrapper mainController = new MainControllerWrapper();

        Registry registry = LocateRegistry.createRegistry(registryPort);
        registry.bind(MAIN_CONTROLLER_OBJECT_NAME, mainController);

        System.out.println("RMI Server ready");
    }
}
