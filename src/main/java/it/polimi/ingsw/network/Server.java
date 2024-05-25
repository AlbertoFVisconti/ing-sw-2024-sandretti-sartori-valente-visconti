package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.RMIServer;
import it.polimi.ingsw.network.socket.SocketServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class Server {

    private static final int RMI_SERVER_PORT = 1234;
    private static final int SOCKET_SERVER_PORT = 1235;

    public static void main(String[] args) {
        try {
            RMIServer.setup(RMI_SERVER_PORT);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }

        new SocketServer(SOCKET_SERVER_PORT).start();
    }
}
