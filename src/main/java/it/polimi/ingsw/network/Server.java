package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.socket.SocketServer;
import it.polimi.ingsw.utils.GameBackupManager;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

/**
 * Contains main method to run Server components for socket and RMI clients to connect and join
 */
public class Server {
    // the port that the RMI clients needs to use to access the RMI registry
    private static final int RMI_SERVER_PORT = 1234;

    // the port that the socket clients needs to use to connect to the server
    private static final int SOCKET_SERVER_PORT = 1235;

    /**
     * Starts both the socket server and the RMI server
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        GameBackupManager.enableBackup();

        try {
            RMIServer.setup(RMI_SERVER_PORT);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        new SocketServer(SOCKET_SERVER_PORT).start();
    }
}
